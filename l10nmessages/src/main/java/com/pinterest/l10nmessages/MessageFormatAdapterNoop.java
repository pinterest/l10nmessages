package com.pinterest.l10nmessages;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

class MessageFormatAdapterNoop implements MessageFormatAdapter {

  final String message;

  public MessageFormatAdapterNoop(String message) {
    this.message = message;
  }

  @Override
  public String format(Map<String, Object> arguments) {
    return message;
  }

  @Override
  public Set<String> getArgumentNames() {
    return Collections.emptySet();
  }
}
