package com.pinterest.l10nmessages;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public enum ResourceBundleProviders implements ResourceBundleProvider {

  /**
   * Provides a ResourceBundle that tries to load the properties files as UTF-8 and fallbacks to
   * ISO_8859_1 if there is an error.
   *
   * <p>That's the standard behavior of {@link ResourceBundle#getBundle(String)} in Java 9 but is
   * not natively supported in Java 8 (properties file are loaded as ISO-8859-1 instead).
   *
   * <p>For Java 9+ we use the standard implementation. For Java 8, the library backports some of
   * the properties loading logic. Note, the implementation is slightly different (not reading the
   * system property: "java.util.PropertyResourceBundle.encoding", and different {@link
   * ResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}. Use your own
   * {@link ResourceBundleProvider} to implement an exact backport of Java 9 if needed.
   *
   * <p>See {@link java.util.PropertyResourceBundle#PropertyResourceBundle(InputStream)} for
   * implementation details and difference between Java 8 and Java 9.
   */
  DEFAULT_OR_BACKPORT {
    @Override
    public ResourceBundle get(String baseName, Locale locale) {
      ResourceBundle resourceBundle;

      if (JavaVersions.isJava17AndUp()) {
        resourceBundle = ResourceBundle.getBundle(baseName, locale);
      } else {
        resourceBundle =
            ResourceBundle.getBundle(
                baseName,
                locale,
                new CharsetDecoderResourceBundleControl(
                    new UTF8FallbackISO88591Charset().newDecoder()));
      }

      return resourceBundle;
    }
  };
}
