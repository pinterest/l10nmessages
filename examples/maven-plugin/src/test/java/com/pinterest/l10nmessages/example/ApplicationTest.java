package com.pinterest.l10nmessages.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.OnFormatErrorException;
import com.pinterest.l10nmessages.example.jar.Messages;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @BeforeAll
  public static void beforeAll() {
    Locale.setDefault(Locale.US);
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
  }

  @Test
  void withGeneratedEnum() {
    assertEquals("Welcome!", Application.withGeneratedEnum());
  }

  @Test
  void withoutGeneratedEnum() {
    assertEquals("Welcome!", Application.withoutGeneratedEnum());
  }

  @Test
  void withManualEnum() {
    assertEquals("Welcome!", Application.withManualEnum());
  }

  @Test
  void welcomeInFrench() {
    assertEquals("Bienvenue!", Application.welcomeInFrench());
  }

  @Test
  void formatWithMapEntries() {
    assertEquals("Welcome Bob!", Application.formatWithMapEntries());
  }

  @Test
  void formatWithMap() {
    assertEquals("Welcome Bob!", Application.formatWithMap());
  }

  @Test
  void formatNumberedArgument() {
    assertEquals("Welcome Bob!", Application.formatNumberedArgument());
  }

  @Test
  void formatUntyped() {
    assertEquals("Welcome Bob!", Application.formatUntyped());
  }

  @Test
  void formatWithArgumentBuilder() {
    assertEquals("Welcome Bob!", Application.formatWithArgumentBuilder());
  }

  @Test
  void withBaseArguments() {
    assertIterableEquals(
        Arrays.asList("Welcome Mary!", "Bye Mary...", "Bye Bob..."),
        Application.withBaseArguments());
  }

  @Test
  void onFormatErrorRethrow() {
    OnFormatErrorException onFormatErrorException =
        assertThrows(OnFormatErrorException.class, () -> Application.onFormatErrorReThrow());
    assertEquals(
        "java.lang.IllegalArgumentException: can't parse argument number: username",
        onFormatErrorException.getMessage());
  }

  @Test
  void onFormatErrorMessageFallback() {
    assertEquals("Welcome {username}!", Application.onFormatErrorMessageFallback());
  }

  @Test
  void onMissingArgumentLog() {
    assertEquals("Welcome {username}!", Application.onMissingArgumentLog());
  }

  @Test
  void onMissingArgumentSubstitute() {
    assertEquals("Welcome John Doe!", Application.onMissingArgumentSubstitute());
  }

  @Test
  void customizedMessageFormatAdapterProvider() {
    assertEquals("Welcome Mary!", Application.customizedMessageFormatAdapterProvider());
  }

  @Test
  void caching() {
    assertEquals(
        Arrays.asList(
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Bye Bob!",
            "Welcome Bob!",
            "Bye Bob!",
            "Welcome Bob!",
            "Bye Bob!",
            "Welcome Bob!",
            "Bye Bob!",
            "Welcome Bob!",
            "Bye Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Welcome Bob!",
            "Bye Bob...",
            "Welcome Bob!",
            "Bye Bob...",
            "Welcome Bob!",
            "Bye Bob...",
            "Welcome Bob!",
            "Bye Bob...",
            "Welcome Bob!",
            "Bye Bob...",
            "Welcome Mary!",
            "Welcome Mary!"),
        Application.caching());
  }

  @Test
  void icu4jNumber() {
    assertEquals(
        Arrays.asList(
            "Examples: 1,000.1, 1,000, 50%, $50.00", "Exemples: 1 000,1, 1 000, 50 %, 50,00 €"),
        Application.icu4jNumber());
  }

  @Test
  void icu4jDate() {
    assertEquals(
        Arrays.asList(
            "Today is: Jun 22, 2022, it is: 8:12:34 PM",
            "Today is: 6/22/22, it is: 8:12 PM",
            "Aujourd'hui c'est le: 22 juin 2022, il est: 20:12:34",
            "Aujourd'hui c'est le: 22/06/2022, il est: 20:12"),
        Application.icu4jDate());
  }

  @Test
  void icu4jSkeleton() {
    assertEquals(
        Arrays.asList("CA$1,000.12 - June 22, 8:12 PM", "1 000,12 $CA - 22 juin, 20:12"),
        Application.icu4jSkeleton());
  }

  @Test
  void icu4jPluralForm() {
    assertIterableEquals(
        Arrays.asList(
            // en
            "You have 0 messages",
            "You have 1 message",
            "You have 2 messages",
            // fr
            "Vous avez 0 message",
            "Vous avez 1 message",
            "Vous avez 2 messages",
            // ja
            "メッセージが 0 件",
            "メッセージが 1 件",
            "メッセージが 2 件",
            // ru
            "У вас есть 0 сообщений",
            "У вас есть 1 сообщение",
            "У вас есть 2 сообщения",
            "У вас есть 5 сообщений",
            "У вас есть 21 сообщение",
            "У вас есть 22 сообщения",
            "У вас есть 25 сообщений"),
        Application.icu4jPluralForm());
  }

  @Test
  void icu4jPluralFormWith0() {
    assertIterableEquals(
        Arrays.asList(
            // en
            "You have no messages",
            "You have 1 message",
            "You have 2 messages",
            // fr
            "Vous n'avez aucun message",
            "Vous avez 1 message",
            "Vous avez 2 messages"),
        Application.icu4jPluralFormWith0());
  }

  @Test
  void icu4jGender() {
    assertIterableEquals(
        Arrays.asList(
            "You are invited",
            "You are invited",
            "You are invited",
            "Vous êtes invitée",
            "Vous êtes invité",
            "Vous êtes invité"),
        Application.icu4jGender());
  }

  @Test
  void icu4JComplex() {
    assertIterableEquals(
        Arrays.asList(
            // en, female
            "She has no favorite numbers",
            "Her favorite number is 1",
            "Her favorite numbers are 3 and 7",
            // en, male
            "He has no favorite numbers",
            "His favorite number is 1",
            "His favorite numbers are 3 and 7",
            // en, other
            "They have no favorite numbers",
            "Their favorite number is 1",
            "Their favorite numbers are 3 and 7",
            // fr, female
            "Elle n'a pas de nombre préferé",
            "Son nombre préferé est 1",
            "Ses nombres préferés sont 3 et 7",
            // fr, male
            "Il n'a pas de nombre préferé",
            "Son nombre préferé est 1",
            "Ses nombres préferés sont 3 et 7",
            // fr, other
            "Il/Elle n'a pas de nombre préferé",
            "Son nombre préferé est 1",
            "Ses nombres préferés sont 3 et 7"),
        Application.icu4JComplex());
  }

  @Test
  void propertiesFromJar() {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    assertEquals("Welcome Mary!", Application.propertiesFromJar());
  }
}
