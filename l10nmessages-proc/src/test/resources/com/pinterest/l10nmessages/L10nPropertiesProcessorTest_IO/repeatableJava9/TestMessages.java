package com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.repeatableJava9;

import javax.annotation.processing.Generated;

@Generated("com.pinterest.l10nmessages.L10nPropertiesProcessor")
public enum TestMessages {
  hello_user("hello_user");

  public static final String BASENAME = "com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.repeatableJava9.TestMessages";
  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = "AUTO";

  private String key;

  TestMessages(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return key;
  }
}
