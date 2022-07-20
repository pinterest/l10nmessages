package com.pinterest.l10nmessages;

import java.util.Locale;
import java.util.ResourceBundle;

/** To get a {@link ResourceBundle} given a baseName and a locale. */
@FunctionalInterface
public interface ResourceBundleProvider {

  /** Same contract as {@link ResourceBundle#getBundle(String, Locale)} */
  ResourceBundle get(String baseName, Locale locale);
}
