package com.pinterest.l10nmessages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageFormatValidationTargetTest {

  @Test
  void coverage() {
    Assertions.assertThat(MessageFormatValidationTarget.values()).hasSize(2);
  }
}
