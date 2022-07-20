package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.ToJavaIdentifiers.ESCAPING_ONLY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class ToJavaIdentifierTest {

  private void escapingAndUnderscore(String expected, String key) {
    assertThat(ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE.convert(key)).isEqualTo(expected);
  }

  @Test
  void escapingAndUnderscore() {
    escapingAndUnderscore("", "");
    escapingAndUnderscore("abc", "abc");
    escapingAndUnderscore("_1abc", "1abc");
    escapingAndUnderscore("abc", "abc");
    escapingAndUnderscore("a_u32_b", "a b");
    escapingAndUnderscore("a_b", "a.b");
    escapingAndUnderscore("a_b", "a_b");
    escapingAndUnderscore("a_u128512_d", "a\uD83D\uDE00d");
    escapingAndUnderscore("テスト", "テスト");
    escapingAndUnderscore("試験", "試験");
    escapingAndUnderscore("_public", "public");
  }

  private void escapingOnly(String expected, String key) {
    assertThat(ESCAPING_ONLY.convert(key)).isEqualTo(expected);
  }

  @Test
  void escapingOnly() {
    escapingOnly("", "");
    escapingOnly("abc", "abc");
    escapingOnly("1abc", "1abc");
    escapingOnly("abc", "abc");
    escapingOnly("a_u32_b", "a b");
    escapingOnly("a_u46_b", "a.b");
    escapingOnly("a_b", "a_b");
    escapingOnly("a_u128512_d", "a\uD83D\uDE00d");
    escapingOnly("テスト", "テスト");
    escapingOnly("試験", "試験");
    escapingOnly("_public", "public");
  }
}
