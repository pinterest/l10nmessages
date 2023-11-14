package com.pinterest.l10nmessages;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

class AlternateLanguageCodes {

  private AlternateLanguageCodes() {
    throw new AssertionError();
  }

  static Map<String, String> ALTERNATE_LANGUAGE_CODES = getAlternateLanguageCodes();

  static Map<String, String> getAlternateLanguageCodes() {
    Map<String, String> alternateLanguages = new LinkedHashMap<>();
    alternateLanguages.put("iw", "he");
    alternateLanguages.put("he", "iw");
    alternateLanguages.put("in", "id");
    alternateLanguages.put("id", "in");
    alternateLanguages.put("ji", "yi");
    alternateLanguages.put("yi", "ji");
    return alternateLanguages;
  }

  public static boolean hasAlternateLanguageCode(Locale locale) {
    return ALTERNATE_LANGUAGE_CODES.keySet().contains(locale.getLanguage());
  }

  public static String getAlternateResourceName(Locale locale, String resourceName) {
    return getAlternateResourceName(locale.getLanguage(), resourceName);
  }

  static String getAlternateResourceName(String language, String resourceName) {
    if (ALTERNATE_LANGUAGE_CODES.containsKey(language)) {
      String alternateLanguage = ALTERNATE_LANGUAGE_CODES.get(language);
      int idx = resourceName.lastIndexOf("_" + language);
      if (idx != -1) {
        resourceName =
            resourceName.substring(0, idx)
                + "_"
                + alternateLanguage
                + resourceName.substring(idx + 3);
      }
    }
    return resourceName;
  }
}
