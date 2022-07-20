package com.pinterest.l10nmessages;

import javax.lang.model.element.Element;

public interface Reporter {

  void debug(String message);

  void info(String message);

  void error(String message);

  default void debugf(String messagePattern, Object... args) {
    debug(String.format(messagePattern, args));
  }

  default void infof(String messagePattern, Object... args) {
    info(String.format(messagePattern, args));
  }

  default void errorf(String messagePattern, Object... args) {
    error(String.format(messagePattern, args));
  }

  default void errorf(Element element, String messagePattern, Object... args) {
    errorf(messagePattern, args);
  }
}
