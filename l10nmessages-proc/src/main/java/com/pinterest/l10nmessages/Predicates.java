package com.pinterest.l10nmessages;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

class Predicates {

  private Predicates() {
    throw new AssertionError();
  }

  static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyMapper) {
    Map<Object, Boolean> previousValues = new ConcurrentHashMap<>();
    return t -> previousValues.putIfAbsent(keyMapper.apply(t), Boolean.TRUE) == null;
  }

  static <T, K, V> Predicate<T> distinctByKey(
      Function<? super T, K> keyMapper,
      Function<? super T, V> valueMapper,
      OnDuplicate<K, V> onDuplicate) {
    Map<K, V> previousValues = new ConcurrentHashMap<>();
    return t -> {
      K key = keyMapper.apply(t);
      V value = valueMapper.apply(t);
      V previousValue = previousValues.putIfAbsent(key, value);
      if (previousValue != null) {
        onDuplicate.apply(key, value, previousValue);
      }
      return previousValue == null;
    };
  }

  @FunctionalInterface
  public interface OnDuplicate<K, V> {
    void apply(K key, V value, V previousValue);
  }
}
