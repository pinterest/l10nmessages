package com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.validArgumentBuilder;

import com.pinterest.l10nmessages.FormatContext;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.pinterest.l10nmessages.L10nPropertiesProcessor")
public enum TestMessages {
  hello_user("hello_user");

  public static final String BASENAME = "com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.validArgumentBuilder.TestMessages";
  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = "AUTO";

  private String key;

  TestMessages(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return key;
  }

  public static class FC_hello_user implements FormatContext<TestMessages> {
    Map<String, Object> map = new LinkedHashMap<>();

    @Override
    public TestMessages getKey() {
      return hello_user;
    }

    @Override
    public Map<String, Object> getArguments() {
      return map;
    }

    public FC_hello_user username(Object value) {
      map.put("username", value);
      return this;
    }
  }

  public static FC_hello_user hello_user() {
    return new FC_hello_user();
  }
}