package com.pinterest.l10nmessages.example;

import static com.pinterest.l10nmessages.example.Messages.welcome_user;

import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import java.util.Locale;

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

  public static void main(String... args) {
    System.out.println(welcomeUserInEnglish());
    System.out.println(welcomeUserInFrench());
  }

  static String welcomeUserInEnglish() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.ENGLISH).build();
    return m.format(welcome_user, "username", "Mary");
  }

  static String welcomeUserInFrench() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRENCH).build();
    return m.format(welcome_user, "username", "Mary");
  }
}
