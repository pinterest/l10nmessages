package com.pinterest.l10nmessages;

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.testing.compile.JavaFileObjects;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.jupiter.api.Test;

public class JarScanningTest {

  @Test
  void getResourcePackageJar() throws IOException {
    // if run/debug in intellij, this won't read from the jar, so not testing the right path
    // Run: mvn clean test -Dtest=SearchFilerTest#getResourcePackageJar instead
    URL url =
        JarScanningTest.class.getResource(
            "/com/pinterest/l10nmessages/tests/m1/Strings.properties");
    JavaFileObject javaFileObject = JavaFileObjects.forResource(url);

    Filer mockFiler = mock(Filer.class);
    when(mockFiler.getResource(
        StandardLocation.ANNOTATION_PROCESSOR_PATH,
        "com.pinterest.l10nmessages.tests.m1",
        "Strings.properties"))
        .thenReturn(javaFileObject);

    ProcessorFiler ProcessorFiler = new ProcessorFiler(mockFiler, null);

    URI actual =
        ProcessorFiler.getInputResourcePackage(
            NameParts.fromPropertiesPath("/com/pinterest/l10nmessages/tests/m1/Strings.properties"));

    assertThat(javaFileObject.toUri().toString().replaceAll("Strings.properties$", ""))
        .isEqualTo(actual.toString());
  }

  @Test
  void findInPackageJar() throws URISyntaxException, MalformedURLException {
    URL url = getJarUrl("/com/pinterest/l10nmessages/tests/m1");
    List<String> actual =
        ProcessorFiler.findInPackage(url.toURI(), alwaysTrue()).stream()
            .sorted()
            .collect(Collectors.toList());
    assertEquals(
        Arrays.asList("Strings.properties", "Strings_fr.properties", "Strings_ja.properties"),
        actual);
  }

  @Test
  void findInPackageJarFileSystemAlreadyOpen() throws URISyntaxException, IOException {
    URL url = getJarUrl("/com/pinterest/l10nmessages/tests/m1");
    FileSystems.newFileSystem(url.toURI(), Collections.emptyMap());
    List<String> actual =
        ProcessorFiler.findInPackage(url.toURI(), alwaysTrue()).stream()
            .sorted()
            .collect(Collectors.toList());
    assertEquals(
        Arrays.asList("Strings.properties", "Strings_fr.properties", "Strings_ja.properties"),
        actual);
  }

  private static URL getJarUrl(String resourceName) throws MalformedURLException {
    URL url = JarScanningTest.class.getResource(resourceName);
    if (!url.toString().contains("jar:file")) {
      // Previous call may not give the jar url, eg. when debugging in Intellij, so
      // re-build the url explicitly. Update the SNAPSHOT version as needed as it may get outdated
      url =
          new URL(
              "jar:file:"
                  + Paths.get(
                  "../test-jar-properties/target/test-jar-properties-0.0.9-SNAPSHOT.jar")
                  .toAbsolutePath()
                  + "!/com/pinterest/l10nmessages/tests/m1");
    }
    return url;
  }
}
