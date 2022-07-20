package com.pinterest.l10nmessages;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An adapter for JDK message formatting that support named arguments.
 *
 * <p>The mapping index to argument names must be provided. See {@link
 * MessageFormatAdapterProviderJDKNamedArgs#toMessageWithNumberedArgs(String)}.
 */
class MessageFormatAdapterJDKNamedArgs implements MessageFormatAdapter {

  final MessageFormat messageFormat;
  final Map<Integer, String> indexToArgumentNames;

  public MessageFormatAdapterJDKNamedArgs(
      MessageFormat messageFormat, Map<Integer, String> indexToArgumentNames) {
    this.messageFormat = Objects.requireNonNull(messageFormat);
    this.indexToArgumentNames = Objects.requireNonNull(indexToArgumentNames);
  }

  @Override
  public String format(Map<String, Object> arguments) {
    Object[] argumentArray = new Object[indexToArgumentNames.size()];

    for (int i = 0; i < indexToArgumentNames.size(); i++) {
      argumentArray[i] = arguments.getOrDefault(indexToArgumentNames.get(i), "{" + i + "}");
    }

    return messageFormat.format(argumentArray);
  }

  @Override
  public Set<String> getArgumentNames() {
    return new HashSet<>(indexToArgumentNames.values());
  }
}
