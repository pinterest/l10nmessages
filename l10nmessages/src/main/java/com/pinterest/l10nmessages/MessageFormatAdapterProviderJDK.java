package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.MessageFormatAdapterProviders.withFormatOrNoop;

import java.text.MessageFormat;
import java.util.Locale;

public class MessageFormatAdapterProviderJDK implements MessageFormatAdapterProvider {
  @Override
  public MessageFormatAdapter get(String message, Locale locale) {
    return withFormatOrNoop(
        message, new MessageFormatAdapterJDK(new MessageFormat(message, locale)));
  }
}
