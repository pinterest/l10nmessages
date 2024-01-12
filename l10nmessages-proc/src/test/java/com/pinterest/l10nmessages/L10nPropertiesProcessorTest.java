package com.pinterest.l10nmessages;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.pinterest.l10nmessages.L10nPropertiesEnumGenerator.shouldUseOldGeneratedAnnotation;
import static com.pinterest.l10nmessages.PropertiesLoaderTest.getPropertiesEntries;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.google.common.io.Resources;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject.DiagnosticInFile;
import com.google.testing.compile.JavaFileObjectSubject;
import com.google.testing.compile.JavaFileObjects;
import com.pinterest.l10nmessages.PropertiesLoader.PropertiesEntry;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

class L10nPropertiesProcessorTest {

  static final String DUPLICATES_PROPERTIES =
      "/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties";

  String testName;

  @BeforeEach
  public void beforeEach(TestInfo testInfo) {
    testName = testInfo.getTestMethod().get().getName();
  }

  @Test
  public void valid() {
    assumeTrue(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);
  }

  @Test
  public void validArgumentBuilder() {
    assumeTrue(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);
  }

  @Test
  public void validNoKey() {
    assumeTrue(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);
  }

  @Test
  public void validJava9() {
    assumeFalse(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);
  }

  @Test
  public void repeatable() {
    assumeTrue(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);

    // test the second file
    JavaFileObjectSubject javaFileObjectSubject =
        assertThat(compilation).generatedSourceFile(Generator.getEnumOutputName(testName) + "2");

    javaFileObjectSubject.hasSourceEquivalentTo(
        JavaFileObjects.forResource(
            String.format(
                "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/%1$s/TestMessages2.java",
                testName)));
  }

