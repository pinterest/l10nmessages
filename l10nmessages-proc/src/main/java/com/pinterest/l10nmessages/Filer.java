package com.pinterest.l10nmessages;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import javax.lang.model.element.Element;
import javax.tools.FileObject;

public interface Filer {

  URI writeSourceFile(String content, NameParts nameParts, Element... originatingElements);

  Optional<FileObject> findInputResource(NameParts nameParts);

  InputStream getInputResourceInputStream(NameParts nameParts);

  URI getInputResourcePackage(NameParts nameParts);

  boolean sameSourceFileExists(String enumFileContent, NameParts nameParts);
}
