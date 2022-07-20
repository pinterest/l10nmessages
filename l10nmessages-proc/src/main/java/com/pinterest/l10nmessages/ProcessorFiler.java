package com.pinterest.l10nmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

/**
 * This class provides convenient methods to access build resources and to scan packages.
 *
 * <p>Different build systems and setups will lead to resources being accessible at different
 * locations (annotation processor path or classpath). This should be transparent to the annotation
 * processor itself.
 *
 * <p>The are also some inconsistencies with IDEs, leading to properties not being accessible. To
 * address this, a fallback input location can be provided. It will search for the properties file
 * in that location if it can't find a match in the standard locations.
 *
 * <p>Resources can be local files or packaged inside jar files and this class helps opening them.
 */
class ProcessorFiler extends AbstractFiler {

  private final Filer filer;

  public ProcessorFiler(Filer filer, Path fallbackInputLocation) {
    super(fallbackInputLocation);
    this.filer = Objects.requireNonNull(filer);
  }

  @Override
  public URI writeSourceFile(String content, NameParts nameParts, Element... originatingElements) {
    JavaFileObject sourceFile = null;

    try {
      sourceFile =
          filer.createSourceFile(nameParts.getFullyQualifiedEnumName(), originatingElements);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
      out.println(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return sourceFile.toUri();
  }

  @Override
  public boolean sameSourceFileExists(String enumFileContent, NameParts nameParts) {
    boolean sameSourceFileExists;
    try {
      FileObject resource =
          filer.getResource(
              StandardLocation.SOURCE_OUTPUT,
              nameParts.getDotPackageName(),
              nameParts.getRootPropertiesFilename());

      String fileContent = resource.getCharContent(true).toString();
      sameSourceFileExists = fileContent.equals(enumFileContent);
    } catch (Throwable t) {
      sameSourceFileExists = false;
    }
    return sameSourceFileExists;
  }

  @Override
  public Optional<FileObject> findInputResource(NameParts nameParts) {
    Optional<FileObject> resource =
        findStandardLocationInputResource(
            nameParts.getDotPackageName(), nameParts.getRootPropertiesFilename());

    if (!resource.isPresent() && localFileSystemInputLocation != null) {
      resource =
          Optional.of(localFileSystemInputLocation.resolve(nameParts.getRootPropertiesPath()))
              .filter(Files::exists)
              .map(p -> new FileObjectFromLocalFileSystem(p));
    }

    return resource;
  }

  Optional<FileObject> findStandardLocationInputResource(
      String dotPackageName, String relativeName) {
    return Stream.of(StandardLocation.ANNOTATION_PROCESSOR_PATH, StandardLocation.CLASS_PATH)
        .map(
            location -> {
              FileObject fileObject;
              try {
                fileObject = filer.getResource(location, dotPackageName, relativeName);
                try (InputStream is = fileObject.openInputStream()) {
                } catch (Throwable t) {
                  fileObject = null;
                }
              } catch (Throwable t) {
                fileObject = null;
              }
              return fileObject;
            })
        .filter(Objects::nonNull)
        .findFirst();
  }
}
