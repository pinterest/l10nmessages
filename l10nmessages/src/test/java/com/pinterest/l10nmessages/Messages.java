package com.pinterest.l10nmessages;

public enum Messages {
  welcome("welcome"),
  format_multi_args("format_multi_args"),
  welcome_with_name("welcome_with_name"),
  welcome_with_gender("welcome_with_gender"),
  welcome_with_name_and_gender("welcome_with_name_and_gender"),
  missing_arguments("missing_arguments"),
  modify_keys("modify.keys"),
  modify_u32_key("modify key"),
  utf8_message("utf8.message"),
  utf8_key__u128512_("utf8.key.😀"),
  テスト("テスト"),
  試験("試験"),
  _u38_ampersand("&ampersand"),
  escaped("escaped"),
  iso("iso");

  public static final String BASENAME = "com.pinterest.l10nmessages.Messages";
  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = "AUTO";

  private String key;

  Messages(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return key;
  }
}
