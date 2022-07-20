package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.Reflection.getPublicStaticFiledOrNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReflectionTest {

  public static final String FIELD_FOR_TEST = "for test";

  @Test
  void isClassPresentTrue() {
    assertThat(Reflection.isClassPresent("java.lang.String")).isTrue();
  }

  @Test
  void isClassPresentFalse() {
    assertThat(Reflection.isClassPresent("SomeClassThatDoesNotExists")).isFalse();
  }

  @Test
  void getFieldOrNullExist() {
    assertEquals(
        FIELD_FOR_TEST, getPublicStaticFiledOrNull("FIELD_FOR_TEST", ReflectionTest.class));
  }

  @Test
  void getFieldOrNullMissing() {
    assertThat((String) getPublicStaticFiledOrNull("MISSING", ReflectionTest.class)).isNull();
  }
}
