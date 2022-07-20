package com.pinterest.l10nmessages;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NamePartsTest {

  @Test
  void fromBaseName() {
    NameParts nameParts = NameParts.fromBaseName("com.pinterest.l10nmessages.Messages");
    assertThat(nameParts.getBaseName()).isEqualTo("com.pinterest.l10nmessages.Messages");
    assertThat(nameParts.getClassName()).isEqualTo("Messages");
    assertThat(nameParts.getEnumName()).isEqualTo("Messages");
    assertThat(nameParts.getDotPackageName()).isEqualTo("com.pinterest.l10nmessages");
    assertThat(nameParts.getFullyQualifiedEnumName())
        .isEqualTo("com.pinterest.l10nmessages.Messages");
    assertThat(nameParts.getRootPropertiesPath())
        .isEqualTo("com/pinterest/l10nmessages/Messages.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("Messages.properties");
    assertThat(nameParts.resolveInPackage("Messages_fr.properties"))
        .isEqualTo("com/pinterest/l10nmessages/Messages_fr.properties");
  }

  @Test
  void fromBaseNameNoPackage() {
    NameParts nameParts = NameParts.fromBaseName("Messages");
    assertThat(nameParts.getBaseName()).isEqualTo("Messages");
    assertThat(nameParts.getClassName()).isEqualTo("Messages");
    assertThat(nameParts.getEnumName()).isEqualTo("Messages");
    assertEquals("", nameParts.getDotPackageName());
    assertThat(nameParts.getFullyQualifiedEnumName()).isEqualTo("Messages");
    assertThat(nameParts.getRootPropertiesPath()).isEqualTo("Messages.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("Messages.properties");
    assertThat(nameParts.resolveInPackage("Messages_fr.properties"))
        .isEqualTo("Messages_fr.properties");
  }

  @Test
  void classNameIsJavaKeyword() {
    NameParts nameParts = NameParts.fromBaseName("com.pinterest.l10nmessages.public");
    assertThat(nameParts.getBaseName()).isEqualTo("com.pinterest.l10nmessages.public");
    assertThat(nameParts.getClassName()).isEqualTo("public");
    assertThat(nameParts.getEnumName()).isEqualTo("_public");
    assertThat(nameParts.getDotPackageName()).isEqualTo("com.pinterest.l10nmessages");
    assertThat(nameParts.getFullyQualifiedEnumName())
        .isEqualTo("com.pinterest.l10nmessages._public");
    assertThat(nameParts.getRootPropertiesPath())
        .isEqualTo("com/pinterest/l10nmessages/public.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("public.properties");
    assertThat(nameParts.resolveInPackage("public_fr.properties"))
        .isEqualTo("com/pinterest/l10nmessages/public_fr.properties");
  }

  @Test
  void fromPropertiesPath() {
    NameParts nameParts =
        NameParts.fromPropertiesPath("com/pinterest/l10nmessages/Messages.properties");
    assertThat(nameParts.getBaseName()).isEqualTo("com.pinterest.l10nmessages.Messages");
    assertThat(nameParts.getClassName()).isEqualTo("Messages");
    assertThat(nameParts.getEnumName()).isEqualTo("Messages");
    assertThat(nameParts.getDotPackageName()).isEqualTo("com.pinterest.l10nmessages");
    assertThat(nameParts.getFullyQualifiedEnumName())
        .isEqualTo("com.pinterest.l10nmessages.Messages");
    assertThat(nameParts.getRootPropertiesPath())
        .isEqualTo("com/pinterest/l10nmessages/Messages.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("Messages.properties");
    assertThat(nameParts.resolveInPackage("Messages_fr.properties"))
        .isEqualTo("com/pinterest/l10nmessages/Messages_fr.properties");
  }

  @Test
  void fromPropertiesPathNoPackage() {
    NameParts nameParts = NameParts.fromPropertiesPath("Messages.properties");
    assertThat(nameParts.getBaseName()).isEqualTo("Messages");
    assertThat(nameParts.getClassName()).isEqualTo("Messages");
    assertThat(nameParts.getEnumName()).isEqualTo("Messages");
    assertEquals("", nameParts.getDotPackageName());
    assertThat(nameParts.getFullyQualifiedEnumName()).isEqualTo("Messages");
    assertThat(nameParts.getRootPropertiesPath()).isEqualTo("Messages.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("Messages.properties");
    assertThat(nameParts.resolveInPackage("Messages_fr.properties"))
        .isEqualTo("Messages_fr.properties");
  }

  @Test
  void fromPropertiesPathClassNameIsJavaKeyword() {
    NameParts nameParts =
        NameParts.fromPropertiesPath("com/pinterest/l10nmessages/public.properties");
    assertThat(nameParts.getBaseName()).isEqualTo("com.pinterest.l10nmessages.public");
    assertThat(nameParts.getClassName()).isEqualTo("public");
    assertThat(nameParts.getEnumName()).isEqualTo("_public");
    assertThat(nameParts.getDotPackageName()).isEqualTo("com.pinterest.l10nmessages");
    assertThat(nameParts.getFullyQualifiedEnumName())
        .isEqualTo("com.pinterest.l10nmessages._public");
    assertThat(nameParts.getRootPropertiesPath())
        .isEqualTo("com/pinterest/l10nmessages/public.properties");
    assertThat(nameParts.getRootPropertiesFilename()).isEqualTo("public.properties");
    assertThat(nameParts.resolveInPackage("public_fr.properties"))
        .isEqualTo("com/pinterest/l10nmessages/public_fr.properties");
  }

  @Test
  void fromPropertiesPathInvalid() {
    assertThatThrownBy(() -> NameParts.fromPropertiesPath("com/pinterest/l10nmessages/invalid"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Path must end with .properties");
  }
}
