package com.pinterest.l10nmessages;

/** Defines the behavior to apply when an error occurred during formatting */
@FunctionalInterface
public interface OnFormatError {

  /**
   * Behavior to apply when an error occurred during formatting.
   *
   * <p>Implementations can either fail safe or throw an exception to stop the formatting.
   *
   * @param throwable the formatting error that occured
   * @param baseName base name of resource that contains the message
   * @param key the message key
   * @param message the message itself
   * @return the fallback message to use if not interrupting the flow with an exception
   * @throws OnFormatErrorException an exception to stop the formatting flow
   */
  String apply(Throwable throwable, String baseName, String key, String message)
      throws OnFormatErrorException;
}
