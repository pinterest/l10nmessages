package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.JavaVersions.isJava17AndUp;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class AlternateLanguageCodesTest {

  @Test
  void alternateForHebrew_iw() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("iw", "Messages_iw.properties"))
        .isEqualTo("Messages_he.properties");
  }

  @Test
  void alternateForHebrew_he() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("he", "Messages_he.properties"))
        .isEqualTo("Messages_iw.properties");
  }

  @Test
  void alternateForHebrew_iw_IL() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("iw", "Messages_iw_IL.properties"))
        .isEqualTo("Messages_he_IL.properties");
  }

  @Test
  void alternateForHebrew_in() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("in", "Messages_in.properties"))
        .isEqualTo("Messages_id.properties");
  }

  @Test
  void alternateForHebrew_id() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("id", "Messages_id.properties"))
        .isEqualTo("Messages_in.properties");
  }

  @Test
  void alternateForHebrew_ji() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("ji", "Messages_ji.properties"))
        .isEqualTo("Messages_yi.properties");
  }

  @Test
  void alternateForHebrew_yi() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("yi", "Messages_yi.properties"))
        .isEqualTo("Messages_ji.properties");
  }

  @Test
  void basedOnJavaVersion() {
    if (isJava17AndUp()) {
      System.out.println(new Locale("he"));
      assertThat(
              AlternateLanguageCodes.getAlternateResourceName(
                  new Locale("he"), "Messages_he.properties"))
          .isEqualTo("Messages_iw.properties");
    } else {
      assertThat(
              AlternateLanguageCodes.getAlternateResourceName(
                  new Locale("iw"), "Messages_iw.properties"))
          .isEqualTo("Messages_he.properties");
    }
  }

  @Test
  void doNothingForRoot() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("iw", "Messages.properties"))
        .isEqualTo("Messages.properties");
  }

  @Test
  void doNothingForOtherLocale() {
    assertThat(AlternateLanguageCodes.getAlternateResourceName("iw", "Messages_fr.properties"))
        .isEqualTo("Messages_fr.properties");
  }
}
