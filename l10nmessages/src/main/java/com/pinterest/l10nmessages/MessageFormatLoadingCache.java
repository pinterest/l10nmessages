package com.pinterest.l10nmessages;

/** MessageFormatAdapter cache that will load an instance on missing value. */
@FunctionalInterface
public interface MessageFormatLoadingCache {

  /**
   * Get (optionally load if missing) a {@link MessageFormatAdapter } for a message.
   *
   * @param message the message of the message to get/load
   * @return a non null {@link MessageFormatAdapter }
   */
  MessageFormatAdapter get(String message);
}
