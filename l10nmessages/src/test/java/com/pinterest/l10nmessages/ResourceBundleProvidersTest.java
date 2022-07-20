package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
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
    if (ResourceBundleProviders.isJava8()) {
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
    if (ResourceBundleProviders.isJava8()) {
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
}
