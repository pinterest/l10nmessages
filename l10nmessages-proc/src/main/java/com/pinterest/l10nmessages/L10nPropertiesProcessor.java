package com.pinterest.l10nmessages;

import com.google.auto.service.AutoService;
import com.pinterest.l10nmessages.ProcessorCore.L10nPropertiesWithElement;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({
  "com.pinterest.l10nmessages.L10nProperties",
  "com.pinterest.l10nmessages.L10nPropertiesList"
})
@SupportedOptions({"debug", L10nPropertiesProcessor.OPTION_FALLBACK_INPUT_LOCATION})
@AutoService(Processor.class)
public class L10nPropertiesProcessor extends AbstractProcessor {

  public static final String OPTION_FALLBACK_INPUT_LOCATION =
      "l10nPropertiesProcessor.fallbackInputLocation";

  AbstractFiler abstractFiler;

  MessagerReporter messagerReporter;

  ProcessorCore processorCore;

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    abstractFiler = new ProcessorFiler(processingEnv.getFiler(), getOptionFallbackInputLocation());
    messagerReporter =
        new MessagerReporter(
            processingEnv.getMessager(), processingEnv.getOptions().containsKey("debug"));
    processorCore = new ProcessorCore(abstractFiler, messagerReporter, getClass().getName());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    messagerReporter.debug("L10nPropertiesProcessor processing...");
    List<L10nPropertiesWithElement> l10nPropertiesWithElements =
        getL10nPropertiesToProcess(roundEnv);
    processorCore.process(l10nPropertiesWithElements);
    return true;
  }

  private Path getOptionFallbackInputLocation() {
    Path path = null;

    String option = processingEnv.getOptions().get(OPTION_FALLBACK_INPUT_LOCATION);
    if (option != null) {
      path = Paths.get(option);
    }
    return path;
  }

  private List<L10nPropertiesWithElement> getL10nPropertiesToProcess(RoundEnvironment roundEnv) {
    return Stream.concat(
            roundEnv.getElementsAnnotatedWith(L10nProperties.class).stream()
                .map(
                    element -> {
                      L10nProperties annotation = element.getAnnotation(L10nProperties.class);
                      return convertToL10nPropertiesWithElement(annotation, element);
                    }),
            roundEnv.getElementsAnnotatedWith(L10nPropertiesList.class).stream()
                .flatMap(
                    element ->
                        Arrays.stream(element.getAnnotation(L10nPropertiesList.class).value())
                            .map(
                                annatation ->
                                    convertToL10nPropertiesWithElement(annatation, element))))
        .filter(Objects::nonNull)
        .filter(Predicates.distinctByKey(e -> e.getBaseName()))
        .collect(Collectors.toList());
  }

  private L10nPropertiesWithElement convertToL10nPropertiesWithElement(
      L10nProperties annotation, Element element) {
    return new L10nPropertiesWithElement(
        annotation.baseName(),
        annotation.messageFormatValidationTargets(),
        annotation.messageFormatAdapterProviders(),
        annotation.onDuplicatedKeys(),
        annotation.toJavaIdentifiers(),
        annotation.enumType(),
        element);
  }
}
