package com.pinterest.l10nmessages;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.MessagePattern;
import com.ibm.icu.text.MessagePattern.Part;
import com.ibm.icu.text.MessagePattern.Part.Type;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class MessageFormatAdapterICU implements MessageFormatAdapter {

  final MessageFormat messageFormat;
  final Set<String> argumentNames;

  public MessageFormatAdapterICU(MessageFormat messageFormat) {
    this.messageFormat = Objects.requireNonNull(messageFormat);
    this.argumentNames = getArgumentNamesFromMessageFormat(messageFormat);
  }

  @Override
  public String format(Map<String, Object> arguments) {
    return messageFormat.format(arguments);
  }

  @Override
  public Set<String> getArgumentNames() {
    return argumentNames;
  }

  static Set<String> getArgumentNamesFromMessageFormat(MessageFormat messageFormat) {
    Set<String> argumentNames;
    try {
      argumentNames =
          getArgumentNamesFromMessagePattern(getMessagePatternOfMessageFormat(messageFormat));
    } catch (Throwable t) {
      // extra level of safety, we just fallback to top-level arguments from the MessageFormat
      argumentNames = messageFormat.getArgumentNames();
    }
    return argumentNames;
  }

  static MessagePattern getMessagePatternOfMessageFormat(MessageFormat messageFormat) {
    MessagePattern msgPattern;
    try {
      Field msgPatternField = messageFormat.getClass().getDeclaredField("msgPattern");
      msgPatternField.setAccessible(true);
      msgPattern = (MessagePattern) msgPatternField.get(messageFormat);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      msgPattern = new MessagePattern(messageFormat.toPattern());
    }
    return msgPattern;
  }

  static Set<String> getArgumentNamesFromMessagePattern(MessagePattern messagePattern) {
    HashSet<String> argNames = new LinkedHashSet<>();
    for (int partIndex = 0; partIndex < messagePattern.countParts(); partIndex++) {
      MessagePattern.Part.Type type = messagePattern.getPartType(partIndex);
      if (type == Type.ARG_NAME || type == Type.ARG_NUMBER) {
        Part part = messagePattern.getPart(partIndex);
        argNames.add(
            messagePattern
                .getPatternString()
                .substring(part.getIndex(), part.getIndex() + part.getLength()));
      }
    }
    return argNames;
  }
}
