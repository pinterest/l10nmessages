package com.pinterest.l10nmessages;

import java.lang.reflect.Field;

class Reflection {

  private Reflection() {
    throw new AssertionError();
  }

  static boolean isClassPresent(String className) {
    boolean isClassPresent;
    try {
      Class.forName(className, true, Reflection.class.getClassLoader());
      isClassPresent = true;
    } catch (ClassNotFoundException e) {
      isClassPresent = false;
    }
    return isClassPresent;
  }

  static <R, T> R getPublicStaticFiledOrNull(String fieldName, Class<T> clazz) {
    R value;
    try {
      Field filed = clazz.getField(fieldName);
      value = (R) filed.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      value = null;
    }
    return value;
  }
}
