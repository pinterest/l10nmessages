package com.pinterest.l10nmessages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** To get a {@link ResourceBundle} given a baseName and a locale. */
@FunctionalInterface
public interface ResourceBundleProvider {

  /**
   * Same contract as {@link ResourceBundle#getBundle(String, Locale)}
   *
   * @param baseName the base name of the resource bundle, a fully qualified class name
   * @param locale the locale for which a resource bundle is desired
   * @return a resource bundle for the given base name and locale
   * @throws NullPointerException if <code>baseName</code> or <code>locale</code> is <code>null
   *     </code>
   * @throws MissingResourceException if no resource bundle for the specified base name can be found
   */
  ResourceBundle get(String baseName, Locale locale);
}
