package com.pinterest.l10nmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

class PropertiesLoader {

  private PropertiesLoader() {
    throw new AssertionError();
  }

  /**
   * This returns all the entries in the properties file with potentially duplicated keys. Order is
   * preserved.
   *
   * <p>Assuming properties: <code>
   * k1=v1
   * k2=v2
   * k1=v1b
   * </code>
   *
   * <p>entries will be: [{k1: v1}, {k2: v2}, {k1: v1b}]
   *
   * @param inputStream
   * @return
   */
  static List<PropertiesEntry> getPropertiesEntries(InputStream inputStream) {
    PropertiesWithEntries propertiesWithEntries = new PropertiesWithEntries();
    load(propertiesWithEntries, inputStream);
    return propertiesWithEntries.getPropertiesEntries();
  }

  static void load(Properties properties, InputStream inputStream) {
    try {
      properties.load(
          new InputStreamReader(inputStream, new UTF8FallbackISO88591Charset().newDecoder()));
    } catch (IOException e) {
      throw new PropertiesLoaderException(e);
    }
  }

  public static final class PropertiesEntry {
    private final String key;
    private final String value;

    public PropertiesEntry(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PropertiesEntry that = (PropertiesEntry) o;
      return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(key, value);
    }
  }

  /** Extension of {@link Properties} that also stores key and values into a MultiMap. */
  private static class PropertiesWithEntries extends Properties {

    private final List<PropertiesEntry> propertiesEntries = new ArrayList<>();

    @Override
    public synchronized Object put(Object key, Object value) {
      propertiesEntries.add(new PropertiesEntry(key.toString(), value.toString()));
      return super.put(key, value);
    }

    public List<PropertiesEntry> getPropertiesEntries() {
      return propertiesEntries;
    }
  }
}
