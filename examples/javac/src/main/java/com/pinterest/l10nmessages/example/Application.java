package com.pinterest.l10nmessages.example;

import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import java.util.Locale;

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

  public static void main(String[] args) {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class).locale(Locale.forLanguageTag("fr-FR")).build();
    System.out.println(m.format(Messages.welcome_user, "0", "Mary"));
  }
}
