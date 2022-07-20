package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MessageFormatAdapterNoopTest {
  @Test
  void argument() {
    assertThat(new MessageFormatAdapterNoop("message").getArgumentNames()).isEmpty();
  }
}
