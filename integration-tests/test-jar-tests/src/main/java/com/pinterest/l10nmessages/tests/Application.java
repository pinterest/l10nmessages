package com.pinterest.l10nmessages.tests;

import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import com.pinterest.l10nmessages.tests.m2.Strings2;
import java.util.Locale;

@L10nProperties(baseName = "com.pinterest.l10nmessages.tests.m2.Strings2")
public class Application {

  static String english() {
    L10nMessages<Strings2> m = L10nMessages.builder(Strings2.class).locale(Locale.ENGLISH).build();
    return m.format(Strings2.hello);
  }

  static String french() {
    L10nMessages<Strings2> m = L10nMessages.builder(Strings2.class).locale(Locale.FRENCH).build();
    return m.format(Strings2.hello);
  }
}
