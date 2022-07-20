package com.pinterest.l10nmessages;

import java.util.Optional;

/** Defines the behavior to apply when a missing argument is found in a message. */
@FunctionalInterface
public interface OnMissingArgument {

  /**
   * Behavior to apply when a missing argument is found in a message.
   *
   * <p>Implementation can either fail safe or throw an exception to stop the formatting.
   *
   * @param baseName base name of resource that contains the message
   * @param key the message key
   * @param argumentName the argument name
   * @return an optional fallback argument value to use if not interrupting the formatting flow with
   *     an exception
   * @throws OnMissingArgumentException an Exception to stop the formatting flow
   */
  Optional<Object> apply(String baseName, String key, String argumentName)
      throws OnMissingArgumentException;
}
