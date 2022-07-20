package com.pinterest.l10nmessages.example;

import static com.pinterest.l10nmessages.example.Application.welcomeUserInEnglish;
import static com.pinterest.l10nmessages.example.Application.welcomeUserInFrench;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void welcomeUserEnglish() {
    assertEquals("Welcome Mary!", welcomeUserInEnglish());
  }

  @Test
  void welcomeUserFrench() {
    assertEquals("Bienvenue Mary!", welcomeUserInFrench());
  }
}