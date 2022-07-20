package com.pinterest.l10nmessages;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import javax.lang.model.SourceVersion;

public enum ToJavaIdentifiers implements ToJavaIdentifier {
  /**
   * Replaces '.' with '_'. Prepends '_' to the the first character if it is not permissible as
   * first character but permissible as part of an identifier. Other invalid characters are escaped
   * with escape sequence: '_u{unicodeCodePoint}_'. Prepends '_' to Java keywords.
   *
   * <p>This conversion is meant to generate user friendly enum values, converting common pattern
   * seen in properties key to user Java identifier with "_". Eg. properties key: "a.b" is converted
   * to enum value: "a_b".
   *
   * <p>The conversion is N to 1 and may lead to collision. In case of collision and if there is no
   * control over the properties key then {@link ToJavaIdentifiers#ESCAPING_ONLY} should be used
   * instead. In other case it is preferable to change the properties keys to benefit from the
   * friendlier enum values.
   */
  ESCAPING_AND_UNDERSCORE {
    @Override
    IntStream convertIntStream(String key) {
      return IntStream.concat(
              // first character
              key.codePoints().limit(1).flatMap(prependUnderscoreIfInvalidStart()),
              // the rest of the string
              key.codePoints().skip(1))
          .map(ToJavaIdentifiers::dotToUnderscore)
          .flatMap(ToJavaIdentifiers::escapeIfInvalid);
    }
  },

  /**
   * Replaces all invalid characters with escape sequence: '_u{unicodeCodePoint}_'. Prepends '_' to
   * Java keywords.
   *
   * <p>This conversion is 1 to 1 and prevents collision. It should be used when there is no control
   * over the properties keys and if using {@link ToJavaIdentifiers#ESCAPING_AND_UNDERSCORE}
   * generates collision. In other case, it should be avoided since the generated identifiers are
   * not user friendly eg. a simple properties key: "a.b" is converted to "a_u46_b".
   */
  ESCAPING_ONLY {
    @Override
    IntStream convertIntStream(String key) {
      return key.codePoints().flatMap(ToJavaIdentifiers::escapeIfInvalid);
    }
  };

  abstract IntStream convertIntStream(String key);

  @Override
  public String convert(String key) {
    Objects.requireNonNull(key, "key is null");
    String result = ToJavaIdentifiers.toString(convertIntStream(key));
    result = convertKeywords(result);
    return result;
  }

  private static String toString(IntStream intStream) {
    return intStream
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  private static IntFunction<IntStream> prependUnderscoreIfInvalidStart() {
    return codePoint ->
        (!Character.isJavaIdentifierStart(codePoint) && Character.isJavaIdentifierPart(codePoint))
            ? IntStream.of("_".codePointAt(0), codePoint)
            : IntStream.of(codePoint);
  }

  private static IntStream escapeIfInvalid(int codePoint) {
    return Character.isJavaIdentifierPart(codePoint)
        ? IntStream.of(codePoint)
        : ("_u" + codePoint + "_").codePoints();
  }

  private static int dotToUnderscore(int codePoint) {
    return codePoint == '.' ? '_' : codePoint;
  }

  private String convertKeywords(String result) {
    if (SourceVersion.isKeyword(result)) {
      result = "_" + result;
    }
    return result;
  }
}
