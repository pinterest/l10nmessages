package com.pinterest.l10nmessages;

import java.util.Map;

/** Provides the key and the arguments needed to format a message */
public interface FormatContext<T> {

  T getKey();

  Map<String, Object> getArguments();
}
