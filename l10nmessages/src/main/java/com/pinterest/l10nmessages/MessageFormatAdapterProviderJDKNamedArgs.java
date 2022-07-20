package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.MessageFormatAdapterProviders.withFormatOrNoop;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** A provider to add named argument support to JDK message formatting. */
public class MessageFormatAdapterProviderJDKNamedArgs implements MessageFormatAdapterProvider {
  @Override
  public MessageFormatAdapter get(String message, Locale locale) {
    MessageWithNumberedArgs messageWithNumberedArgs = toMessageWithNumberedArgs(message);
    return withFormatOrNoop(
        message,
        new MessageFormatAdapterJDKNamedArgs(
            new MessageFormat(messageWithNumberedArgs.getMessage(), locale),
            messageWithNumberedArgs.getIndexToArgumentNames()));
  }

  MessageWithNumberedArgs toMessageWithNumberedArgs(String message) {

    MessageWithNumberedArgs messageWithNumberedArgs;

    ArrayList<PlaceholderIndices> placeholderIndices = getPlaceholdersIndices(message);

    if (placeholderIndices.size() == 0) {
      messageWithNumberedArgs = new MessageWithNumberedArgs(message, new LinkedHashMap<>());
    } else {
      Map<Integer, String> indexToArgumentNames = new LinkedHashMap<>();
      Map<String, Integer> argumentNamesToIndex = new LinkedHashMap<>();
      StringBuilder sb = new StringBuilder();

      int current = 0;
      for (int j = 0; j < placeholderIndices.size(); j++) {
        int start = placeholderIndices.get(j).getStart();
        int end = placeholderIndices.get(j).getEnd();

        sb.append(message, current, start + 1);
        String placeholder = message.substring(start + 1, end).trim();

        Integer index = argumentNamesToIndex.get(placeholder);
        if (index == null) {
          index = argumentNamesToIndex.size();
          indexToArgumentNames.put(index, placeholder);
          argumentNamesToIndex.put(placeholder, index);
        }
        sb.append(index);
        current = end;
      }
      sb.append(message, current, message.length());
      messageWithNumberedArgs = new MessageWithNumberedArgs(sb.toString(), indexToArgumentNames);
    }

    return messageWithNumberedArgs;
  }

  private ArrayList<PlaceholderIndices> getPlaceholdersIndices(String message) {
    ArrayList<PlaceholderIndices> placeholderIndices = new ArrayList<>();

    int i = 0;
    while (i < message.length()) {
      if (message.charAt(i) == '{' && (i == 0 || i > 0 && message.charAt(i - 1) != '\'')) {
        int start = i;
        i++;
        while (i < message.length()) {
          char currentChar = message.charAt(i);
          if (currentChar == '}' || currentChar == ',') {
            placeholderIndices.add(new PlaceholderIndices(start, i));
            break;
          } else if (!Character.isLetterOrDigit(currentChar)
              && !Character.isWhitespace(currentChar)) {
            break;
          }
          i++;
        }
      }
      i++;
    }
    return placeholderIndices;
  }

  static class PlaceholderIndices {
    private int start;
    private int end;

    public PlaceholderIndices(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public int getStart() {
      return start;
    }

    public int getEnd() {
      return end;
    }
  }

  static class MessageWithNumberedArgs {
    private String message;
    private Map<Integer, String> indexToArgumentNames;

    public MessageWithNumberedArgs(String message, Map<Integer, String> indexToArgumentNames) {
      this.message = Objects.requireNonNull(message);
      this.indexToArgumentNames = Objects.requireNonNull(indexToArgumentNames);
    }

    public String getMessage() {
      return message;
    }

    public Map<Integer, String> getIndexToArgumentNames() {
      return indexToArgumentNames;
    }
  }
}
