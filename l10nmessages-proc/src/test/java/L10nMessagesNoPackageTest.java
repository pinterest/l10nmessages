import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import java.util.Locale;
import org.junit.jupiter.api.Test;

@L10nProperties(baseName = "MessagesNoPackage")
public class L10nMessagesNoPackageTest {

  @Test
  public void buildForClassNoPackage() {
    L10nMessages<MessagesNoPackage> m = L10nMessages.builder(MessagesNoPackage.class).build();
    assertEquals("No package", m.format(MessagesNoPackage.no_package, ImmutableMap.of()));
  }

  @Test
  public void buildForClassNoPackageJapanese() {
    L10nMessages<MessagesNoPackage> m =
        L10nMessages.builder(MessagesNoPackage.class).locale(Locale.forLanguageTag("ja")).build();
    assertEquals("No package - ja", m.format(MessagesNoPackage.no_package, ImmutableMap.of()));
  }
}
