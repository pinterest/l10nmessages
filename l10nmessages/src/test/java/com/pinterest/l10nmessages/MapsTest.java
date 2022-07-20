package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

class MapsTest {

  @Test
  void testOf() {
    assertThat(Maps.of()).isEqualTo(new LinkedHashMap<>());
  }

  @Test
  void testOf1() {
    assertThat(Maps.of(1, 2)).containsExactly(entry(1, 2));
  }

  @Test
  void testOf2() {
    assertThat(Maps.of(1, 2, 3, 4)).containsExactly(entry(1, 2), entry(3, 4));
  }

  @Test
  void testOf3() {
    assertThat(Maps.of(1, 2, 3, 4, 5, 6)).containsExactly(entry(1, 2), entry(3, 4), entry(5, 6));
  }

  @Test
  void testOf4() {
    assertThat(Maps.of(1, 2, 3, 4, 5, 6, 7, 8))
        .containsExactly(entry(1, 2), entry(3, 4), entry(5, 6), entry(7, 8));
  }

  @Test
  void testOf5() {
    assertThat(Maps.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        .containsExactly(entry(1, 2), entry(3, 4), entry(5, 6), entry(7, 8), entry(9, 10));
  }
}
