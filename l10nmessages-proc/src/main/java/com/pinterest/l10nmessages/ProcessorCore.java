package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.PropertiesLoader.getPropertiesEntries;

import com.pinterest.l10nmessages.PropertiesLoader.PropertiesEntry;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class ProcessorCore {

  Filer filer;
  Reporter reporter;
  String generatedAnnotationValue;

  public ProcessorCore(Filer filer, Reporter reporter, String generatedAnnotationValue) {
    this.filer = filer;
    this.reporter = reporter;
    this.generatedAnnotationValue = generatedAnnotationValue;
  }

  public void process(List<L10nPropertiesWithElement> l10nPropertiesWithElements) {
    if (l10nPropertiesWithElements.isEmpty()) {
      reporter.debug("No @L10nProperties found");
    } else {
      l10nPropertiesWithElements.forEach(this::process);
    }
  }

  void process(L10nPropertiesWithElement l10nPropertiesWithElement) {
    reporter.debug(
        "Processing @L10nProperties: " + l10nPropertiesWithElement.getNameParts().getBaseName());
    if (filer.findInputResource(l10nPropertiesWithElement.getNameParts()).isPresent()) {
      generateEnum(l10nPropertiesWithElement);
      validateMessageFormats(l10nPropertiesWithElement);
    } else {
      reporter.errorf(
          l10nPropertiesWithElement.getElement(),
          "Found @L10nProperties with baseName: \"%s\" but required matching resource: \"%s\" is missing. \nMake sure that:"
              + "\n - the baseName in @L10nProperties follows the pattern: \"{package}.{filenameWithoutExtension}\""
              + "\n - the .properties file is accessible as resource in the proper package",
          l10nPropertiesWithElement.getNameParts().getBaseName(),
          l10nPropertiesWithElement.getNameParts().getRootPropertiesPath());
    }
  }

  void generateEnum(L10nPropertiesWithElement l10nPropertiesWithElement) {

    NameParts nameParts = l10nPropertiesWithElement.getNameParts();

    List<PropertiesEntry> entries =
        getPropertiesEntries(filer.getInputResourceInputStream(nameParts));

    LinkedHashMap<String, String> deduplicatedWithReporting =
        deduplicatedWithReporting(
            entries,
            l10nPropertiesWithElement.getToJavaIdentifiers(),
            l10nPropertiesWithElement.getOnDuplicatedKeys(),
            nameParts.getRootPropertiesPath());

    String enumFileContent =
        L10nPropertiesEnumGenerator.toEnum(
            deduplicatedWithReporting,
            nameParts,
            l10nPropertiesWithElement.getToJavaIdentifiers(),
            l10nPropertiesWithElement.getMessageFormatAdapterProviders(),
            generatedAnnotationValue,
            l10nPropertiesWithElement.getEnumType());

    checkNoConflictWithBasenameAndCoreClass(nameParts);

    if (filer.sameSourceFileExists(enumFileContent, nameParts)) {
      reporter.debugf("Enum for: %1$s has not changed", nameParts.getBaseName());
    } else {
      URI sourceFileURI =
          filer.writeSourceFile(enumFileContent, nameParts, l10nPropertiesWithElement.getElement());

      reporter.infof("Enum generated for: %1$s, uri: %2$s", nameParts.getBaseName(), sourceFileURI);
    }
  }

  void validateMessageFormats(L10nPropertiesWithElement l10nPropertiesWithElement) {
    Arrays.stream(l10nPropertiesWithElement.getMessageFormatValidationTargets())
        .flatMap(
            validationTarget ->
                getPropertiesRelativeNamesToCheck(validationTarget, l10nPropertiesWithElement))
        .forEach(
            relativeName -> {
              try {
                NameParts localizedName =
                    NameParts.fromPropertiesPath(
                        l10nPropertiesWithElement.getNameParts().resolveInPackage(relativeName));
                getPropertiesEntries(filer.getInputResourceInputStream(localizedName))
                    .forEach(
                        e -> {
                          try {
                            l10nPropertiesWithElement
                                .getMessageFormatAdapterProviders()
                                .get(e.getValue(), Locale.ROOT);
                          } catch (Throwable t) {
                            reporter.errorf(
                                "%1$s Invalid message format (fix or configure checks with @L10nProperties(messageFormatValidationTargets={})\nkey:     '%2$s'\nmessage: '%3$s'",
                                localizedName.getRootPropertiesPath(), e.getKey(), e.getValue());
                          }
                        });
              } catch (PropertiesLoaderException ple) {
                reporter.infof("Skip validation of: %1$s, since it can't be loaded", relativeName);
              }
            });
  }

  Stream<String> getPropertiesRelativeNamesToCheck(
      MessageFormatValidationTarget validationTarget,
      L10nPropertiesWithElement l10nPropertiesWithElement) {
    switch (validationTarget) {
      case ROOT:
        return Stream.of(l10nPropertiesWithElement.getNameParts().getRootPropertiesFilename());
      case LOCALIZED:
        URI packageUri = filer.getInputResourcePackage(l10nPropertiesWithElement.getNameParts());
        return AbstractFiler.findInPackage(
            packageUri,
            getPropertiesMatcher(l10nPropertiesWithElement.getNameParts().getClassName()))
            .stream();
      default:
        return Stream.of();
    }
  }

  static void checkNoConflictWithBasenameAndCoreClass(NameParts nameParts) {
    if (L10nMessages.class.getName().equals(nameParts.getFullyQualifiedEnumName())) {
      throw new IllegalArgumentException(
          "Can't use basename: "
              + nameParts.getFullyQualifiedEnumName()
              + " as it is the same as the core class.");
    }
  }

  LinkedHashMap<String, String> deduplicatedWithReporting(
      List<PropertiesEntry> entries,
      ToJavaIdentifier toJavaIdentifier,
      OnDuplicatedKeys onDuplicatedKey,
      String rootPropertiesPath) {
    return entries.stream()
        .filter(
            // de-duplicate entries based on the key. Report duplicates based on the strategy
            Predicates.distinctByKey(
                PropertiesEntry::getKey,
                PropertiesEntry::getValue,
                (key, value, previousValue) -> {
                  try {
                    onDuplicatedKey.apply(rootPropertiesPath, key, value, previousValue);
                  } catch (OnDuplicatedKeyException e) {
                    reporter.error(e.getMessage());
                  }
                }))
        .filter(
            // de-duplicate entries based on the enum value corresponding to the key. Report
            // duplicates
            Predicates.distinctByKey(
                e -> toJavaIdentifier.convert(e.getKey()),
                PropertiesEntry::getKey,
                (key, value, previousValue) ->
                    reporter.errorf(
                        "Generated enum value '%1$s' is duplicated. Rename key '%2$s' or key '%3$s' in '%4$s' to avoid this collision. Alternatively, use ToJavaIdentifiers#ESCAPING_ONLY",
                        key, value, previousValue, rootPropertiesPath)))
        .collect(
            Collectors.toMap(
                PropertiesEntry::getKey,
                PropertiesEntry::getValue,
                (s, s2) -> s,
                LinkedHashMap::new));
  }

  LinkedHashSet<String> deduplicatedKeysWithReporting(
      List<PropertiesEntry> entries,
      ToJavaIdentifier toJavaIdentifier,
      OnDuplicatedKeys onDuplicatedKey,
      String rootPropertiesPath) {

    return deduplicatedWithReporting(entries, toJavaIdentifier, onDuplicatedKey, rootPropertiesPath)
        .entrySet().stream()
        .map(Entry::getKey)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  static Predicate<Path> getPropertiesMatcher(String filename) {
    Pattern pattern = Pattern.compile(filename + "_.*?\\.properties");
    return (p) -> pattern.matcher(p.getFileName().toString()).matches();
  }

  /**
   * The {@link L10nProperties} annotation with the element that was annotated. Keep them together
   * to write messages with the location of the annotation using {@link
   * javax.annotation.processing.Messager#printMessage(Kind, CharSequence, Element)}
   */
  public static final class L10nPropertiesWithElement {

    private final String baseName;
    private final MessageFormatValidationTarget[] messageFormatValidationTargets;
    private final MessageFormatAdapterProviders messageFormatAdapterProviders;
    private final OnDuplicatedKeys onDuplicatedKeys;
    private final ToJavaIdentifiers toJavaIdentifiers;
    private final EnumType enumType;
    private final Element element;
    private final NameParts nameParts;

    public L10nPropertiesWithElement(
        String baseName,
        MessageFormatValidationTarget[] messageFormatValidationTargets,
        MessageFormatAdapterProviders messageFormatAdapterProviders,
        OnDuplicatedKeys onDuplicatedKeys,
        ToJavaIdentifiers toJavaIdentifiers,
        EnumType enumType,
        Element element) {

      this.baseName = Objects.requireNonNull(baseName);
      this.messageFormatValidationTargets = Objects.requireNonNull(messageFormatValidationTargets);
      this.messageFormatAdapterProviders = Objects.requireNonNull(messageFormatAdapterProviders);
      this.onDuplicatedKeys = Objects.requireNonNull(onDuplicatedKeys);
      this.toJavaIdentifiers = Objects.requireNonNull(toJavaIdentifiers);
      this.enumType = Objects.requireNonNull(enumType);
      this.element = element;
      this.nameParts = NameParts.fromBaseName(baseName);
    }

    public String getBaseName() {
      return baseName;
    }

    MessageFormatValidationTarget[] getMessageFormatValidationTargets() {
      return messageFormatValidationTargets;
    }

    MessageFormatAdapterProviders getMessageFormatAdapterProviders() {
      return messageFormatAdapterProviders;
    }

    OnDuplicatedKeys getOnDuplicatedKeys() {
      return onDuplicatedKeys;
    }

    ToJavaIdentifiers getToJavaIdentifiers() {
      return toJavaIdentifiers;
    }

    public EnumType getEnumType() {
      return enumType;
    }

    Element getElement() {
      return element;
    }

    NameParts getNameParts() {
      return nameParts;
    }
  }
}
