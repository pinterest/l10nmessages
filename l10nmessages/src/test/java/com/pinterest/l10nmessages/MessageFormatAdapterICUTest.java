package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.MessagePattern;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageFormatAdapterICUTest {

  @Test
  void format() {
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(new MessageFormat("{0} {1}"));
    assertThat(messageFormatAdapterICU.format(Maps.of("0", "a", "1", "b"))).isEqualTo("a b");
  }

  @Test
  void formatChoice() {
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(new MessageFormat("{0,choice,0#test0{1}|1#test1{1}}"));
    assertThat(messageFormatAdapterICU.format(Maps.of("0", 0, "1", "b"))).isEqualTo("test0b");
    assertThat(messageFormatAdapterICU.format(Maps.of("0", 1, "1", "b"))).isEqualTo("test1b");
  }

  @Test
  void formatComplex() {
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(
            new MessageFormat(
                "{n, number, integer}, {p, plural, other {{pnested}, {c, select, val {{valnested}} other {other}}}}"));
    assertThat(
            messageFormatAdapterICU.format(
                Maps.of(
                    "n",
                    10.5,
                    "p",
                    3,
                    "pnested",
                    "nested in plural",
                    "c",
                    "val",
                    "valnested",
                    "nested in val")))
        .isEqualTo("10, nested in plural, nested in val");
  }

  @Test
  void getArgumentNamesNumbered() {
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(
            new MessageFormat(
                "{0} {1} {3} {4,date} {5, time} {6,choice,0#test0{1}|1#test1{1}}} {7,number}"));
    assertThat(messageFormatAdapterICU.getArgumentNames())
        .containsExactly("0", "1", "3", "4", "5", "6", "7");
  }

  @Test
  void getArgumentNamesNamed() {
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(
            new MessageFormat("{number} {3} {dt,date} {t, time} {count,plural,other{test}}"));
    assertThat(messageFormatAdapterICU.getArgumentNames())
        .containsExactlyInAnyOrder("number", "3", "dt", "t", "count");
  }

  @Test
  void getArgumentNamesComplex() {
    String pattern =
        "{n, number, integer}, {p, plural, other {{pnested}, {c, select, val {{valnested}} other {other}}}}";
    MessageFormatAdapterICU messageFormatAdapterICU =
        new MessageFormatAdapterICU(new MessageFormat(pattern));
    assertThat(messageFormatAdapterICU.getArgumentNames())
        .containsExactly("n", "p", "pnested", "c", "valnested");
  }

  @Test
  void getMessagePatternOfMessageFormatValidMessageFormat() {
    MessagePattern messagePattern =
        MessageFormatAdapterICU.getMessagePatternOfMessageFormat(
            new MessageFormat("Hi {username}!"));
    assertThat(messagePattern.getPatternString()).isEqualTo("Hi {username}!");
  }

  @Test
  void getMessagePatternOfMessageFormatReflectionWorks() {
    final MessagePattern forTest = new MessagePattern("for testing reflection");
    MessageFormat messageFormat = new MessageFormat("this should not be used");
    overrideMessagePattern(messageFormat, forTest);
    MessagePattern messagePattern =
        MessageFormatAdapterICU.getMessagePatternOfMessageFormat(messageFormat);
    assertThat(messagePattern).isSameAs(forTest);
  }

  @Test
  void getMessagePatternOfMessageFormatReflectionFails() {
    MessageFormat mfMock = Mockito.mock(MessageFormat.class);
    Mockito.when(mfMock.toPattern()).thenReturn("Hi {username}!");
    MessagePattern messagePattern =
        MessageFormatAdapterICU.getMessagePatternOfMessageFormat(mfMock);
    assertThat(messagePattern.getPatternString()).isEqualTo("Hi {username}!");
  }

  @Test
  void getMessagePatternOfMessageFormatReflectionFailsAndFallbackFails() {
    MessageFormat mfMock = Mockito.mock(MessageFormat.class);
    Mockito.when(mfMock.toPattern()).thenReturn("invalid to break MessagePattern {num");
    assertThatThrownBy(() -> MessageFormatAdapterICU.getMessagePatternOfMessageFormat(mfMock))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unmatched '{' braces in message \"invalid to break Mes ...\"");
  }

  @Test
  void getArgumentNamesFromMessageFormat() {
    assertThat(
            MessageFormatAdapterICU.getArgumentNamesFromMessageFormat(
                new MessageFormat(
                    "This '{isn''t}' obvious: {toplevel} {count, plural, other { {nested} }}")))
        .containsExactly("toplevel", "count", "nested");
  }

  @Test
  void getArgumentNamesFromMessageFormatFallbackToTopLevel() {
    MessageFormat mfMock = Mockito.mock(MessageFormat.class);
    Mockito.when(mfMock.toPattern()).thenReturn("invalid to break MessagePattern {num");
    Mockito.when(mfMock.getArgumentNames())
        .thenReturn(new HashSet<>(Arrays.asList("toplevel", "count")));

    assertThat(MessageFormatAdapterICU.getArgumentNamesFromMessageFormat(mfMock))
        .containsExactly("toplevel", "count");
  }

  void overrideMessagePattern(MessageFormat messageFormat, MessagePattern messagePattern) {
    try {
      Field msgPatternField = messageFormat.getClass().getDeclaredField("msgPattern");
      msgPatternField.setAccessible(true);
      msgPatternField.set(messageFormat, messagePattern);
    } catch (Throwable t) {
      fail(t);
    }
  }
}
