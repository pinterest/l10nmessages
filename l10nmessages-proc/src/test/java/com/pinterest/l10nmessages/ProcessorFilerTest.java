package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.testing.compile.JavaFileObjects;
import com.pinterest.l10nmessages.AbstractFiler.NoSuchPackageException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.jupiter.api.Test;

class ProcessorFilerTest {

  @Test
  void findResourceAnnotationPath() throws IOException {
    JavaFileObject javaFileObject =
        JavaFileObjects.forResource(
            "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties");

    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.ANNOTATION_PROCESSOR_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(javaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);
    Optional<FileObject> actual =
        ProcessorFiler.findStandardLocationInputResource(
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared", "TestMessages.properties");

    assertThat(actual).isEqualTo(Optional.of(javaFileObject));
  }

  @Test
  void findResourceClassPath() throws IOException {
    JavaFileObject javaFileObject =
        JavaFileObjects.forResource(
            "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties");
    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.CLASS_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(javaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);
    Optional<FileObject> actual =
        ProcessorFiler.findStandardLocationInputResource(
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared", "TestMessages.properties");

    assertThat(actual).isEqualTo(Optional.of(javaFileObject));
  }

  @Test
  void findResourceFilerNull() throws IOException {
    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.CLASS_OUTPUT,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(null);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    Optional<FileObject> actual =
        ProcessorFiler.findStandardLocationInputResource(
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared", "TestMessages.properties");

    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  void findResourceFilerException() throws IOException {
    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.CLASS_OUTPUT,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenThrow(new IOException());

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    Optional<FileObject> actual =
        ProcessorFiler.findStandardLocationInputResource(
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared", "TestMessages.properties");

    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  void findResourceFailOpenInputStream() throws IOException {
    Filer mockFiler = mock(Filer.class);

    JavaFileObject mockJavaFileObject = mock(JavaFileObject.class);
    when(mockJavaFileObject.openInputStream()).thenThrow(new RuntimeException("for test"));

    when(mockFiler.getResource(
            StandardLocation.ANNOTATION_PROCESSOR_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(mockJavaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    Optional<FileObject> actual =
        ProcessorFiler.findStandardLocationInputResource(
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared", "TestMessages.properties");

    assertThat(actual).isEqualTo(Optional.empty());
  }

  @Test
  void getInputResource() throws IOException {

    JavaFileObject javaFileObject =
        JavaFileObjects.forResource(
            "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties");

    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.ANNOTATION_PROCESSOR_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(javaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    FileObject actual =
        ProcessorFiler.getInputResource(
            NameParts.fromPropertiesPath(
                "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties"));

    assertThat(actual).isEqualTo(javaFileObject);
  }

  @Test
  void getInputResourceMissing() throws IOException {
    Filer mockFiler = mock(Filer.class);

    when(mockFiler.getResource(
            StandardLocation.ANNOTATION_PROCESSOR_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenThrow(new IOException());

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    assertThatThrownBy(
            () -> {
              ProcessorFiler.getInputResource(
                  NameParts.fromPropertiesPath(
                      "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties"));
            })
        .isInstanceOf(RuntimeException.class)
        .hasMessage(
            "Can't find resource for package: com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared and relativeName: TestMessages.properties");
  }

  @Test
  void getResourcePackage() throws IOException {

    JavaFileObject javaFileObject =
        JavaFileObjects.forResource(
            "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties");

    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
            StandardLocation.ANNOTATION_PROCESSOR_PATH,
            "com.pinterest.l10nmessages.ProcessorFilerTest_IO.shared",
            "TestMessages.properties"))
        .thenReturn(javaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    URI actual =
        ProcessorFiler.getInputResourcePackage(
            NameParts.fromPropertiesPath(
                "com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared/TestMessages.properties"));

    assertEquals(
        javaFileObject.toUri().toString().replaceAll("TestMessages.properties$", ""),
        actual.toString());
  }

  @Test
  void findInPackage() throws URISyntaxException {
    URL url =
        ProcessorFilerTest.class.getResource(
            "/com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared");
    List<String> actual =
        ProcessorFiler.findInPackage(url.toURI(), alwaysTrue()).stream()
            .sorted()
            .collect(Collectors.toList());
    assertThat(actual)
        .isEqualTo(Arrays.asList("TestMessages.properties", "TestMessages_fr.properties"));
  }

  @Test
  void findInPackagePathMatcher() throws URISyntaxException {
    URL url =
        ProcessorFilerTest.class.getResource(
            "/com/pinterest/l10nmessages/ProcessorFilerTest_IO/shared");
    List<String> actual =
        ProcessorFiler.findInPackage(
                url.toURI(), p -> p.getFileName().toString().equals("TestMessages_fr.properties"))
            .stream()
            .sorted()
            .collect(Collectors.toList());
    assertThat(actual).isEqualTo(Arrays.asList("TestMessages_fr.properties"));
  }

  @Test
  void findInPackageNoPackage() throws URISyntaxException {
    URL url = ProcessorFilerTest.class.getResource("/");
    List<String> actual =
        ProcessorFiler.findInPackage(
                url.toURI(),
                p -> p.getFileName().toString().matches("MessagesNoPackage.*properties"))
            .stream()
            .sorted()
            .collect(Collectors.toList());
    assertEquals(
        Arrays.asList("MessagesNoPackage.properties", "MessagesNoPackage_ja.properties"), actual);
  }

  @Test
  void findInPackageMissing() {
    URI uri = URI.create("file:/SearchFilterTest/misssing");
    NoSuchPackageException noSuchPackageException =
        assertThrows(
            NoSuchPackageException.class, () -> ProcessorFiler.findInPackage(uri, alwaysTrue()));
    assertThat(noSuchPackageException.getMessage()).isEqualTo("/SearchFilterTest/misssing");
  }

  @Test
  void getParentFile() {
    assertEquals(
        URI.create("file:/com/pinterest/l10nmessages/example/"),
        ProcessorFiler.getParent(
            URI.create("file:/com/pinterest/l10nmessages/example/File1.properties"),
            "File1.properties"));
  }

  @Test
  void getParentJar() {
    assertEquals(
        URI.create("jar:file:/test-jar-properties.jar!/com/pinterest/l10nmessages/testjar/"),
        ProcessorFiler.getParent(
            URI.create(
                "jar:file:/test-jar-properties.jar!/com/pinterest/l10nmessages/testjar/Strings.properties"),
            "Strings.properties"));
  }

  private static Predicate<Path> alwaysTrue() {
    return path -> true;
  }
}
