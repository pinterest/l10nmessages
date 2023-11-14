package com.pinterest.l10nmessages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaVersionsTest {

  @Test
  void version() {
    int versionAsInt = JavaVersions.specificationVersionAsInt();
    Assertions.assertThat(versionAsInt).isGreaterThanOrEqualTo(8);
  }
}
