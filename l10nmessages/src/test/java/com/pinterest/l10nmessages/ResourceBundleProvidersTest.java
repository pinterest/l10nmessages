package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.JavaVersions.isJava8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceBundleProvidersTest {

  @BeforeEach
  void clearCache() {
    ResourceBundle.clearCache();
  }

  @Test
  void utf8NoBomFile() {
    ResourceBundle resourceBundle =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.Messages", Locale.ROOT);
    assertThat(resourceBundle.getString("iso")).isEqualTo("break because of accented character: à");
  }

  @Test
  void utf8BomFile() {
    ResourceBundle resourceBundle =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesBOM", Locale.ROOT);
    assertThat(resourceBundle.getString("iso")).isEqualTo("break because of accented character: à");
  }

  @Test
  void fallbackForISOFile() {
    ResourceBundle resourceBundle =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesISO", Locale.ROOT);
    assertThat(resourceBundle.getString("iso")).isEqualTo("break because of accented character: à");
  }

  @Test
  void utf8NoBomFileFailsForNative8() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("com.pinterest.l10nmessages.Messages", Locale.ROOT);
    if (isJava8()) {
      // 8 will read as iso and not fail to read but return the wrong result
      assertThat(resourceBundle.getString("iso"))
          .isEqualTo("break because of accented character: Ã ");
    } else {
      // 9+ will read as utf8 first and will get it right
      assertThat(resourceBundle.getString("iso"))
          .isEqualTo("break because of accented character: à");
    }
  }

  @Test
  void utf8BomFileFailsForNative8() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("com.pinterest.l10nmessages.MessagesBOM", Locale.ROOT);
    if (isJava8()) {
      // 8 will read as iso and not fail to read but return the wrong result
      assertThat(resourceBundle.getString("iso"))
          .isEqualTo("break because of accented character: Ã ");
    } else {
      // 9+ will read as utf8 first and will get it right
      assertThat(resourceBundle.getString("iso"))
          .isEqualTo("break because of accented character: à");
    }
  }

  @Test
  public void nativeFallbackForISOFile() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("com.pinterest.l10nmessages.MessagesISO", Locale.ROOT);
    assertThat(resourceBundle.getString("iso")).isEqualTo("break because of accented character: à");
  }

  @Test
  public void alternateLocaleBothIWAndHE() {
    // This bundle has both "iw" and "he" files
    // this shows how based on the version, "iw" or "he" file will be loaded, this regardless
    // of the locale code. Java 17 and up will load "he" while before it would load "iw"
    // This could be made stable ... but the complexity is not worth as we assume only one
    // file will be used in practice, or that they'll have the same content.
    if (JavaVersions.isJava17AndUp()) {
      ResourceBundle resourceBundle =
          ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
              "com.pinterest.l10nmessages.Messages", new Locale("iw"));
      assertThat(resourceBundle.getString("welcome")).isEqualTo("Welcome! - he");

      ResourceBundle resourceBundleHe =
          ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
              "com.pinterest.l10nmessages.Messages", new Locale("he"));
      assertThat(resourceBundleHe.getString("welcome")).isEqualTo("Welcome! - he");
    } else {
      ResourceBundle resourceBundle =
          ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
              "com.pinterest.l10nmessages.Messages", new Locale("iw"));
      assertThat(resourceBundle.getString("welcome")).isEqualTo("Welcome! - iw");

      ResourceBundle resourceBundleHe =
          ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
              "com.pinterest.l10nmessages.Messages", new Locale("he"));
      assertThat(resourceBundleHe.getString("welcome")).isEqualTo("Welcome! - iw");
    }
  }

  @Test
  public void alternateLocaleOnlyIW() {
    ResourceBundle resourceBundle =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesIW", new Locale("iw"));
    assertThat(resourceBundle.getString("welcome")).isEqualTo("Welcome! - iw");

    ResourceBundle resourceBundleHe =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesIW", new Locale("he"));
    assertThat(resourceBundleHe.getString("welcome")).isEqualTo("Welcome! - iw");
  }

  @Test
  public void alternateLocaleOnlyHE() {
    ResourceBundle resourceBundle =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesHE", new Locale("iw"));
    assertThat(resourceBundle.getString("welcome")).isEqualTo("Welcome! - he");

    ResourceBundle resourceBundleHe =
        ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
            "com.pinterest.l10nmessages.MessagesHE", new Locale("he"));
    assertThat(resourceBundleHe.getString("welcome")).isEqualTo("Welcome! - he");
  }

  @Test
  public void missingBundle() {
    assertThatThrownBy(
            () ->
                ResourceBundleProviders.DEFAULT_OR_BACKPORT.get(
                    "a.missing.bundle.Messages", Locale.FRENCH))
        .isInstanceOf(MissingResourceException.class)
        .hasMessageContaining("Can't find bundle for base name a.missing.bundle.Messages");
  }
}
