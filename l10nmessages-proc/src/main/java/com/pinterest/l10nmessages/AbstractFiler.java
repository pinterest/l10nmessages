package com.pinterest.l10nmessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.tools.FileObject;

/**
 * Filer that works with the local file system. This is meant to be extended by the annotation
 * processor filer and the plugin filer to read from their respective specific locations.
 */
public abstract class AbstractFiler implements Filer {

  Path localFileSystemInputLocation;

  public AbstractFiler(Path localFileSystemInputLocation) {
    this.localFileSystemInputLocation = localFileSystemInputLocation;
  }

  @Override
  public InputStream getInputResourceInputStream(NameParts nameParts) {
    FileObject resource = getInputResource(nameParts);

    try {
      return resource.openInputStream();
    } catch (Throwable t) {
      throw new RuntimeException(
          "Can't open InputStream for resource with package: "
              + nameParts.getDotPackageName()
              + " and relativeName: "
              + nameParts.getBaseName(),
          t);
    }
  }

  @Override
  public URI getInputResourcePackage(NameParts nameParts) {
    return getParent(getInputResource(nameParts).toUri(), nameParts.getRootPropertiesFilename());
  }

  @Override
  public Optional<FileObject> findInputResource(NameParts nameParts) {
    return Optional.of(localFileSystemInputLocation.resolve(nameParts.getRootPropertiesPath()))
        .filter(Files::exists)
        .map(p -> new FileObjectFromLocalFileSystem(p));
  }

  static URI getParent(URI uri, String relativeName) {
    String uriStr = uri.toString();
    return URI.create(uriStr.substring(0, uriStr.length() - relativeName.length()));
  }

  public static List<String> findInPackage(URI uri, Predicate<Path> pathMatcher) {
    Objects.requireNonNull(uri, "uri is null");
    Objects.requireNonNull(pathMatcher, "pathMatcher is null");

    FileSystem fileSystem = null;
    try {
      if ("jar".equals(uri.getScheme())) {
        try {
          fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException e) {
          fileSystem = null;
        }
      }
      Path path = Paths.get(uri);
      List<String> relativeNames =
          Files.find(
                  path,
                  1,
                  (p, basicFileAttributes) ->
                      basicFileAttributes.isRegularFile() && pathMatcher.test(p.getFileName()))
              .map(Path::getFileName)
              .map(Path::toString)
              .collect(Collectors.toList());
      return relativeNames;
    } catch (NoSuchFileException nsfe) {
      throw new NoSuchPackageException(nsfe.getMessage());
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } finally {
      if (fileSystem != null) {
        try {
          fileSystem.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  FileObject getInputResource(NameParts nameParts) {
    return findInputResource(nameParts)
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Can't find resource for package: "
                        + nameParts.getDotPackageName()
                        + " and relativeName: "
                        + nameParts.getRootPropertiesFilename()));
  }

  static class FileObjectFromLocalFileSystem implements FileObject {

    private final Path p;

    public FileObjectFromLocalFileSystem(Path p) {
      this.p = p;
    }

    @Override
    public URI toUri() {
      return p.toUri();
    }

    @Override
    public String getName() {
      return p.toString();
    }

    @Override
    public InputStream openInputStream() throws IOException {
      return Files.newInputStream(p);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
      return Files.newOutputStream(p);
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

  static class NoSuchPackageException extends RuntimeException {

    public NoSuchPackageException(String message) {
      super(message);
    }
  }
}
