package com.pinterest.l10nmessages;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class L10nPropertiesEnumGenerator {

  static final String IMPORT_STATEMENT_FOR_ARGUMENTS =
      "import com.pinterest.l10nmessages.FormatContext;\n"
          + "import java.util.LinkedHashMap;\n"
          + "import java.util.Map;\n";

  static final String IMPORT_GENERATED_ANNOTATION =
      (shouldUseOldGeneratedAnnotation()
              ? "import javax.annotation.Generated;"
              : "import javax.annotation.processing.Generated;")
          + "\n";

  private L10nPropertiesEnumGenerator() {}

  public static String toEnum(
      LinkedHashMap<String, String> deduplicatedWithReporting,
      NameParts nameParts,
      ToJavaIdentifiers toJavaIdentifier,
      MessageFormatAdapterProviders messageFormatAdapterProviders,
      String generatedAnnotationValue,
      EnumType enumType) {

    Objects.requireNonNull(deduplicatedWithReporting);
    Objects.requireNonNull(nameParts);
    Objects.requireNonNull(toJavaIdentifier);
    Objects.requireNonNull(messageFormatAdapterProviders);
    Objects.requireNonNull(generatedAnnotationValue);
    Objects.requireNonNull(enumType);

    String formatContexts =
        deduplicatedWithReporting.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(
                entry ->
                    generateFormatContextForEntry(
                        nameParts, toJavaIdentifier, messageFormatAdapterProviders, entry))
            .filter(fc -> !Objects.isNull(fc))
            .collect(Collectors.joining("\n"));

    String enumValues =
        deduplicatedWithReporting.keySet().stream()
            .map(key -> String.format("  %s(\"%s\")", toJavaIdentifier.convert(key), key))
            .collect(Collectors.joining(",\n"));

    String packageString =
        nameParts.getDotPackageName().isEmpty()
            ? ""
            : "package " + nameParts.getDotPackageName() + ";";

    return String.format(
        ""
            + "%1$s\n\n"
            + "%2$s"
            + "%3$s"
            + "\n"
            + "@Generated(\"%4$s\")\n"
            + "public enum %5$s {\n"
            + "%6$s;\n"
            + "\n"
            + "  public static final String BASENAME = \"%7$s\";\n"
            + "  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = \"%8$s\";\n"
            + "\n"
            + "  private String key;\n"
            + "\n"
            + "  %5$s(String key) {\n"
            + "    this.key = key;\n"
            + "  }\n"
            + "\n"
            + "  @Override\n"
            + "  public String toString() {\n"
            + "    return key;\n"
            + "  }\n"
            + "%9$s"
            + "}\n",
        packageString, // 1
        enumType.WITH_ARGUMENT_BUILDERS.equals(enumType) ? IMPORT_STATEMENT_FOR_ARGUMENTS : "", // 2
        IMPORT_GENERATED_ANNOTATION, // 3
        generatedAnnotationValue, // 4
        nameParts.getEnumName(), // 5
        enumValues, // 6
        nameParts.getBaseName(), // 7
        messageFormatAdapterProviders, // 8
        enumType.WITH_ARGUMENT_BUILDERS.equals(enumType) ? formatContexts + "\n" : ""); // 9
  }

  private static String generateFormatContextForEntry(
      NameParts nameParts,
      ToJavaIdentifiers toJavaIdentifier,
      MessageFormatAdapterProviders messageFormatAdapterProviders,
      Entry<String, String> entry) {

    Set<String> entryArgumentNames;
    try {
      entryArgumentNames =
          messageFormatAdapterProviders.get(entry.getValue(), Locale.ROOT).getArgumentNames();
    } catch (Throwable t) {
      // if argument are not valid we don't generate setters
      entryArgumentNames = Collections.emptySet();
    }

    String formatContextForEntryCode = null;

    if (!entryArgumentNames.isEmpty()) {
      String keyAsJavaIdentifier = toJavaIdentifier.convert(entry.getKey());
      String classNameOfFormatContextForEntry = "FC_" + keyAsJavaIdentifier;

      String argumentSetters =
          entryArgumentNames.stream()
              .sorted()
              .map(
                  argumentName ->
                      generateSetterForArgument(
                          toJavaIdentifier, classNameOfFormatContextForEntry, argumentName))
              .collect(Collectors.joining("\n"));

      formatContextForEntryCode =
          String.format(
              "\n"
                  + "  public static class %1$s implements FormatContext<%2$s> {\n"
                  + "    Map<String, Object> map = new LinkedHashMap<>();\n"
                  + "\n"
                  + "    @Override\n"
                  + "    public %2$s getKey() {\n"
                  + "      return %3$s;\n"
                  + "    }\n"
                  + "\n"
                  + "    @Override\n"
                  + "    public Map<String, Object> getArguments() {\n"
                  + "      return map;\n"
                  + "    }\n"
                  + "\n"
                  + "%4$s"
                  + "  }\n"
                  + "\n"
                  + "  public static %1$s %3$s() {\n"
                  + "    return new %1$s();\n"
                  + "  }",
              classNameOfFormatContextForEntry,
              nameParts.getEnumName(),
              keyAsJavaIdentifier,
              argumentSetters);
    }

    return formatContextForEntryCode;
  }

  private static String generateSetterForArgument(
      ToJavaIdentifiers toJavaIdentifier,
      String classNameOfEntryFormatContext,
      String argumentName) {
    String argumentNameAsJavaIdentifier = toJavaIdentifier.convert(argumentName);
    return String.format(
        ""
            + "    public %1$s %2$s(Object value) {\n"
            + "      map.put(\"%3$s\", value);\n"
            + "      return this;\n"
            + "    }\n",
        classNameOfEntryFormatContext, argumentNameAsJavaIdentifier, argumentName);
  }

  static boolean shouldUseOldGeneratedAnnotation() {
    boolean useOldGeneratedAnnotation;

    String systemProperty = System.getProperty("io.l10nmessages.useOldGeneratedAnnotation");

    if (systemProperty != null) {
      useOldGeneratedAnnotation = Boolean.parseBoolean(systemProperty);
    } else {
      try {
        Class.forName("java.lang.Module");
        useOldGeneratedAnnotation = false;
      } catch (ClassNotFoundException e) {
        useOldGeneratedAnnotation = true;
      }
    }

    return useOldGeneratedAnnotation;
  }
}
