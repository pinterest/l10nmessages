package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UTF8FallbackISO88591CharsetTest {

  @Test
  void fallback() throws CharacterCodingException {
    byte[] bytes = {(byte) 0b1010_0001};
    CharBuffer decode =
        new UTF8FallbackISO88591Charset().newDecoder().decode(ByteBuffer.wrap(bytes));
    assertThat(decode.toString()).isEqualTo("¡");
  }

  @Test
  void isoValid() throws CharacterCodingException {
    byte[] bytes = {(byte) 0b1010_0001};
    assertThat(StandardCharsets.ISO_8859_1.newDecoder().decode(ByteBuffer.wrap(bytes)).toString())
        .isEqualTo("¡");
  }

  @Test
  void reportMalformedInputUTF8IfNoFallback() {
    byte[] bytes = {(byte) 0b1010_0001};
    assertThatThrownBy(() -> StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(bytes)))
        .isInstanceOf(MalformedInputException.class);
  }

  @Test
  void containsAlwaysFalse() {
    assertThat(new UTF8FallbackISO88591Charset().contains(Mockito.mock(Charset.class))).isFalse();
  }

  @Test
  void newEncoderIsUTF8() {
    assertThat(new UTF8FallbackISO88591Charset().newEncoder().charset())
        .isEqualTo(StandardCharsets.UTF_8);
  }

  /**
   * Some sequence can be valid in both UTF-8 and ISO 8859-1. This could lead to content to be
   * loaded as UTF-8 instead of ISO 8859-1. The likeliness of this occurring is low, and given the
   * JDK adopted the fallback logic as part of the default implementation for Java 9+, the library
   * will just replicate that behavior.
   *
   * <p>This test is for the record.
   */
  @Test
  void isoAndUTF8Valid() throws CharacterCodingException {
    byte[] bytes = {(byte) 194, (byte) 162};
    assertThat(StandardCharsets.ISO_8859_1.newDecoder().decode(ByteBuffer.wrap(bytes)).toString())
        .isEqualTo("Â¢");
    assertThat(StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(bytes)).toString())
        .isEqualTo("¢");
    assertThat(
            new UTF8FallbackISO88591Charset()
                .newDecoder()
                .decode(ByteBuffer.wrap(bytes))
                .toString())
        .isEqualTo("¢");
  }
}
