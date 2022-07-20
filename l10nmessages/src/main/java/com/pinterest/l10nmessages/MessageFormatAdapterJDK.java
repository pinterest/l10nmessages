package com.pinterest.l10nmessages;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.IntStream;

class MessageFormatAdapterJDK implements MessageFormatAdapter {

  final MessageFormat messageFormat;
  final Set<String> argumentNames;

  public MessageFormatAdapterJDK(MessageFormat messageFormat) {
    this.messageFormat = Objects.requireNonNull(messageFormat);
    this.argumentNames = getArgumentNames(messageFormat);
  }

  @Override
  public String format(Map<String, Object> arguments) {
    Object[] argumentArray;
    OptionalInt max = arguments.keySet().stream().mapToInt(Integer::valueOf).max();
    if (max.isPresent()) {
      argumentArray =
          IntStream.range(0, max.getAsInt() + 1)
              .mapToObj(Integer::toString)
              .map(k -> arguments.getOrDefault(k, "{" + k + "}"))
              .toArray();

    } else {
      argumentArray = new Object[0];
    }
    return messageFormat.format(argumentArray);
  }

  @Override
  public Set<String> getArgumentNames() {
    return argumentNames;
  }

  /**
   * This method uses pattern matching to handle skipped arguments. Eg. "{0} {2}" will return {"0",
   * "2"} (and not {"0", "1", "2"}).
   */
  static Set<String> getArgumentNames(MessageFormat messageFormat) {
    String pattern = messageFormat.toPattern();
    int length = messageFormat.getFormatsByArgumentIndex().length;
    HashSet<String> argumentNames = new HashSet<>(length);
    for (int i = 0; i < length; i++) {
      String argumentName = Integer.toString(i);
      if (pattern.contains("{" + argumentName + "}")
          || pattern.contains("{" + argumentName + ",number")
          || pattern.contains("{" + argumentName + ",date")
          || pattern.contains("{" + argumentName + ",time")
          || pattern.contains("{" + argumentName + ",choice")) {
        argumentNames.add(argumentName);
      }
    }
    return argumentNames;
  }
}
