package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import java.text.MessageFormat;
import org.junit.jupiter.api.Test;

class MessageFormatAdapterJDKTest {

  @Test
  void format() {
    MessageFormatAdapterJDK messageFormatAdapterJDK =
        new MessageFormatAdapterJDK(new MessageFormat("{0} {1}"));
    assertThat(messageFormatAdapterJDK.format(Maps.of("0", "a", "1", "b"))).isEqualTo("a b");
  }

  @Test
  void formatNested() {
    MessageFormatAdapterJDK messageFormatAdapterJDK =
        new MessageFormatAdapterJDK(new MessageFormat("{0,choice,0#test0{1}|1#test1{1}}"));
    assertThat(messageFormatAdapterJDK.format(Maps.of("0", 0, "1", "b"))).isEqualTo("test0b");
    assertThat(messageFormatAdapterJDK.format(Maps.of("0", 1, "1", "b"))).isEqualTo("test1b");
  }

  @Test
  void getArgumentNamesSkipped() {
    MessageFormatAdapterJDK messageFormatAdapterJDK =
        new MessageFormatAdapterJDK(
            new MessageFormat("{0} {2,number} {4,date} {5, time} {6,choice,0#test0|1#test1}}"));
    assertThat(messageFormatAdapterJDK.getArgumentNames()).containsExactly("0", "2", "4", "5", "6");
  }

  @Test
  void getArgumentNamesNestedNo() {
    assertThat(
            MessageFormatAdapterJDK.getArgumentNames(
                new MessageFormat("{0} {1,choice,0#test0{2}|1#test1{2}}}")))
        .containsExactly("0", "1");
  }

  @Test
  void getArgumentNamesNestedYes() {
    assertEquals(
        Sets.newHashSet("0", "1", "2"),
        MessageFormatAdapterJDK.getArgumentNames(
            new MessageFormat("{0} {2,choice,0#test0{1}|1#test1{1}}}")));
  }
}
