package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.OnFormatErrors.MESSAGE_FALLBACK;
import static com.pinterest.l10nmessages.OnMissingArguments.NOOP;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A message loader and formatter designed for string localization.
 *
 * <p>Builds on top of {@link ResourceBundle} to load localized messages. Offers strong typing of
 * the message keys.
 *
 * <p>Use {@link L10nProperties} to generate an {@link Enum} of message keys from an existing {@code
 * properties} file and to apply build time checks to its messages.
 *
 * <p>The generated enum can then be provided to the builder ({@link
 * L10nMessagesBuilder#builder(Class)}). {@link L10nMessages} instance created from that builder
 * will provide strong typing on the keys for the {@link L10nMessages#format(Object)}
 *
 * <p>Message are formatted using ICU {@link com.ibm.icu.text.MessageFormat} if available in the
 * classpath or else fallbacks to JDK implementation {@link java.text.MessageFormat}. The JDK
 * implementation is limited but requires no extra dependency. For evolved localization (named
 * parameter, plural support, etc) consider adopting ICU.
 *
 * <h3>Thread safety</h3>
 *
 * <p>{@link L10nMessages} thread safety may vary depending on its configuration. Instances with no
 * message format caching (default configuration) are thread safe. When using caching, it will
 * depend on the implementation of the {@link MessageFormatLoadingCache}.
 *
 * <p>This stems from the MessageFormat thread safety. In their own words: "MessageFormats are not
 * synchronized. It is recommended to create separate format instances for each thread. If multiple
 * threads access a format concurrently, it must be synchronized externally."
 *
 * <p>If the {@link MessageFormatLoadingCache} exposes (to different threads) the same instance of
 * an un-synchronized message format, the {@link L10nMessages} will not be thread safe anymore.
 * That's the case with a simple cache like {@link
 * MessageFormatLoadingCacheProviders#CONCURRENT_HASH_MAP}
 *
 * <p>L10nMessages instances are relatively inexpensive to create and benefit from caching
 * capabilities of the underlying {@link ResourceBundle}. For simplicity, keep the scope of the
 * instance limited (at least to the thread level).
 *
 * @param <T> the key type. It is usually an enum generated by the annotation processor (or the
 *     Maven plugin). If not, it can be any type. If {@link String} is used, the message loading
 *     will be similar to plain Java.
 * @author jeanaurambault
 */
public class L10nMessages<T> {

  static final String BASENAME_FIELD = "BASENAME";
  static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS_FIELD = "MESSAGE_FORMAT_ADAPTER_PROVIDERS";

  private final Locale locale;
  private final ResourceBundle resourceBundle;
  private final MessageFormatLoadingCache messageFormatLoadingCache;
  private final Map<String, Object> baseArguments;
  private final OnMissingArgument onMissingArgument;
  private final OnFormatError onFormatError;

  private L10nMessages(
      Locale locale,
      ResourceBundle resourceBundle,
      Map<String, Object> baseArguments,
      MessageFormatLoadingCache messageFormatLoadingCache,
      OnMissingArgument onMissingArgument,
      OnFormatError onFormatError) {
    this.locale = Objects.requireNonNull(locale);
    this.resourceBundle = Objects.requireNonNull(resourceBundle);
    this.messageFormatLoadingCache = Objects.requireNonNull(messageFormatLoadingCache);
    this.baseArguments = Objects.requireNonNull(baseArguments);
    this.onMissingArgument = Objects.requireNonNull(onMissingArgument);
    this.onFormatError = Objects.requireNonNull(onFormatError);
  }

  /**
   * Creates a L10nMessagesBuilder with default settings.
   *
   * <p>The resource bundle basename must be provided before trying to create the L10nMessages
   * instance using {@link L10nMessagesBuilder#resourceBundleName(String)}
   *
   * @param <T> type of the keys used in format()
   * @return the builder
   */
  public static <T> L10nMessagesBuilder<T> builder() {
    return new L10nMessagesBuilder<>();
  }

  /**
   * Creates a builder with settings initialized from the provided class.
   *
   * <p>Typically the class is a generated Enum from the Annotation processor (or Maven plugin)
   * which defines the resource bundle basename and messageFormatAdapterProvider to use and they
   * "typed" keys (enum values).
   *
   * <p>If the provided class doesn't contain information about the resource bundle basename, it
   * must be provided before trying to create the L10nMessages instance using {@link
   * L10nMessagesBuilder#resourceBundleName(String)}.
   *
   * @param clazz to initialize the builder and provide strong key typing
   * @param <T> type of the keys used in format()
   * @return the builder
   */
  public static <T> L10nMessagesBuilder<T> builder(Class<T> clazz) {
    L10nMessagesBuilder<T> l10nMessagesBuilder = new L10nMessagesBuilder<>();

    String basename = Reflection.getPublicStaticFiledOrNull(BASENAME_FIELD, clazz);
    if (basename != null) {
      l10nMessagesBuilder.resourceBundleName(basename);
    }

    String messageFormatAdapterProviders =
        Reflection.getPublicStaticFiledOrNull(MESSAGE_FORMAT_ADAPTER_PROVIDERS_FIELD, clazz);
    if (messageFormatAdapterProviders != null) {
      l10nMessagesBuilder.messageFormatAdapterProvider(
          MessageFormatAdapterProviders.valueOf(messageFormatAdapterProviders));
    }

    return l10nMessagesBuilder;
  }

  /**
   * Get the locale used to format messages.
   *
   * @return the locale used to format messages
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Format a message for an "untyped" key and a given map of arguments.
   *
   * <p>The key is passed as it to the underlying {@link ResourceBundle}.
   *
   * @param key the "untyped" key
   * @param arguments arguments used to format the message
   * @return the formatted message
   */
  public String formatUntyped(String key, Map<String, Object> arguments) {
    Objects.requireNonNull(key, "key is null");
    Objects.requireNonNull(arguments, "arguments is null");

    String formattedMessage;
    String message = null;

    try {
      message = resourceBundle.getString(key);
      MessageFormatAdapter messageFormatAdapter = messageFormatLoadingCache.get(message);

      HashMap<String, Object> finalArguments = new LinkedHashMap<>(baseArguments);
      finalArguments.putAll(arguments);

      checkMissingArguments(
          onMissingArgument,
          key,
          messageFormatAdapter,
          finalArguments,
          resourceBundle.getBaseBundleName());

      formattedMessage = messageFormatAdapter.format(finalArguments);

    } catch (Throwable t) {
      formattedMessage = onFormatError.apply(t, resourceBundle.getBaseBundleName(), key, message);
    }
    return formattedMessage;
  }

  private static void checkMissingArguments(
      OnMissingArgument onMissingArgument,
      String key,
      MessageFormatAdapter messageFormatAdapter,
      HashMap<String, Object> finalArguments,
      String resourceBundleBaseName) {
    if (onMissingArgument != NOOP) {
      messageFormatAdapter.getArgumentNames().stream()
          .filter(argumentName -> !finalArguments.containsKey(argumentName))
          .forEach(
              argumentName ->
                  onMissingArgument
                      .apply(resourceBundleBaseName, key, argumentName)
                      .ifPresent(
                          o -> {
                            finalArguments.put(argumentName, o);
                          }));
    }
  }

  /**
   * Formats a message given a "typed" key and a given map of arguments.
   *
   * @param key the "typed" key
   * @param arguments arguments used to format the message
   * @return the formatted message
   */
  public String format(T key, Map<String, Object> arguments) {
    Objects.requireNonNull(key, "key is null");
    return formatUntyped(key.toString(), arguments);
  }

  /**
   * Formats a message given a "typed" key and no arguments
   *
   * @param key the "typed" key
   * @return the formatted message
   */
  public String format(T key) {
    return format(key, Maps.of());
  }

  /**
   * Formats a message given a "typed" key and one argument.
   *
   * @param key the "typed" key
   * @param k1 key/name of the first argument
   * @param v1 value of the first argument
   * @return the formatted message
   */
  public String format(T key, String k1, Object v1) {
    return format(key, Maps.of(k1, v1));
  }

  /**
   * Formats a message given a "typed" key and two arguments.
   *
   * @param key the "typed" key
   * @param k1 key/name of the first argument
   * @param v1 value of the first argument
   * @param k2 key/name of the second argument
   * @param v2 value of the second argument
   * @return the formatted message
   */
  public String format(T key, String k1, Object v1, String k2, Object v2) {
    return format(key, Maps.of(k1, v1, k2, v2));
  }

  /**
   * Formats a message given a "typed" key and three arguments.
   *
   * @param key the "typed" key
   * @param k1 key/name of the first argument
   * @param v1 value of the first argument
   * @param k2 key/name of the second argument
   * @param v2 value of the second argument
   * @param k3 key/name of the third argument
   * @param v3 value of the third argument
   * @return the formatted message
   */
  public String format(T key, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
    return format(key, Maps.of(k1, v1, k2, v2, k3, v3));
  }

  /**
   * Formats a message given a "typed" key and four arguments.
   *
   * @param key the "typed" key
   * @param k1 key/name of the first argument
   * @param v1 value of the first argument
   * @param k2 key/name of the second argument
   * @param v2 value of the second argument
   * @param k3 key/name of the third argument
   * @param v3 value of the third argument
   * @param k4 key/name of the fourth argument
   * @param v4 value of the fourth argument
   * @return the formatted message
   */
  public String format(
      T key,
      String k1,
      Object v1,
      String k2,
      Object v2,
      String k3,
      Object v3,
      String k4,
      Object v4) {
    return format(key, Maps.of(k1, v1, k2, v2, k3, v3, k4, v4));
  }

  /**
   * Formats a message given a "typed" key and five arguments.
   *
   * @param key the "typed" key
   * @param k1 key/name of the first argument
   * @param v1 value of the first argument
   * @param k2 key/name of the second argument
   * @param v2 value of the second argument
   * @param k3 key/name of the third argument
   * @param v3 value of the third argument
   * @param k4 key/name of the fourth argument
   * @param v4 value of the fourth argument
   * @param k5 key/name of the fifth argument
   * @param v5 value of the fifth argument
   * @return the formatted message
   */
  public String format(
      T key,
      String k1,
      Object v1,
      String k2,
      Object v2,
      String k3,
      Object v3,
      String k4,
      Object v4,
      String k5,
      Object v5) {
    return format(key, Maps.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
  }

  /**
   * Formats a message given a format context (contains the key and argument map).
   *
   * @param formatContext the format context
   * @return the formatted message
   */
  public String format(FormatContext<T> formatContext) {
    return format(formatContext.getKey(), formatContext.getArguments());
  }

  /**
   * The builder to create {@link L10nMessages} instances.
   *
   * <p>If a locale is not specified, it will use the default value: {@link Locale#getDefault()}.
   *
   * <p>Other default settings are:
   *
   * <ul>
   *   <li>Base arguments are empty by default
   *   <li>The strategy for missing arguments is to do nothing
   *   <li>The strategy for format error is to show the original message
   *   <li>No cache is enabled
   *   <li>Use ICU4j if on the classpath else JDK message format
   *   <li>For Java 8, it will accept UTF-8 properties file and fallback to ISO-8859-1
   * </ul>
   *
   * @param <T> the key type that will be used in the L10nMessages instance.
   */
  public static class L10nMessagesBuilder<T> {

    private String resourceBundleName;
    private Locale locale = Locale.getDefault();
    private Map<String, Object> baseArguments = Collections.emptyMap();
    private OnMissingArgument onMissingArgument = NOOP;
    private OnFormatError onFormatError = MESSAGE_FALLBACK;
    private ResourceBundleProvider resourceBundleProvider =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT;
    private MessageFormatLoadingCacheProvider messageFormatLoadingCacheProvider =
        MessageFormatLoadingCacheProviders.NO_CACHE;
    private MessageFormatAdapterProvider messageFormatAdapterProvider =
        MessageFormatAdapterProviders.AUTO;

    /**
     * Specifies the resource bundle name which is used to load the resource bundle, resource bundle
     * that will be used to load the messages.
     *
     * @param resourceBundleName the resource bundle name
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> resourceBundleName(String resourceBundleName) {
      this.resourceBundleName = Objects.requireNonNull(resourceBundleName);
      return this;
    }

    /**
     * Specifies the locale that will be used when formatting messages.
     *
     * @param locale the locale used to format messages
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> locale(Locale locale) {
      this.locale = Objects.requireNonNull(locale);
      return this;
    }

    /**
     * Specifies the base arguments that are used when formatting messages.
     *
     * <p>Base arguments are added to the arguments provided to the format function before
     * formatting messages.
     *
     * @param baseArguments the base arguments
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> baseArguments(Map<String, Object> baseArguments) {
      this.baseArguments = Objects.requireNonNull(baseArguments);
      return this;
    }

    /**
     * Specifies the MessageFormatLoadingCacheProvider used to obtain a cache that will be used to
     * cache the message formats.
     *
     * @param messageFormatLoadingCacheProvider the MessageFormatLoadingCacheProvider to use
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> messageFormatLoadingCacheProvider(
        MessageFormatLoadingCacheProvider messageFormatLoadingCacheProvider) {
      this.messageFormatLoadingCacheProvider =
          Objects.requireNonNull(messageFormatLoadingCacheProvider);
      return this;
    }

    /**
     * Specifies the ResourceBundleProvider used to obtain the resource bundle that will be used to
     * load messages.
     *
     * @param resourceBundleProvider the ResourceBundleProvider to use
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> resourceBundleProvider(
        ResourceBundleProvider resourceBundleProvider) {
      this.resourceBundleProvider = Objects.requireNonNull(resourceBundleProvider);
      return this;
    }

    /**
     * Specifies the MessageFormatAdapterProvider used to transform the messages into {@link
     * MessageFormatAdapter}
     *
     * @param messageFormatAdapterProvider the MessageFormatAdapterProvider to use
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> messageFormatAdapterProvider(
        MessageFormatAdapterProvider messageFormatAdapterProvider) {
      this.messageFormatAdapterProvider = Objects.requireNonNull(messageFormatAdapterProvider);
      return this;
    }

    /**
     * Specifies the strategy to apply when missing argument are found during formatting
     *
     * @param onMissingArgument the strategy to apply when missing argument are found during
     *     formatting
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> onMissingArgument(OnMissingArgument onMissingArgument) {
      this.onMissingArgument = Objects.requireNonNull(onMissingArgument);
      return this;
    }

    /**
     * Specifies the strategy to apply when formatting errors happen.
     *
     * <p>Typical errors: missing key, invalid messages, missing argument, etc.
     *
     * @param onFormatError the strategy to apply when a formatting errors happen
     * @return the builder (for chaining)
     */
    public L10nMessagesBuilder<T> onFormatError(OnFormatError onFormatError) {
      this.onFormatError = Objects.requireNonNull(onFormatError);
      return this;
    }

    /**
     * Creates the {@link L10nMessages} instance.
     *
     * @return the L10nMessages instance with requested options
     * @throws IllegalArgumentException if the resource bundle name was not specified
     */
    public L10nMessages<T> build() {

      if (resourceBundleName == null) {
        throw new IllegalArgumentException(
            "A resource bundle name must be explicitly provided or provided through the "
                + BASENAME_FIELD
                + " field");
      }

      return new L10nMessages<T>(
          locale,
          Objects.requireNonNull(
              resourceBundleProvider.get(resourceBundleName, locale),
              "resourceBundleProvider must not return null"),
          baseArguments,
          Objects.requireNonNull(
              messageFormatLoadingCacheProvider.get(locale, messageFormatAdapterProvider),
              "messageFormatLoadingCacheProvider must not return null"),
          onMissingArgument,
          onFormatError);
    }
  }
}
