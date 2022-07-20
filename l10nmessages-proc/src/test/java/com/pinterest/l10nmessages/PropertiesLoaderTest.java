package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pinterest.l10nmessages.PropertiesLoader.PropertiesEntry;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PropertiesLoaderTest {

  static final String DUPLICATES_PROPERTIES =
      "/com/pinterest/l10nmessages/PropertiesLoaderTest_IO/duplicates.properties";

  @Test
  void loadInJdkPropertiesUtf8() {
    Properties properties =
        loadResourceInJdkProperties(
            "/com/pinterest/l10nmessages/PropertiesLoaderTest_IO/utf8.properties");
    assertThat(properties.getProperty("utf8")).isEqualTo("with emoji \uD83D\uDE00");
    assertThat(properties.getProperty("\uD83D\uDE00key")).isEqualTo("a utf8 key");
  }

  @Test
  void loadInJdkPropertiesISO() {
    Properties properties =
        loadResourceInJdkProperties(
            "/com/pinterest/l10nmessages/PropertiesLoaderTest_IO/iso.properties");
    assertThat(properties.getProperty("iso")).isEqualTo("break because of accented character: à");
  }

  @Test
  void loadInJdkPropertiesWithDuplicates() {
    Properties properties = loadResourceInJdkProperties(DUPLICATES_PROPERTIES);

    // Java Properties doesn't maintain order so we sort
    List<String> keys =
        properties.keySet().stream().map(Object::toString).sorted().collect(Collectors.toList());

    assertThat(keys).isEqualTo(Arrays.asList("a", "a.1", "a.2", "a_1", "a_2", "b", "c", "d"));

    // Java Properties keep last element when there are duplicates. Implementation extends
    // Hashtable + put() as it reads the each value hence keeps the last value.
    List<String> values =
        keys.stream().map(properties::get).map(Object::toString).collect(Collectors.toList());
    assertEquals(
        Arrays.asList(
            "no conflict",
            "different key: a.1, same enum value: a_1",
            "different key: a.2, same enum value: a_2",
            "different key: a_1, same enum value: a_1",
            "different key: a_2, same enum value: a_2",
            "same key: b, same content",
            "same key: c, same content",
            "same key: d, different content: 2"),
        values);
  }

  @Test
  void getPropertiesEntriesWithDuplicatesPreserved() {
    List<PropertiesEntry> propertiesEntries = getPropertiesEntries(DUPLICATES_PROPERTIES);

    List<PropertiesEntry> expected = new ArrayList<>();
    expected.add(new PropertiesEntry("a", "no conflict"));
    expected.add(new PropertiesEntry("b", "same key: b, same content"));
    expected.add(new PropertiesEntry("b", "same key: b, same content"));
    expected.add(new PropertiesEntry("c", "same key: c, same content"));
    expected.add(new PropertiesEntry("c", "same key: c, same content"));
    expected.add(new PropertiesEntry("d", "same key: d, different content: 1"));
    expected.add(new PropertiesEntry("d", "same key: d, different content: 2"));
    expected.add(new PropertiesEntry("a_1", "different key: a_1, same enum value: a_1"));
    expected.add(new PropertiesEntry("a.1", "different key: a.1, same enum value: a_1"));
    expected.add(new PropertiesEntry("a_2", "different key: a_2, same enum value: a_2"));
    expected.add(new PropertiesEntry("a.2", "different key: a.2, same enum value: a_2"));

    Assertions.assertThat(propertiesEntries).isEqualTo(expected);
  }

  @Test
  void throwPropertiesLoaderException() {
    assertThatThrownBy(
            () -> PropertiesLoader.load(new Properties(), Mockito.mock(InputStream.class)))
        .isInstanceOf(PropertiesLoaderException.class);
  }

  static List<PropertiesEntry> getPropertiesEntries(String resourceName) {
    return PropertiesLoader.getPropertiesEntries(getInputStreamFromClassLoader(resourceName));
  }

  static Properties loadResourceInJdkProperties(String resourceName) {
    Properties properties = new Properties();
    PropertiesLoader.load(properties, getInputStreamFromClassLoader(resourceName));
    return properties;
  }

  static InputStream getInputStreamFromClassLoader(String resourceName) {
    InputStream is = PropertiesLoader.class.getResourceAsStream(resourceName);
    if (is == null) {
      throw new RuntimeException("No resource in test for name: " + resourceName);
    }
    return is;
  }
}
