package com.pinterest.l10nmessages;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.lang.model.SourceVersion;

public class NameParts {

  private String baseName;
  private String className;
  private String dotPackageName;
  private String slashPackageName;
  private String rootPropertiesFilename;
  private String rootPropertiesPath;
  private String enumName;
  private String fullyQualifiedEnumName;

  /**
   * Creates from a resource bundle base name (a fully-qualified class name, as defined in {@link
   * ResourceBundle})
   *
   * @param baseName a resource bundle base name (a fully-qualified class name)
   * @return an {@link NameParts} instance initialized using the base name
   */
  static NameParts fromBaseName(String baseName) {
    Objects.requireNonNull(baseName);
    int indexOfLastDot = baseName.lastIndexOf(".");
    NameParts nameParts = new NameParts();
    nameParts.baseName = baseName;
    nameParts.dotPackageName = indexOfLastDot != -1 ? baseName.substring(0, indexOfLastDot) : "";
    nameParts.className = baseName.substring(indexOfLastDot + 1);
    nameParts.slashPackageName = nameParts.dotPackageName.replace('.', '/');
    nameParts.rootPropertiesFilename = nameParts.className + ".properties";
    nameParts.rootPropertiesPath =
        resolveInPackage(nameParts.slashPackageName, nameParts.rootPropertiesFilename);
    setEnumNames(nameParts);
    return nameParts;
  }

  static NameParts fromPropertiesPath(String propertiesPathStr) {
    Objects.requireNonNull(propertiesPathStr);
    if (!propertiesPathStr.endsWith(".properties")) {
      throw new IllegalArgumentException("Path must end with .properties");
    }
    Path propertiesPath = Paths.get(propertiesPathStr);
    NameParts nameParts = new NameParts();
    nameParts.rootPropertiesPath = propertiesPath.toString();
    nameParts.rootPropertiesFilename = propertiesPath.getFileName().toString();
    nameParts.className =
        nameParts.rootPropertiesFilename.substring(
            0, nameParts.rootPropertiesFilename.length() - 11);
    nameParts.slashPackageName =
        propertiesPath.getParent() != null ? propertiesPath.getParent().toString() : "";
    nameParts.dotPackageName =
        (nameParts.slashPackageName.startsWith("/")
                ? nameParts.slashPackageName.substring(1)
                : nameParts.slashPackageName)
            .replace("/", ".");
    nameParts.baseName =
        nameParts.dotPackageName.isEmpty()
            ? nameParts.className
            : nameParts.getDotPackageName() + "." + nameParts.className;
    setEnumNames(nameParts);
    return nameParts;
  }

  private static void setEnumNames(NameParts nameParts) {
    nameParts.enumName =
        SourceVersion.isKeyword(nameParts.className)
            ? "_" + nameParts.className
            : nameParts.className;
    nameParts.fullyQualifiedEnumName =
        nameParts.dotPackageName
            + (nameParts.dotPackageName.equals("") ? "" : ".")
            + nameParts.enumName;
  }

  /**
   * @return the original resource bundle base name (a fully-qualified class name, as defined in
   *     {@link ResourceBundle})
   */
  String getBaseName() {
    return baseName;
  }

  /** @return the class name */
  String getClassName() {
    return className;
  }

  /** @return the package name in the dot notation (fully-qualified package name) */
  String getDotPackageName() {
    return dotPackageName;
  }

  /**
   * @return the root properties file path in '<tt>/</tt>'-separated notation (resource name style)
   */
  public String getRootPropertiesPath() {
    return rootPropertiesPath;
  }

  /**
   * @return the root properties file filename in '<tt>/</tt>'-separated notation (resource name
   *     style)
   */
  public String getRootPropertiesFilename() {
    return rootPropertiesFilename;
  }

  /** @return the class name of the enum that will contain the keys */
  public String getEnumName() {
    return enumName;
  }

  /** @return the fully-qualified class name of the enum that will contain the keys */
  public String getFullyQualifiedEnumName() {
    return fullyQualifiedEnumName;
  }

  /**
   * Resolve the given relative name against the "slash" package name.
   *
   * @param relativeName
   * @return
   */
  public String resolveInPackage(String relativeName) {
    return resolveInPackage(slashPackageName, relativeName);
  }

  private static String resolveInPackage(String slashPackageName, String relativeName) {
    return slashPackageName
        + (slashPackageName.isEmpty() || slashPackageName.endsWith("/") ? "" : "/")
        + relativeName;
  }
}
