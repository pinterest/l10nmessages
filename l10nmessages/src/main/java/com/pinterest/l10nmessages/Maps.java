package com.pinterest.l10nmessages;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

class Maps {

  public static <K, V> Map<K, V> of() {
    return Collections.emptyMap();
  }

  public static <K, V> Map<K, V> of(K k1, V v1) {
    LinkedHashMap<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    return map;
  }

  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
    LinkedHashMap<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    LinkedHashMap<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    return map;
  }

  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    LinkedHashMap<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    return map;
  }

  public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    LinkedHashMap<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    return map;
  }
}
