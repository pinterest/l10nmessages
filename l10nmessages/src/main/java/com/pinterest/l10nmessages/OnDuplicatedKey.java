package com.pinterest.l10nmessages;

/** Strategy to apply when a duplicated key is found. */
public interface OnDuplicatedKey {

  void apply(String resourceName, String key, String value, String previousValue)
      throws OnDuplicatedKeyException;
}
