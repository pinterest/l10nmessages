package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OnDuplicatedKeysTest {

  @Test
  void accept() {
    assertThatCode(() -> OnDuplicatedKeys.ACCEPT.apply("test", "key", "value", "previousValue"))
        .doesNotThrowAnyException();
  }

  @Test
  void acceptIfSameSame() {
    assertThatCode(() -> OnDuplicatedKeys.ACCEPT_IF_SAME.apply("test", "key", "value", "value"))
        .doesNotThrowAnyException();
  }

  @Test
  void acceptIfSameReject() {
    assertThatThrownBy(
            () -> OnDuplicatedKeys.ACCEPT_IF_SAME.apply("test", "key", "value", "previousValue"))
        .isInstanceOf(OnDuplicatedKeyException.class)
        .hasMessage("Duplicated key 'key'. Fix 'test'. Alternatively, use OnDuplicatedKeys#ACCEPT");
  }

  @Test
  void reject() {
    assertThatThrownBy(
            () -> {
              OnDuplicatedKeys.REJECT.apply("test", "key", "value", "previousValue");
              OnDuplicatedKeys.REJECT.apply("test", "key", "value", "value");
            })
        .isInstanceOf(OnDuplicatedKeyException.class)
        .hasMessage(
            "Duplicated key 'key'. Fix 'test'. Alternatively, use OnDuplicatedKeys#ACCEPT or OnDuplicatedKeys#ACCEPT_IF_SAME");
  }
}
