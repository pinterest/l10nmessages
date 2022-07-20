package com.pinterest.l10nmessages.example;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void youHaveMessagesEnglish() {
    assertIterableEquals(
        Arrays.asList(
            "You have 0 messages",
            "You have 1 message",
            "You have 2 messages"),
        Application.youHaveMessagesEnglish());
  }

  @Test
  void youHaveMessagesFrench() {
    assertIterableEquals(
        Arrays.asList(
            "Vous avez 0 message",
            "Vous avez 1 message",
            "Vous avez 2 messages"),
        Application.youHaveMessagesFrench());
  }

  @Test
  void youHaveMessagesJapanese() {
    assertIterableEquals(
        Arrays.asList(
            "メッセージが 0 件",
            "メッセージが 1 件",
            "メッセージが 2 件"),
        Application.youHaveMessagesJapanese());
  }

  @Test
  void youHaveMessagesRussian() {
    assertIterableEquals(
        Arrays.asList(
            "У вас есть 0 сообщений", "У вас есть 1 сообщение", "У вас есть 2 сообщения",
            "У вас есть 5 сообщений", "У вас есть 21 сообщение", "У вас есть 22 сообщения",
            "У вас есть 25 сообщений"),
        Application.youHaveMessagesRussian());
  }
}