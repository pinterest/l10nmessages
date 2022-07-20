package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MessageFormatAdapterProvidersTest {

  @Test
  void jdk() {
    Map<String, Object> args = Maps.of("0", "v0", "2", "v2");
    MessageFormatAdapter messageFormatAdapter =
        MessageFormatAdapterProviders.JDK.get("message jdk: {0} {1} {2}", Locale.FRANCE);

    assertThat(messageFormatAdapter).isInstanceOf(MessageFormatAdapterJDK.class);
    assertThat(messageFormatAdapter.format(args)).isEqualTo("message jdk: v0 {1} v2");
  }

  @Test
  void jdkNamedArgument() {
    Map<String, Object> args = Maps.of("0", "v0", "named", "v2");
    MessageFormatAdapter messageFormatAdapter =
        MessageFormatAdapterProviders.JDK_NAMED_ARGS.get(
            "message jdk: {0} {1} {named}", Locale.FRANCE);

    assertThat(messageFormatAdapter).isInstanceOf(MessageFormatAdapterJDKNamedArgs.class);
    assertThat(messageFormatAdapter.format(args)).isEqualTo("message jdk: v0 {1} v2");
  }

  @Test
  void icu() {
    Map<String, Object> args = Maps.of("0", "v0", "2", "v2");
    MessageFormatAdapter messageFormatAdapter =
        MessageFormatAdapterProviders.ICU.get("message icu: {0} {1} {2}", Locale.FRANCE);

    assertThat(messageFormatAdapter).isInstanceOf(MessageFormatAdapterICU.class);
    assertThat(messageFormatAdapter.format(args)).isEqualTo("message icu: v0 {1} v2");
  }

  @Test
  void noopForICU() {
    MessageFormatAdapter noop =
        MessageFormatAdapterProviders.ICU.get("simple message", Locale.FRANCE);
    assertThat(noop).isInstanceOf(MessageFormatAdapterNoop.class);
  }

  @Test
  void noopForJDK() {
    MessageFormatAdapter noop =
        MessageFormatAdapterProviders.JDK.get("simple message", Locale.FRANCE);
    assertThat(noop).isInstanceOf(MessageFormatAdapterNoop.class);
  }

  @Test
  void auto() {
    assertThat(MessageFormatAdapterProviders.AUTO.messageFormatAdapterProvider)
        .isSameAs(MessageFormatAdapterProviders.ICU.messageFormatAdapterProvider);
  }
}
