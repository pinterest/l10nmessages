package com.pinterest.l10nmessages;

import java.util.Locale;

/** Function that provides a {@link MessageFormatAdapter} given a message and a locale. */
@FunctionalInterface
public interface MessageFormatAdapterProvider {

  MessageFormatAdapter get(String message, Locale locale);
}