  @Test
  public void repeatableJava9() {
    assumeFalse(shouldUseOldGeneratedAnnotation());
    Compilation compilation = defaultCompile();
    checkGenerationSuccessful(compilation);

    // test the second file
    compareGenerateWithTestResource(
        compilation,
        Generator.getEnumOutputName(testName) + "2",
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/%1$s/TestMessages2.java");
  }

  @Test
  public void invalidSource() {
    Compilation compilation = defaultCompile();
    failedWithError(
        compilation,
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/invalidSource/TestMessages.properties Invalid message format (fix or configure checks with @L10nProperties(messageFormatValidationTargets={})\n"
            + "  key:     'hello_user'\n"
            + "  message: 'Hello {user'",
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/invalidSource/TestMessages.properties Invalid message format (fix or configure checks with @L10nProperties(messageFormatValidationTargets={})\n"
            + "  key:     'bye_user'\n"
            + "  message: 'bye {user'");
  }

  @Test
  public void invalidSourceNoCheck() {
    Compilation compilation = defaultCompile();
    assertThat(compilation).succeededWithoutWarnings();
  }

  @Test
  public void invalidTarget() {
    Compilation compilation = defaultCompile();
    failedWithError(
        compilation,
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/invalidTarget/TestMessages_fr_FR.properties Invalid message format (fix or configure checks with @L10nProperties(messageFormatValidationTargets={})\n"
            + "  key:     'hello_user'\n"
            + "  message: 'Bonjour {user'",
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/invalidTarget/TestMessages_fr_FR.properties Invalid message format (fix or configure checks with @L10nProperties(messageFormatValidationTargets={})\n"
            + "  key:     'bye_user'\n"
            + "  message: 'Au revoir {user'");
  }

  @Test
  public void invalidTargetNoCheck() {
    Compilation compilation = defaultCompile();
    assertThat(compilation).succeededWithoutWarnings();
  }

  @Test
  public void missingProperties() {
    Compilation compilation = defaultCompile();
    failedWithErrorInFile(
        compilation,
        getTestAppJavaPath(),
        "Found @L10nProperties with baseName: "
            + "\"com.pinterest.l10nmessages.L10nPropertiesProcessorTest_IO.missingproperties.TestMessages\" "
            + "but required matching resource: \"com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/missingproperties/TestMessages.properties\""
            + " is missing. \n"
            + "  Make sure that:\n"
            + "   - the baseName in @L10nProperties follows the pattern: \"{package}.{filenameWithoutExtension}\"\n"
            + "   - the .properties file is accessible as resource in the proper package");
  }

  @Test
  void deduplicateEntriesWithReportingRejectedEscapingAndUnderscore() {
    Reporter reporterMock = Mockito.mock(Reporter.class);
    ProcessorCore processorCore =
        new ProcessorCore(
            Mockito.mock(AbstractFiler.class),
            reporterMock,
            L10nPropertiesProcessor.class.getName());
    List<PropertiesEntry> entries = getPropertiesEntries(DUPLICATES_PROPERTIES);
    Set<String> deduplicatedKeysWithReporting =
        processorCore.deduplicatedKeysWithReporting(
            entries,
            ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE,
            OnDuplicatedKeys.REJECT,
            DUPLICATES_PROPERTIES);
    assertIterableEquals(
        Arrays.asList("a", "b", "c", "d", "a_1", "a_2"), deduplicatedKeysWithReporting);
    InOrder inOrder = Mockito.inOrder(reporterMock);
    inOrder
        .verify(reporterMock)
        .error(
            "Duplicated key 'b'. Fix '/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties'. Alternatively, use OnDuplicatedKeys#ACCEPT or OnDuplicatedKeys#ACCEPT_IF_SAME");
    inOrder
        .verify(reporterMock)
        .error(
            "Duplicated key 'c'. Fix '/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties'. Alternatively, use OnDuplicatedKeys#ACCEPT or OnDuplicatedKeys#ACCEPT_IF_SAME");
    inOrder
        .verify(reporterMock)
        .error(
            "Duplicated key 'd'. Fix '/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties'. Alternatively, use OnDuplicatedKeys#ACCEPT or OnDuplicatedKeys#ACCEPT_IF_SAME");
    inOrder
        .verify(reporterMock)
        .errorf(
            "Generated enum value '%1$s' is duplicated. Rename key '%2$s' or key '%3$s' in '%4$s' to avoid this collision. Alternatively, use ToJavaIdentifiers#ESCAPING_ONLY",
            "a_1",
            "a.1",
            "a_1",
            "/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties");
    inOrder
        .verify(reporterMock)
        .errorf(
            "Generated enum value '%1$s' is duplicated. Rename key '%2$s' or key '%3$s' in '%4$s' to avoid this collision. Alternatively, use ToJavaIdentifiers#ESCAPING_ONLY",
            "a_2",
            "a.2",
            "a_2",
            "/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties");

    Mockito.verifyNoMoreInteractions(reporterMock);
  }

  @Test
  void deduplicateEntriesWithReportingAcceptEscaping() {
    Reporter reporterMock = Mockito.mock(Reporter.class);
    ProcessorCore processorCore =
        new ProcessorCore(
            Mockito.mock(AbstractFiler.class),
            reporterMock,
            L10nPropertiesProcessor.class.getName());
    List<PropertiesEntry> entries = getPropertiesEntries(DUPLICATES_PROPERTIES);
    Set<String> deduplicatedKeysWithReporting =
        processorCore.deduplicatedKeysWithReporting(
            entries,
            ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE,
            OnDuplicatedKeys.ACCEPT,
            DUPLICATES_PROPERTIES);
    assertIterableEquals(
        Arrays.asList("a", "b", "c", "d", "a_1", "a_2"), deduplicatedKeysWithReporting);
    InOrder inOrder = Mockito.inOrder(reporterMock);
    inOrder
        .verify(reporterMock)
        .errorf(
            "Generated enum value '%1$s' is duplicated. Rename key '%2$s' or key '%3$s' in '%4$s' to avoid this collision. Alternatively, use ToJavaIdentifiers#ESCAPING_ONLY",
            "a_1",
            "a.1",
            "a_1",
            "/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties");
    inOrder
        .verify(reporterMock)
        .errorf(
            "Generated enum value '%1$s' is duplicated. Rename key '%2$s' or key '%3$s' in '%4$s' to avoid this collision. Alternatively, use ToJavaIdentifiers#ESCAPING_ONLY",
            "a_2",
            "a.2",
            "a_2",
            "/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties");

    Mockito.verifyNoMoreInteractions(reporterMock);
  }

  @Test
  void deduplicateEntriesWithReportingAcceptIfEqualsEscaping() {
    Reporter reporterMock = Mockito.mock(Reporter.class);
    ProcessorCore processorCore =
        new ProcessorCore(
            Mockito.mock(AbstractFiler.class),
            reporterMock,
            L10nPropertiesProcessor.class.getName());
    List<PropertiesEntry> entries = getPropertiesEntries(DUPLICATES_PROPERTIES);
    Set<String> deduplicatedKeysWithReporting =
        processorCore.deduplicatedKeysWithReporting(
            entries,
            ToJavaIdentifiers.ESCAPING_ONLY,
            OnDuplicatedKeys.ACCEPT_IF_SAME,
            DUPLICATES_PROPERTIES);
    assertIterableEquals(
        Arrays.asList("a", "b", "c", "d", "a_1", "a.1", "a_2", "a.2"),
        deduplicatedKeysWithReporting);
    InOrder inOrder = Mockito.inOrder(reporterMock);
    inOrder
        .verify(reporterMock)
        .error(
            "Duplicated key 'd'. Fix '/com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/duplicates.properties'. Alternatively, use OnDuplicatedKeys#ACCEPT");

    Mockito.verifyNoMoreInteractions(reporterMock);
  }

  static class Generator {

    static String getEnumOutputName(String testName) {
      return String.format(
          "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/%1$s/TestMessages", testName);
    }
  }

  void failedWithError(Compilation compilation, String... errors) {
    failedWithErrorInFile(compilation, null, errors);
  }

  void failedWithErrorInFile(Compilation compilation, String file, String... errors) {
    assertThat(compilation).failed();
    Stream.of(errors)
        .forEach(
            err -> {
              DiagnosticInFile diagnosticInFile = assertThat(compilation).hadErrorContaining(err);
              if (file != null) {
                diagnosticInFile.inFile(JavaFileObjects.forResource(getTestAppJavaPath()));
              }
            });
  }

  String getTestAppJavaPath() {
    return String.format(
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/%1$s/TestApp.java", testName);
  }

  Compilation defaultCompile() {
    return javac()
        .withProcessors(new L10nPropertiesProcessor())
        .compile(JavaFileObjects.forResource(getTestAppJavaPath()));
  }

  void checkGenerationSuccessful(Compilation compilation) {
    assertThat(compilation).succeeded();
    compareGenerateWithTestResource(
        compilation,
        Generator.getEnumOutputName(testName),
        "com/pinterest/l10nmessages/L10nPropertiesProcessorTest_IO/%1$s/TestMessages.java");
  }

  void compareGenerateWithTestResource(
      Compilation compilation, String generatedPath, String resourcePathPattern) {

    try {
      String resourcePath = String.format(resourcePathPattern, testName);

      String generated =
          compilation.generatedSourceFile(generatedPath).get().getCharContent(false).toString();

      String expected =
          Resources.toString(Resources.getResource(resourcePath), StandardCharsets.UTF_8);

      Assertions.assertThat(generated.trim()).isEqualTo(expected.trim());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
