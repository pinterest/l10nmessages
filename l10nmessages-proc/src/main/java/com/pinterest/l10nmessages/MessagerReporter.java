package com.pinterest.l10nmessages;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

class MessagerReporter implements Reporter {

  Messager messager;
  boolean hasDebug;

  MessagerReporter(Messager messager, boolean hasDebug) {
    this.messager = messager;
    this.hasDebug = hasDebug;
  }

  @Override
  public void info(String message) {
    messager.printMessage(Kind.NOTE, message);
  }

  @Override
  public void error(String message) {
    messager.printMessage(Kind.ERROR, message);
  }

  @Override
  public void errorf(Element element, String messagePattern, Object... args) {
    messager.printMessage(Kind.ERROR, String.format(messagePattern, args), element);
  }

  public void debug(String message) {
    if (hasDebug) {
      info(message);
    }
  }
}
