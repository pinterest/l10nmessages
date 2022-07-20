package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.MessageFormatAdapterProviders.withFormatOrNoop;

import com.ibm.icu.text.MessageFormat;
import java.util.Locale;

class MessageFormatAdapterProviderICU implements MessageFormatAdapterProvider {
  @Override
  public MessageFormatAdapter get(String message, Locale locale) {
    return withFormatOrNoop(
        message, new MessageFormatAdapterICU(new MessageFormat(message, locale)));
  }
}
