package com.pinterest.l10nmessages;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.MessageFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageFormatLoadingCacheProvidersTest {

  @Test
  void noCache() {
    MessageFormatAdapterProvider mockMessageFormatAdapterProvider =
        Mockito.mock(MessageFormatAdapterProvider.class);

    when(mockMessageFormatAdapterProvider.get("test", Locale.FRANCE))
        .thenReturn(new MessageFormatAdapterJDK(new MessageFormat("test", Locale.FRANCE)));

    MessageFormatLoadingCache messageFormatLoadingCache =
        MessageFormatLoadingCacheProviders.NO_CACHE.get(
            Locale.FRANCE, mockMessageFormatAdapterProvider);

    MessageFormatAdapter call1 = messageFormatLoadingCache.get("test");
    assertThat(call1.format(Maps.of())).isEqualTo("test");

    MessageFormatAdapter call2 = messageFormatLoadingCache.get("test");
    assertThat(call2.format(Maps.of())).isEqualTo("test");

    verify(mockMessageFormatAdapterProvider, times(2)).get("test", Locale.FRANCE);
  }

  @Test
  void concurrentHashMap() {
    MessageFormatAdapterProvider mockMessageFormatAdapterProvider =
        Mockito.mock(MessageFormatAdapterProvider.class);

    when(mockMessageFormatAdapterProvider.get("test", Locale.FRANCE))
        .thenReturn(new MessageFormatAdapterJDK(new MessageFormat("test", Locale.FRANCE)));

    MessageFormatLoadingCache messageFormatLoadingCache =
        MessageFormatLoadingCacheProviders.CONCURRENT_HASH_MAP.get(
            Locale.FRANCE, mockMessageFormatAdapterProvider);

    MessageFormatAdapter call1 = messageFormatLoadingCache.get("test");
    assertThat(call1.format(Maps.of())).isEqualTo("test");

    MessageFormatAdapter call2 = messageFormatLoadingCache.get("test");
    assertThat(call2.format(Maps.of())).isEqualTo("test");

    verify(mockMessageFormatAdapterProvider).get("test", Locale.FRANCE);
  }
}
