package com.pinterest.l10nmessages;

import static com.pinterest.l10nmessages.MessageFormatValidationTarget.LOCALIZED;
import static com.pinterest.l10nmessages.MessageFormatValidationTarget.ROOT;
import static com.pinterest.l10nmessages.OnDuplicatedKeys.REJECT;
import static com.pinterest.l10nmessages.ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register a resource bundle backed by properties files for pre-processing.
 *
 * <p>The pre-processor:
 *
 * <ul>
 *   <li>generates an enum of the message keys contained in the source properties file. The enum can
 *       be used with {@link L10nMessages} for adding strong typing to "format" functions.
 *       Optionally, argument builders can be generated with {@link #enumType()}.
 *   <li>checks for duplicates in keys and potentially in generated enum values. Can be configured
 *       with {@link #onDuplicatedKeys()}
 *   <li>checks the validity for the message formats. Files to be checked can be configured with
 *       {@link #messageFormatValidationTargets()})
 * </ul>
 *
 * <p>{@see https://docs.oracle.com/javase/tutorial/i18n/resbundle/propfile.html}
 */
@Repeatable(L10nPropertiesList.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface L10nProperties {

  /**
   * Resource bundle base name: fully-qualified class name as defined by {@link
   * java.util.ResourceBundle}
   *
   * @return the resource bundle base name
   */
  String baseName();

  /**
   * Indicates which properties files should have their message formats validated.
   *
   * @return Array of targets to be validated.
   */
  MessageFormatValidationTarget[] messageFormatValidationTargets() default {ROOT, LOCALIZED};

  /**
   * The message format adapter provider used to validate the message formats. It will also be used
   * by the fluent API when the builder is created from the generated enum {@link L10nMessages}
   *
   * @return the message format adapter provider
   */
  MessageFormatAdapterProviders messageFormatAdapterProviders() default
      MessageFormatAdapterProviders.AUTO;

  /**
   * Strategy to apply when duplicated keys are present in properties file.
   *
   * @return strategy to apply on duplicated keys
   */
  OnDuplicatedKeys onDuplicatedKeys() default REJECT;

  /**
   * Conversion applied to message keys and argument names to generate valid java identifiers for
   * inclusion as enum values and for the argument builders.
   *
   * <p>The default conversion {@link ToJavaIdentifiers#ESCAPING_AND_UNDERSCORE} may lead to
   * collision in the enum values (and argument names) and in turn lead to compilation failure. Also
   * see {@link ToJavaIdentifiers#ESCAPING_ONLY}.
   *
   * @return the conversion to be applied
   */
  ToJavaIdentifiers toJavaIdentifiers() default ESCAPING_AND_UNDERSCORE;

  /**
   * The type of "enum" to generate.
   *
   * <p>To control the content of the generated enum for strong typing.
   *
   * @return the type of enum to use
   */
  EnumType enumType() default EnumType.KEYS_ONLY;
}
