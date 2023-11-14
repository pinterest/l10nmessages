package com.pinterest.l10nmessages;

final class JavaVersions {

  static final int SPECIFIC_VERSION_AS_INT = specificationVersionAsInt();

  private JavaVersions() {
    throw new AssertionError();
  }

  static boolean isJava17AndUp() {
    return SPECIFIC_VERSION_AS_INT >= 17;
  }

  static boolean isJava8() {
    return SPECIFIC_VERSION_AS_INT == 8;
  }

  static int specificationVersionAsInt() {
    String property = System.getProperty("java.specification.version");
    if (property.startsWith("1.")) {
      property = property.substring(2);
    }
    return Integer.parseInt(property);
  }
}
