package com.pinterest.l10nmessages.example;

import static com.pinterest.l10nmessages.MessageFormatAdapterProviders.JDK_NAMED_ARGS;
import static com.pinterest.l10nmessages.MessageFormatLoadingCacheProviders.CONCURRENT_HASH_MAP;
import static com.pinterest.l10nmessages.example.Messages.*;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import com.ibm.icu.text.ListFormatter;
import com.ibm.icu.text.MessageFormat;
import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.MessageFormatAdapterProviders;
import com.pinterest.l10nmessages.OnFormatErrorException;
import com.pinterest.l10nmessages.OnFormatErrors;
import com.pinterest.l10nmessages.OnMissingArguments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Application {

  static final Logger logger = Logger.getLogger(Application.class.getName());

  public static void main(String... args) {
    System.out.println(withGeneratedEnum());
    System.out.println(withoutGeneratedEnum());
    System.out.println(withManualEnum());
    System.out.println(welcomeInFrench());
    System.out.println(formatWithMapEntries());
    System.out.println(formatWithMap());
    System.out.println(formatNumberedArgument());
    System.out.println(formatUntyped());
    withBaseArguments().forEach(System.out::println);
    try {
      onFormatErrorReThrow();
    } catch (OnFormatErrorException ofee) {
      System.out.println(ofee.getMessage());
    }
    System.out.println(onFormatErrorMessageFallback());
    System.out.println(onMissingArgumentLog());
    System.out.println(onMissingArgumentSubstitute());
    System.out.println(customizedMessageFormatAdapterProvider());
    caching().forEach(System.out::println);
    icu4jNumber().forEach(System.out::println);
    icu4jDate().forEach(System.out::println);
    icu4jSkeleton().forEach(System.out::println);
    icu4jGender().forEach(System.out::println);
    icu4jPluralForm().forEach(System.out::println);
    icu4jPluralFormWith0().forEach(System.out::println);
    icu4JComplex().forEach(System.out::println);
  }

  static String withGeneratedEnum() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    return m.format(welcome);
  }

  static String withoutGeneratedEnum() {
    L10nMessages<String> m =
        L10nMessages.builder(String.class)
            .resourceBundleName("com.pinterest.l10nmessages.example.Messages")
            .build();
    return m.format("welcome");
  }

  enum MyEnum {
    welcome,
    welcome_user,
  }

  static String withManualEnum() {
    L10nMessages<MyEnum> m =
        L10nMessages.builder(MyEnum.class)
            .resourceBundleName("com.pinterest.l10nmessages.example.Messages")
            .build();
    return m.format(MyEnum.welcome);
  }

  static String welcomeInFrench() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRENCH).build();
    return m.format(welcome);
  }

  static String formatWithMapEntries() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    return m.format(welcome_user, "username", "Bob");
  }

  static String formatWithMap() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    return m.format(welcome_user, ImmutableMap.of("username", "Bob"));
  }

  static String formatNumberedArgument() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    // Numbered arguments are referenced by name as any other arguments
    return m.format(welcome_user_numbered, "0", "Bob");
  }

  static String formatUntyped() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    // It is possible to load a message with an untyped key
    return m.formatUntyped("welcome_user", ImmutableMap.of("username", "Bob"));
  }

  static String formatWithArgumentBuilder() {
    // argument builder is only available if enumType = EnumType.WITH_ARGUMENT_BUILDERS is used.
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    // Format using an "argument builder" / FormatContext
    return m.format(welcome_user().username("Bob"));
  }

  static List<String> withBaseArguments() {
    // Create an instance providing base arguments. Base arguments will be available for any
    // formatting without having to pass them explicitly to format() later
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .baseArguments(ImmutableMap.of("username", "Mary"))
            .build();

    return Arrays.asList(
        m.format(welcome_user), m.format(bye_user), m.format(bye_user().username("Bob")));
  }

  static String onFormatErrorReThrow() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
            .onFormatError(
                (throwable, baseName, key, message) -> {
                  logger.severe(
                      String.format(
                          "Can't format message for baseName: `%1$s`, key: `%2$s` and message: `%3$s`",
                          baseName, key, message));
                  return OnFormatErrors.RETHROW.apply(throwable, baseName, key, message);
                })
            .build();

    return m.format(welcome_user);
  }

  static String onFormatErrorMessageFallback() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatAdapterProvider(MessageFormatAdapterProviders.JDK)
            .onFormatError(
                (throwable, baseName, key, message) -> {
                  logger.severe(
                      String.format(
                          "Can't format message for baseName: `%1$s`, key: `%2$s` and message: `%3$s`",
                          baseName, key, message));
                  return OnFormatErrors.MESSAGE_FALLBACK.apply(throwable, baseName, key, message);
                })
            .build();

    return m.format(welcome_user);
  }

  static String onMissingArgumentLog() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .onMissingArgument(
                (baseName, key, argumentName) -> {
                  logger.severe(
                      String.format(
                          "Argument: `%3$s` missing for baseName: `%1$s` and key: `%2$s`",
                          baseName, key, argumentName));
                  return OnMissingArguments.NOOP.apply(baseName, key, argumentName);
                })
            .build();
    return m.format(welcome_user);
  }

  static String onMissingArgumentSubstitute() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .onMissingArgument((baseName, key, argumentName) -> Optional.of("John Doe"))
            .build();
    return m.format(welcome_user);
  }

  static String customizedMessageFormatAdapterProvider() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatAdapterProvider(
                (message, locale) -> {
                  logger.info("Using named argument with JDK");
                  return JDK_NAMED_ARGS.get(message, locale);
                })
            .build();

    return m.format(welcome_user, "username", "Mary");
  }

  static List<String> caching() {
    List<String> strings = new ArrayList<>();

    // avoid
    {
      for (int i = 0; i < 5; i++) {
        strings.add(
            new MessageFormat("Welcome {username}!").format(ImmutableMap.of("username", "Bob")));
      }
    }

    // prefer
    {
      MessageFormat messageFormat = new MessageFormat("Welcome {username}!");
      for (int i = 0; i < 5; i++) {
        strings.add(messageFormat.format(ImmutableMap.of("username", "Bob")));
      }
    }

    // Multiple messages / different scopes
    {
      ConcurrentHashMap<String, MessageFormat> messagesConcurrentHashMap =
          new ConcurrentHashMap<>();
      messagesConcurrentHashMap.put("welcome_user", new MessageFormat("Welcome {username}!"));
      messagesConcurrentHashMap.put("bye_user", new MessageFormat("Bye {username}!"));

      scope1:
      {
        for (int i = 0; i < 5; i++) {
          strings.add(
              messagesConcurrentHashMap
                  .get("welcome_user")
                  .format(ImmutableMap.of("username", "Bob")));
        }
      }

      scope2:
      {
        for (int i = 0; i < 5; i++) {
          strings.add(
              messagesConcurrentHashMap
                  .get("welcome_user")
                  .format(ImmutableMap.of("username", "Bob")));
          strings.add(
              messagesConcurrentHashMap.get("bye_user").format(ImmutableMap.of("username", "Bob")));
        }
      }
    }

    // Similarly with disabled cache
    {
      L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();

      for (int i = 0; i < 5; i++) {
        strings.add(m.format(welcome_user, "username", "Bob"));
      }
    }

    // With cache enabled
    {
      L10nMessages<Messages> m =
          L10nMessages.builder(Messages.class)
              .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)
              .build();

      scope1:
      {
        for (int i = 0; i < 5; i++) {
          strings.add(m.format(welcome_user, "username", "Bob"));
        }
      }

      scope2:
      {
        for (int i = 0; i < 5; i++) {
          strings.add(m.format(welcome_user, "username", "Bob"));
          strings.add(m.format(bye_user, "username", "Bob"));
        }
      }
    }

    // Basic cache
    {
      L10nMessages<Messages> m =
          L10nMessages.builder(Messages.class)
              .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)
              .build();

      strings.add(m.format(welcome_user, "username", "Mary"));
    }

    // Guava cache
    {
      L10nMessages<Messages> m =
          L10nMessages.builder(Messages.class)
              .messageFormatLoadingCacheProvider(
                  (locale, messageFormatProvider) ->
                      CacheBuilder.newBuilder()
                              .maximumSize(1000)
                              .build(
                                  CacheLoader.from(
                                      (String message) ->
                                          messageFormatProvider.get(message, locale)))
                          ::getUnchecked)
              .build();
      strings.add(m.format(welcome_user, "username", "Mary"));
    }

    return strings;
  }

  static List<String> icu4jNumber() {
    return Stream.of(Locale.forLanguageTag("en-US"), Locale.forLanguageTag("fr-FR"))
        .map(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              return m.format(numbers().basic(1000.1).percentage(0.5).currency(50));
            })
        .collect(Collectors.toList());
  }

  static List<String> icu4jSkeleton() {
    Date dateForTest = getDateForTest();
    return Stream.of(Locale.ENGLISH, Locale.FRENCH)
        .map(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              return m.format(skeletons().date(dateForTest).number(1000.1234));
            })
        .collect(Collectors.toList());
  }

  static List<String> icu4jDate() {
    Date date = getDateForTest();
    return Stream.of(Locale.ENGLISH, Locale.FRENCH)
        .flatMap(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              return Stream.of(
                  m.format(today_is().todayDate(date)), m.format(today_is_short().todayDate(date)));
            })
        .collect(Collectors.toList());
  }

  private static Date getDateForTest() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2022, 05, 22, 20, 12, 34);
    return calendar.getTime();
  }

  static List<String> icu4jPluralForm() {
    List<String> strings =
        Stream.of(Locale.ENGLISH, Locale.FRENCH, Locale.JAPANESE)
            .flatMap(
                locale -> {
                  L10nMessages<Messages> m =
                      L10nMessages.builder(Messages.class).locale(locale).build();
                  return IntStream.range(0, 3)
                      .mapToObj(
                          numberMessages ->
                              m.format(you_have_messages().numberMessages(numberMessages)));
                })
            .collect(Collectors.toList());

    strings.addAll(
        Stream.of(Locale.forLanguageTag("ru"))
            .flatMap(
                locale -> {
                  L10nMessages<Messages> m =
                      L10nMessages.builder(Messages.class).locale(locale).build();
                  return IntStream.of(0, 1, 2, 5, 21, 22, 25)
                      .mapToObj(
                          numberMessages ->
                              m.format(you_have_messages().numberMessages(numberMessages)));
                })
            .collect(Collectors.toList()));

    return strings;
  }

  static List<String> icu4jPluralFormWith0() {
    return Stream.of(Locale.ENGLISH, Locale.FRENCH)
        .flatMap(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              return IntStream.range(0, 3)
                  .mapToObj(
                      numberMessages ->
                          m.format(you_have_messages_with_0().numberMessages(numberMessages)));
            })
        .collect(Collectors.toList());
  }

  static List<String> icu4jGender() {
    return Stream.of(Locale.ENGLISH, Locale.FRENCH)
        .flatMap(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              return Stream.of("female", "male", "other")
                  .map(userGender -> m.format(you_are_invited().gender(userGender)));
            })
        .collect(Collectors.toList());
  }

  static List<String> icu4JComplex() {
    return Stream.of(Locale.ENGLISH, Locale.FRENCH)
        .flatMap(
            locale -> {
              L10nMessages<Messages> m =
                  L10nMessages.builder(Messages.class).locale(locale).build();
              ListFormatter lf = ListFormatter.getInstance(locale);

              return Stream.of("female", "male", "other")
                  .flatMap(
                      userGender ->
                          Stream.of(Arrays.asList(), Arrays.asList("1"), Arrays.asList("3", "7"))
                              .map(
                                  numbers ->
                                      m.format(
                                          favorite_numbers()
                                              .numbers(lf.format(numbers))
                                              .numbersCount(numbers.size())
                                              .userGender(userGender))));
            })
        .collect(Collectors.toList());
  }
}
