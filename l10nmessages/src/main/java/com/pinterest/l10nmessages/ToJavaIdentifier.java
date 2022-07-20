package com.pinterest.l10nmessages;

/**
 * Convert a value (message keys and argument names) to a valid java identifier for inclusion as
 * enum values and for the argument builders.
 */
public interface ToJavaIdentifier {

  String convert(String value);
}
