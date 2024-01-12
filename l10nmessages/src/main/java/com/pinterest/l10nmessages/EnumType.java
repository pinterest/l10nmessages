package com.pinterest.l10nmessages;

import java.util.Map;

/**
 * The Enum type to generate.
 *
 * <p>Controls the content of the generated enum for strong typing.
 *
 * <p>{@link #WITH_ARGUMENT_BUILDERS} provide stronger typing but requires one class to be generated
 * by message with arguments. If this not acceptable, use {@link #KEYS_ONLY} (default value).
 *
 * <p>For large files with messages that are not referenced explicitly by key in code it is possible
 * to generate the enum with no keys {@link #NO_KEYS}. The enum can still be used to create the
 * L10nMessages instance but obviously there no type checking anymore.
 */
public enum EnumType {
  /**
   * Generate an enum of message keys to use in the format function.
   *
   * <p>This provides only strong typing of the message keys. Arguments are provided as "untyped"
   * key/value pairs.
   *
   * <p>Typically used with {@link L10nMessages#format(Object, Map)}} and its vargs variants.
   */
  KEYS_ONLY,
  /**
   * Generate an enum of message keys and for messages with arguments, generate additional argument
   * builders.
   *
   * <p>This provides strong typing of message keys and arguments.
   *
   * <p>Typically used with {@link com.pinterest.l10nmessages.L10nMessages#format(FormatContext)}
   */
  WITH_ARGUMENT_BUILDERS,
  /**
   * Generates an enum with no keys.
   *
   * <p>This can be used to work with large properties files that generate the following compilation
   * error: "error: code too large" when the enum becomes too large with {@link #KEYS_ONLY}.
   *
   * <p>Obviously strong typing does not apply anymore but other enum attribute can be used for
   * example to create an instance of L10nMessages with {@link L10nMessages#builder(Class)}
   *
   * <p>Typically used with {@link com.pinterest.l10nmessages.L10nMessages#format(Object)}
   */
  NO_KEYS,
}
