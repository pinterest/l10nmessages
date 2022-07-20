package com.pinterest.l10nmessages.example;

import static com.pinterest.l10nmessages.example.Messages.you_have_messages;
import static java.util.stream.Collectors.toList;

import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

  public static void main(String... args) {
    print(youHaveMessagesEnglish());
    print(youHaveMessagesFrench());
    print(youHaveMessagesJapanese());
    print(youHaveMessagesRussian());
  }

  static List<String> youHaveMessagesEnglish() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.ENGLISH).build();
    return IntStream.range(0, 3)
        .mapToObj(numberMessages -> m.format(you_have_messages, "numberMessages", numberMessages))
        .collect(toList());
  }

  static List<String> youHaveMessagesFrench() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRENCH).build();
    return IntStream.range(0, 3)
        .mapToObj(numberMessages -> m.format(you_have_messages, "numberMessages", numberMessages))
        .collect(toList());
  }

  static List<String> youHaveMessagesJapanese() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.JAPANESE).build();
    return IntStream.range(0, 3)
        .mapToObj(numberMessages -> m.format(you_have_messages, "numberMessages", numberMessages))
        .collect(toList());
  }

  static List<String> youHaveMessagesRussian() {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class).locale(Locale.forLanguageTag("ru")).build();
    return IntStream.of(0, 1, 2, 5, 21, 22, 25)
        .mapToObj(numberMessages -> m.format(you_have_messages, "numberMessages", numberMessages))
        .collect(toList());
  }

  private static void print(List<String> strings) {
    strings.forEach(System.out::println);
    System.out.println();
  }
}
