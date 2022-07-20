package com.pinterest.l10nmessages;

import java.util.Locale;

/**
 * Function that provides {@link MessageFormatLoadingCache} for a given locale and
 * messageFormatAdapterProvider.
 */
@FunctionalInterface
public interface MessageFormatLoadingCacheProvider {

  /**
   * @param locale the locale to create the instance
   * @param messageFormatAdapterProvider the {@link MessageFormatAdapterProvider} used by the cache
   * @return A MessageFormatLoadingCache instance, not null
   */
  MessageFormatLoadingCache get(
      Locale locale, MessageFormatAdapterProvider messageFormatAdapterProvider);
}
