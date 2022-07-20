package com.pinterest.l10nmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CharsetDecoder;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * A {@link ResourceBundle.Control} to load Java properties with a specified {@link CharsetDecoder}.
 */
class CharsetDecoderResourceBundleControl extends ResourceBundle.Control {

  CharsetDecoder charsetDecoder;

  public CharsetDecoderResourceBundleControl(CharsetDecoder charsetDecoder) {
    this.charsetDecoder = Objects.requireNonNull(charsetDecoder);
  }

  @Override
  public ResourceBundle newBundle(
      String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
      throws IOException {

    if (!"java.properties".equals(format)) {
      throw new RuntimeException("Only support java.properties format");
    }

    ResourceBundle bundle = null;
    String bundleName = toBundleName(baseName, locale);
    String resourceName = toResourceName(bundleName, "properties");

    try (InputStream stream = loader.getResourceAsStream(resourceName)) {
      if (stream != null) {
        bundle = new PropertyResourceBundle(new InputStreamReader(stream, charsetDecoder));
      }
    }
    return bundle;
  }
}
