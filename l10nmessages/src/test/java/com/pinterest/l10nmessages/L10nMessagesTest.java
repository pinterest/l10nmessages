package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.Messages.BASENAME;
import static com.pinterest.l10nmessages.Messages.format_multi_args;
import static com.pinterest.l10nmessages.Messages.utf8_key__u128512_;
import static com.pinterest.l10nmessages.Messages.utf8_message;
import static com.pinterest.l10nmessages.Messages.welcome;
import static com.pinterest.l10nmessages.Messages.welcome_with_gender;
import static com.pinterest.l10nmessages.Messages.welcome_with_name;
import static com.pinterest.l10nmessages.Messages.welcome_with_name_and_gender;
import static com.pinterest.l10nmessages.OnFormatErrors.RETHROW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class L10nMessagesTest {

  @BeforeAll
  public static void beforeAll() {
    Locale.setDefault(Locale.US);
  }

  @Test
  void builderResourceBundleNameNull() {
    assertThatThrownBy(() -> L10nMessages.builder().build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(
            "A resource bundle name must be explicitly provided or provided through the BASENAME field");
  }

  @Test
  void builderResourceBundleProviderReturnsNull() {

    assertThatThrownBy(
            () ->
                L10nMessages.builder()
                    .resourceBundleName("com.pinterest.l10nmessages.null")
                    .resourceBundleProvider((baseName, locale) -> null)
                    .build())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("resourceBundleProvider must not return null");
  }

  @Test
  void builderResourceBundleProviderMissingBundle() {
    assertThatThrownBy(
            () ->
                L10nMessages.builder()
                    .resourceBundleName("com.pinterest.l10nmessages.missing")
                    .build())
        .isInstanceOf(MissingResourceException.class)
        .hasMessage(
            "Can't find bundle for base name com.pinterest.l10nmessages.missing, locale en_US");
  }

  @Test
  void builderMessageFormatLoadingCacheProviderReturnsNull() {

    assertThatThrownBy(
            () ->
                L10nMessages.builder()
                    .resourceBundleName("com.pinterest.m.forTest")
                    .resourceBundleProvider(resourceBundleProviderForTest)
                    .messageFormatLoadingCacheProvider(
                        (locale, messageFormatAdapterProvider) -> null)
                    .build())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("messageFormatLoadingCacheProvider must not return null");
  }

  @Test
  public void builderForClassWithBasename() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
  }

  @Test
  public void builderForClassWithoutBasename() {
    assertThatThrownBy(() -> L10nMessages.builder(String.class).build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(
            "A resource bundle name must be explicitly provided or provided through the BASENAME field");
  }

  @Test
  void getDefaultLocale() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();
    assertThat(m.getLocale()).isEqualTo(Locale.US);
  }

  @Test
  void getLocale() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    assertThat(m.getLocale()).isEqualTo(Locale.FRANCE);
  }

  @Test
  public void enumTypedFormat() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
  }

  @Test
  public void enumTypedFormatWithLocale() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    assertThat(m.format(welcome)).isEqualTo("Bienvenue!");
  }

  @Test
  public void enumTypedFormatUntypedKey() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();
    String untypedKey = "wel" + "come";
    assertThat(m.formatUntyped(untypedKey, ImmutableMap.of())).isEqualTo("Welcome!");
  }

  @Test
  public void keyTypedFormat() {
    L10nMessages<String> m =
        L10nMessages.<String>builder()
            .resourceBundleName(com.pinterest.l10nmessages.Messages.BASENAME)
            .build();
    assertThat(m.format("welcome")).isEqualTo("Welcome!");
  }

  @Test
  public void notTypedFormat() {
    L10nMessages<Object> m =
        L10nMessages.builder()
            .resourceBundleName(com.pinterest.l10nmessages.Messages.BASENAME)
            .build();
    Object notTypedKey = "welcome";
    assertThat(m.format(notTypedKey)).isEqualTo("Welcome!");
  }

  @Test
  public void formatMultiArgs() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class)
            .baseArguments(ImmutableMap.of("1", "b1", "2", "b2", "3", "b3", "4", "b4", "5", "b5"))
            .build();
    assertThat(m.format(format_multi_args)).isEqualTo("b1 b2 b3 b4 b5");
    assertThat(m.format(format_multi_args, "1", "1")).isEqualTo("1 b2 b3 b4 b5");
    assertThat(m.format(format_multi_args, "1", "1", "2", "2")).isEqualTo("1 2 b3 b4 b5");
    assertThat(m.format(format_multi_args, "1", "1", "2", "2", "3", "3")).isEqualTo("1 2 3 b4 b5");
    assertThat(m.format(format_multi_args, "1", "1", "2", "2", "3", "3", "4", "4"))
        .isEqualTo("1 2 3 4 b5");
    assertThat(m.format(format_multi_args, "1", "1", "2", "2", "3", "3", "4", "4", "5", "5"))
        .isEqualTo("1 2 3 4 5");
  }

  @Test
  public void formatFormatContext() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();

    assertThat(m.format(new FormatContext<Messages>() {
      @Override
      public Messages getKey() {
        return welcome_with_name;
      }

      @Override
      public Map<String, Object> getArguments() {
        return Maps.of("userName", "Mary");
      }
    })).isEqualTo("Welcome Mary!");
  }

  @Test
  public void icuMessageFormat() {
    L10nMessages<com.pinterest.l10nmessages.Messages> m =
        L10nMessages.builder(Messages.class).build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
    assertThat(m.format(welcome_with_gender, "userGender", "female")).isEqualTo("Welcome Mrs.");
    assertThat(m.format(welcome_with_name, "userName", "Mary")).isEqualTo("Welcome Mary!");
    assertThat(m.format(welcome_with_name_and_gender, "userName", "Mary", "userGender", "female"))
        .isEqualTo("Welcome Mrs. Mary");
  }

  @Test
  public void jdkNamedMessageFormat() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK_NAMED_ARGS)
            .build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
    assertThat(m.format(welcome_with_name, "userName", "Mary")).isEqualTo("Welcome Mary!");
  }

  @Test
  void concurrentHashMapCache() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatLoadingCacheProvider(
                MessageFormatLoadingCacheProviders.CONCURRENT_HASH_MAP)
            .build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
  }

  @Test
  void guavaLoadingCache() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatLoadingCacheProvider(
                (locale, messageFormatProvider) ->
                    CacheBuilder.newBuilder()
                            .maximumSize(10000)
                            .build(
                                CacheLoader.from(
                                    (String message) -> messageFormatProvider.get(message, locale)))
                        ::getUnchecked)
            .build();
    assertThat(m.format(welcome)).isEqualTo("Welcome!");
  }

  @Test
  void onMissingArgument() {
    OnMissingArgument onMissingArgumentMock = Mockito.mock(OnMissingArgument.class);
    when(onMissingArgumentMock.apply(BASENAME, "welcome_with_name", "userName"))
        .thenReturn(Optional.of("John Doe"));
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class).onMissingArgument(onMissingArgumentMock).build();
    assertThat(m.format(welcome_with_name)).isEqualTo("Welcome John Doe!");
    verify(onMissingArgumentMock).apply(BASENAME, "welcome_with_name", "userName");
  }

  @Test
  void utf8Message() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    assertThat(m.format(utf8_message)).isEqualTo("utf8 string: \uD83D\uDE00");
  }

  @Test
  void showKeysInsteadOfMessage() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .resourceBundleProvider(
                (baseName, locale) -> {
                  final ResourceBundle resourceBundle =
                      ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(baseName, locale);
                  return new ResourceBundle() {
                    @Override
                    protected Object handleGetObject(String key) {
                      // will fail if key is not declared in resource bundle
                      resourceBundle.getString(key);
                      return key;
                    }

                    @Override
                    public Enumeration<String> getKeys() {
                      return resourceBundle.getKeys();
                    }
                  };
                })
            .build();

    assertThat(m.format(utf8_key__u128512_)).isEqualTo("utf8.key.\uD83D\uDE00");
  }

  @Test
  void onFormatErrorGetMessage() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .onFormatError(
                (throwable, baseName, key, message) -> {
                  assertThat(message).isNull();
                  assertThat(throwable.getMessage())
                      .isEqualTo(
                          "Can't find resource for bundle java.util.PropertyResourceBundle, key missing_key");
                  return key;
                })
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
            .build();
    assertThat(m.formatUntyped("missing_key", ImmutableMap.of())).isEqualTo("missing_key");
  }

  @Test
  void onFormatErrorGetMessageFormat() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .onFormatError(
                (throwable, baseName, key, message) -> {
                  assertThat(throwable.getMessage())
                      .isEqualTo("can't parse argument number: userName");
                  return "fortest: " + message;
                })
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
            .build();
    assertThat(m.format(welcome_with_name, "0", "Bob")).isEqualTo("fortest: Welcome {userName}!");
  }

  @Test
  void onFormatErrorDefault() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
            .build();
    assertThat(m.formatUntyped("missing_key", ImmutableMap.of())).isEqualTo("missing_key");
    assertThat(m.format(welcome_with_name, "0", "Bob")).isEqualTo("Welcome {userName}!");
  }

  @Test
  void onFormatErrorRethrow() {
    assertThatThrownBy(
            () -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class)
                      .onFormatError(RETHROW)
                      .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
                      .build();
              m.format(welcome_with_name, "0", "Bob");
            })
        .isInstanceOf(OnFormatErrorException.class)
        .hasMessage("java.lang.IllegalArgumentException: can't parse argument number: userName");
  }

  //  enum MessagesForTest {
  //    welcome,
  //    welcome_with_gender,
  //    welcome_with_name,
  //    welcome_with_name_and_gender,
  //    format_multi_args,
  //    missing;
  //
  //    public static final String BASENAME = "com.pinterest.l10nmessages.Messages";
  //  }
  //
  public static final String WELCOME = "Welcome";

  static final ResourceBundleProvider resourceBundleProviderForTest =
      (baseName, locale) ->
          new ListResourceBundle() {

            @Override
            protected Object[][] getContents() {
              return new Object[][] {
                {"welcome", WELCOME},
              };
            }
          };
}
