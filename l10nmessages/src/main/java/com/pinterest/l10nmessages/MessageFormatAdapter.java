package com.pinterest.l10nmessages;

import java.util.Map;
import java.util.Set;

/**
 * Adapter for {@link java.text.MessageFormat} and {@code com.ibm.icu.text.MessageFormat} to
 * expose formatting capabilities and expose arguments names of the message format.
 */
public interface MessageFormatAdapter {

  /**
   * Formats the message with provided arguments.
   *
   * @param arguments Map of arguments keyed by argument names
   * @return the formatted message
   */
  String format(Map<String, Object> arguments);

  /**
   * Returns the names of the message format arguments
   *
   * @return a Set of argument names
   */
  Set<String> getArgumentNames();
}
