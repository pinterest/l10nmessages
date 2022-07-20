package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OnMissingArgumentsTest {

  @Test
  public void noop() {
    assertThat(OnMissingArguments.NOOP.apply("basename", "key", "arg")).isEmpty();
  }

  @Test
  public void _throw() {
    assertThatThrownBy(() -> OnMissingArguments.THROW.apply("basename", "key", "arg"))
        .isInstanceOf(OnMissingArgumentException.class)
        .hasMessage("Argument: `arg` missing for baseName: `basename` and key: `key`");
  }
}
