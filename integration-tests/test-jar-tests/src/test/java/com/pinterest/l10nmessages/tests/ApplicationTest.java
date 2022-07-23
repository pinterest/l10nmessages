package com.pinterest.l10nmessages.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void english() {
    assertThat(Application.english()).isEqualTo("Hello");
  }

  @Test
  void french() {
    assertThat(Application.french()).isEqualTo("Bonjour");
  }

}
