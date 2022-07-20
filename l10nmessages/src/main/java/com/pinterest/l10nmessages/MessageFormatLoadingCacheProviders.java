package com.pinterest.l10nmessages;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public enum MessageFormatLoadingCacheProviders implements MessageFormatLoadingCacheProvider {
  /** No caching, it will re-create a {@link MessageFormatAdapter} instance on every call. */
  NO_CACHE {
    @Override
    public MessageFormatLoadingCache get(
        Locale locale, MessageFormatAdapterProvider messageFormatAdapterProvider) {
      return message -> messageFormatAdapterProvider.get(message, locale);
    }
  },
  /**
   * Cache implementation based on ConcurrentHashMap. No eviction policy, to be used only for
   * performance improvement when memory won't be an issue.
   */
  CONCURRENT_HASH_MAP {

    @Override
    public MessageFormatLoadingCache get(
        Locale locale, MessageFormatAdapterProvider messageFormatAdapterProvider) {
      ConcurrentHashMap<String, MessageFormatAdapter> map = new ConcurrentHashMap<>();
      return message ->
          map.computeIfAbsent(message, m -> messageFormatAdapterProvider.get(m, locale));
    }
  }
}
