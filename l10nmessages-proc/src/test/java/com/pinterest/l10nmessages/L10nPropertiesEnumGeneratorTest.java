package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

public class L10nPropertiesEnumGeneratorTest {

  @Test
  void toEnumWithArguments() {
    String baseName = "com.pinterest.l10nmessages.Messages";
    NameParts nameParts = NameParts.fromBaseName(baseName);
    LinkedHashMap<String, String> entries =
        (LinkedHashMap<String, String>)
            Maps.of(
                "hello_username", "Hello {userName}, {count}",
                "bye", "Bye");
    String enumString =
        L10nPropertiesEnumGenerator.toEnum(
            entries,
            nameParts,
            ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE,
            MessageFormatAdapterProviders.JDK_NAMED_ARGS,
            L10nPropertiesProcessor.class.getName(),
            EnumType.WITH_ARGUMENT_BUILDERS);

    assertThat(enumString)
        .isEqualTo(
            String.format(
                "package com.pinterest.l10nmessages;\n"
                    + "\n"
                    + "import com.pinterest.l10nmessages.FormatContext;\n"
                    + "import java.util.LinkedHashMap;\n"
                    + "import java.util.Map;\n"
                    + "%1$s"
                    + "\n"
                    + "@Generated(\"com.pinterest.l10nmessages.L10nPropertiesProcessor\")\n"
                    + "public enum Messages {\n"
                    + "  hello_username(\"hello_username\"),\n"
                    + "  bye(\"bye\");\n"
                    + "\n"
                    + "  public static final String BASENAME = \"com.pinterest.l10nmessages.Messages\";\n"
                    + "  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = \"JDK_NAMED_ARGS\";\n"
                    + "\n"
                    + "  private String key;\n"
                    + "\n"
                    + "  Messages(String key) {\n"
                    + "    this.key = key;\n"
                    + "  }\n"
                    + "\n"
                    + "  @Override\n"
                    + "  public String toString() {\n"
                    + "    return key;\n"
                    + "  }\n"
                    + "\n"
                    + "  public static class FC_hello_username implements FormatContext<Messages> {\n"
                    + "    Map<String, Object> map = new LinkedHashMap<>();\n"
                    + "\n"
                    + "    @Override\n"
                    + "    public Messages getKey() {\n"
                    + "      return hello_username;\n"
                    + "    }\n"
                    + "\n"
                    + "    @Override\n"
                    + "    public Map<String, Object> getArguments() {\n"
                    + "      return map;\n"
                    + "    }\n"
                    + "\n"
                    + "    public FC_hello_username count(Object value) {\n"
                    + "      map.put(\"count\", value);\n"
                    + "      return this;\n"
                    + "    }\n"
                    + "\n"
                    + "    public FC_hello_username userName(Object value) {\n"
                    + "      map.put(\"userName\", value);\n"
                    + "      return this;\n"
                    + "    }\n"
                    + "  }\n"
                    + "\n"
                    + "  public static FC_hello_username hello_username() {\n"
                    + "    return new FC_hello_username();\n"
                    + "  }\n"
                    + "}\n"
                    + "",
                L10nPropertiesEnumGenerator.IMPORT_GENERATED_ANNOTATION));
  }

  @Test
  void toEnum() {
    String baseName = "com.pinterest.l10nmessages.Messages";
    NameParts nameParts = NameParts.fromBaseName(baseName);

    LinkedHashMap<String, String> entries =
        (LinkedHashMap<String, String>)
            Maps.of(
                "hello_username", "Hello {userName}, {count}",
                "bye", "Bye");

    String enumString =
        L10nPropertiesEnumGenerator.toEnum(
            entries,
            nameParts,
            ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE,
            MessageFormatAdapterProviders.JDK_NAMED_ARGS,
            L10nPropertiesProcessor.class.getName(),
            EnumType.KEYS_ONLY);

    assertThat(enumString)
        .isEqualTo(
            "package com.pinterest.l10nmessages;\n"
                + "\n"
                + L10nPropertiesEnumGenerator.IMPORT_GENERATED_ANNOTATION
                + "\n"
                + "@Generated(\"com.pinterest.l10nmessages.L10nPropertiesProcessor\")\n"
                + "public enum Messages {\n"
                + "  hello_username(\"hello_username\"),\n"
                + "  bye(\"bye\");\n"
                + "\n"
                + "  public static final String BASENAME = \"com.pinterest.l10nmessages.Messages\";\n"
                + "  public static final String MESSAGE_FORMAT_ADAPTER_PROVIDERS = \"JDK_NAMED_ARGS\";\n"
                + "\n"
                + "  private String key;\n"
                + "\n"
                + "  Messages(String key) {\n"
                + "    this.key = key;\n"
                + "  }\n"
                + "\n"
                + "  @Override\n"
                + "  public String toString() {\n"
                + "    return key;\n"
                + "  }\n"
                + "}\n",
            enumString);
  }
}
