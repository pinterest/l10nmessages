package com.pinterest.l10nmessages.mojo;

import com.pinterest.l10nmessages.EnumType;
import com.pinterest.l10nmessages.MessageFormatAdapterProviders;
import com.pinterest.l10nmessages.MessageFormatValidationTarget;
import com.pinterest.l10nmessages.OnDuplicatedKeys;
import com.pinterest.l10nmessages.ToJavaIdentifiers;
import org.apache.maven.plugins.annotations.Parameter;

public class L10nProperties {

  @Parameter(property = "baseName")
  String baseName;

  @Parameter(property = "messageFormatValidationTargets")
  MessageFormatValidationTarget[] messageFormatValidationTargets = {
    MessageFormatValidationTarget.ROOT, MessageFormatValidationTarget.LOCALIZED
  };

  @Parameter(property = "messageFormatAdapterProviders")
  MessageFormatAdapterProviders messageFormatAdapterProviders = MessageFormatAdapterProviders.AUTO;

  @Parameter(property = "onDuplicatedKeys")
  OnDuplicatedKeys onDuplicatedKeys = OnDuplicatedKeys.REJECT;

  @Parameter(property = "toJavaIdentifiers")
  ToJavaIdentifiers toJavaIdentifiers = ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE;

  @Parameter(property = "enumType")
  EnumType enumType = EnumType.KEYS_ONLY;

  public String getBaseName() {
    return baseName;
  }

  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  public MessageFormatValidationTarget[] getMessageFormatValidationTargets() {
    return messageFormatValidationTargets;
  }

  public void setMessageFormatValidationTargets(
      MessageFormatValidationTarget[] messageFormatValidationTargets) {
    this.messageFormatValidationTargets = messageFormatValidationTargets;
  }

  public MessageFormatAdapterProviders getMessageFormatAdapterProviders() {
    return messageFormatAdapterProviders;
  }

  public void setMessageFormatAdapterProviders(
      MessageFormatAdapterProviders messageFormatAdapterProviders) {
    this.messageFormatAdapterProviders = messageFormatAdapterProviders;
  }

  public OnDuplicatedKeys getOnDuplicatedKeys() {
    return onDuplicatedKeys;
  }

  public void setOnDuplicatedKeys(OnDuplicatedKeys onDuplicatedKeys) {
    this.onDuplicatedKeys = onDuplicatedKeys;
  }

  public ToJavaIdentifiers getToJavaIdentifiers() {
    return toJavaIdentifiers;
  }

  public void setToJavaIdentifiers(ToJavaIdentifiers toJavaIdentifiers) {
    this.toJavaIdentifiers = toJavaIdentifiers;
  }

  public void setEnumType(EnumType enumType) {
    this.enumType = enumType;
  }

  public EnumType getEnumType() {
    return enumType;
  }

  public void setGeneratorStyle(EnumType enumType) {
    this.enumType = enumType;
  }
}
