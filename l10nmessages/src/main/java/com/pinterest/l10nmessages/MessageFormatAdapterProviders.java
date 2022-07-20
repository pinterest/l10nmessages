package com.pinterest.l10nmessages;

import java.util.Locale;

public enum MessageFormatAdapterProviders implements MessageFormatAdapterProvider {
  ICU(new MessageFormatAdapterProviderICU()),
  JDK(new MessageFormatAdapterProviderJDK()),
  JDK_NAMED_ARGS(new MessageFormatAdapterProviderJDKNamedArgs()),
  AUTO(
      Reflection.isClassPresent("com.ibm.icu.text.MessageFormat")
          ? ICU.messageFormatAdapterProvider
          : JDK_NAMED_ARGS.messageFormatAdapterProvider);

  final MessageFormatAdapterProvider messageFormatAdapterProvider;

  MessageFormatAdapterProviders(MessageFormatAdapterProvider messageFormatAdapterProvider) {
    this.messageFormatAdapterProvider = messageFormatAdapterProvider;
  }

  @Override
  public MessageFormatAdapter get(String message, Locale locale) {
    return messageFormatAdapterProvider.get(message, locale);
  }

  static MessageFormatAdapter withFormatOrNoop(
      String message, MessageFormatAdapter messageFormatAdapter) {
    return messageFormatAdapter.getArgumentNames().size() == 0
        ? new MessageFormatAdapterNoop(message)
        : messageFormatAdapter;
  }
}
