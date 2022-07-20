package com.pinterest.l10nmessages.mojo;

import com.pinterest.l10nmessages.AbstractFiler;
import com.pinterest.l10nmessages.NameParts;
import com.pinterest.l10nmessages.ProcessorCore;
import com.pinterest.l10nmessages.ProcessorCore.L10nPropertiesWithElement;
import com.pinterest.l10nmessages.Reporter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class L10nMessagesMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${mojoExecution}", readonly = true, required = true)
  MojoExecution mojoExecution;

  @Parameter(
      defaultValue = "${project.build.directory}/generated-sources/l10nmessages",
      required = true)
  private File sourceOutputDirectory;

  @Parameter(
      defaultValue = "${project.build.directory}/generated-test-sources/l10nmessages",
      required = true)
  private File testOutputDirectory;

  @Parameter(defaultValue = "${project.basedir}/src/main/resources", required = true)
  private File mainResourceDirectory;

  @Parameter(defaultValue = "${project.basedir}/src/test/resources", required = true)
  private File testResourceDirectory;

  @Parameter(defaultValue = "${project.build.sourceEncoding}", required = true)
  private String sourceEncoding;

  @Parameter(property = "l10nPropertiesList")
  List<L10nProperties> l10nPropertiesList;

  private Reporter mojoReporter;
  private File outputDirectory;
  private File resourceDirectory;

  @Override
  public void execute() {
    mojoReporter = new MojoReporter();
    mojoReporter.debug("Execute L10nMessagesMojo...");

    if (mojoExecution.getLifecyclePhase() == null
        || "generate-sources".equals(mojoExecution.getLifecyclePhase())) {
      project.addCompileSourceRoot(sourceOutputDirectory.toString());
      outputDirectory = sourceOutputDirectory;
      resourceDirectory = mainResourceDirectory;
    } else if ("generate-test-sources".equals(mojoExecution.getLifecyclePhase())) {
      project.addTestCompileSourceRoot(testOutputDirectory.toString());
      outputDirectory = testOutputDirectory;
      resourceDirectory = testResourceDirectory;
    }

    MojoFiler mojoFiler = new MojoFiler(resourceDirectory);
    ProcessorCore processorCore = new ProcessorCore(mojoFiler, mojoReporter, getClass().getName());

    List<L10nPropertiesWithElement> l10nPropertiesWithElements =
        l10nPropertiesList.stream()
            .map(L10nMessagesMojo::convertToL10nPropertiesWithElement)
            .collect(Collectors.toList());

    processorCore.process(l10nPropertiesWithElements);
  }

  static L10nPropertiesWithElement convertToL10nPropertiesWithElement(
      L10nProperties l10NProperties) {
    return new L10nPropertiesWithElement(
        l10NProperties.getBaseName(),
        l10NProperties.getMessageFormatValidationTargets(),
        l10NProperties.getMessageFormatAdapterProviders(),
        l10NProperties.getOnDuplicatedKeys(),
        l10NProperties.getToJavaIdentifiers(),
        l10NProperties.getEnumType(),
        null);
  }

  class MojoReporter implements Reporter {

    @Override
    public void debug(String message) {
      getLog().debug(message);
    }

    @Override
    public void info(String message) {
      getLog().info(message);
    }

    @Override
    public void error(String message) {
      getLog().error(message);
      throw new RuntimeException(message);
    }
  }

  class MojoFiler extends AbstractFiler {

    public MojoFiler(File resourceDirectory) {
      super(resourceDirectory.toPath());
    }

    @Override
    public URI writeSourceFile(
        String content, NameParts nameParts, Element... originatingElements) {

      try {
        Path outputPath =
            outputDirectory
                .toPath()
                .resolve(nameParts.resolveInPackage(nameParts.getEnumName() + ".java"));
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, content.getBytes(Charset.forName(sourceEncoding)));
        return outputPath.toUri();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public Optional<FileObject> findInputResource(NameParts nameParts) {
      Optional<FileObject> fileObjectFromLocalFileSystem = super.findInputResource(nameParts);

      if (!fileObjectFromLocalFileSystem.isPresent()) {
        URL resource = getClass().getClassLoader().getResource(nameParts.getRootPropertiesPath());
        fileObjectFromLocalFileSystem = Optional.ofNullable(resource).map(UrlFileObject::new);
      }
      return fileObjectFromLocalFileSystem;
    }

    @Override
    public boolean sameSourceFileExists(String enumFileContent, NameParts nameParts) {
      boolean sameSourceFileExists = false;
      try {
        Path outputPath =
            outputDirectory
                .toPath()
                .resolve(nameParts.resolveInPackage(nameParts.getEnumName() + ".java"));

        if (outputPath.toFile().isFile()) {
          String fileContent =
              new String(Files.readAllBytes(outputPath), Charset.forName(sourceEncoding));
          sameSourceFileExists = fileContent.equals(enumFileContent);
        }
      } catch (Throwable t) {
        sameSourceFileExists = false;
      }
      return sameSourceFileExists;
    }

    private class UrlFileObject implements FileObject {

      private final URL resource;

      public UrlFileObject(URL resource) {
        this.resource = resource;
      }

      @Override
      public URI toUri() {
        try {
          return resource.toURI();
        } catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public String getName() {
        throw new UnsupportedOperationException();
      }

      @Override
      public InputStream openInputStream() throws IOException {
        return resource.openStream();
      }

      @Override
      public OutputStream openOutputStream() throws IOException {
        throw new UnsupportedOperationException();
      }

      @Override
      public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        throw new UnsupportedOperationException();
      }

      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        throw new UnsupportedOperationException();
      }

      @Override
      public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException();
      }

      @Override
      public long getLastModified() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean delete() {
        throw new UnsupportedOperationException();
      }
    }
  }
}
