package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.*;

import com.pinterest.l10nmessages.MessageFormatAdapterProviderJDKNamedArgs.MessageWithNumberedArgs;
import org.junit.jupiter.api.Test;

class MessageFormatAdapterProviderJDKNamedArgsTest {

  @Test
  void noArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("Hi!");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("Hi!");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames()).isEmpty();
  }

  @Test
  void argEndToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("Hi {username}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("Hi {0}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "username"));
  }

  @Test
  void argStartToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("{username}!");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("{0}!");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "username"));
  }

  @Test
  void singleArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("Hi {username}!");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("Hi {0}!");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "username"));
  }

  @Test
  void multiArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("Hi {username} and {username2}!");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("Hi {0} and {1}!");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "username"), entry(1, "username2"));
  }

  @Test
  void multiDuplicatedArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("Hi {username} and {username}!");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("Hi {0} and {0}!");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "username"));
  }

  @Test
  void commaArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("It is {today,date}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("It is {0,date}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "today"));
  }

  @Test
  void escapedCurlyArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("It '{ is {today,date}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("It '{ is {0,date}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "today"));
  }

  @Test
  void escapedUnMatchedCurlyArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("'{today}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("'{today}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames()).isEmpty();
  }

  @Test
  void nonAlphabeticCurlyArgToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("It '{ is {tod.ay,date}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("It '{ is {tod.ay,date}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames()).isEmpty();
  }

  @Test
  void numberedToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs().toMessageWithNumberedArgs("{0} {1} {2}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("{0} {1} {2}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "0"), entry(1, "1"), entry(2, "2"));
  }

  @Test
  void trimToMessageWithnumberedArgs() {
    MessageWithNumberedArgs messageWithNumberedArgs =
        new MessageFormatAdapterProviderJDKNamedArgs()
            .toMessageWithNumberedArgs("{trim} { trim} {trim2}");
    assertThat(messageWithNumberedArgs.getMessage()).isEqualTo("{0} {0} {1}");
    assertThat(messageWithNumberedArgs.getIndexToArgumentNames())
        .containsExactly(entry(0, "trim"), entry(1, "trim2"));
  }
}
