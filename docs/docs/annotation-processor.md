---
title: Annotation Processor
---

The annotation processor will process all the resource bundles that are registered with the
`@L10nProperties` annotation.

For each resource bundle, the pre-processor:

- Generates an `enum` of the message keys contained in the source properties file. The enum can be
  used with `L10nMessages` for adding strong typing to "format" functions and to set the
  `MessageFormatAdapterProvider`.
- Checks for duplicates in keys. Can be configured with `onDuplicatedKeys`
- Checks the validity for the message formats. Files to be checked can be configured with
  `messageFormatValidationTargets`. By default, both root and localized files are checked.

## Register the resource bundle with `@L10nProperties`

Add the `@L10nProperties` annotation to any class to register a resource bundle with the annotation
processor. To understand how the `baseName` is defined, check
[Naming convention](resource-bundle.md#naming-convention).

```java

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

}

```

:::info

For Maven builds, the annotation processor can be replaced with a
[Maven plugin](installation/maven.md#maven-plugin). The resource bundle registration will be done in
the `pom.xml` file instead of using the annotation.

:::

## Enum generated by the annotation processor

During compilation, the annotation processor will generate an `enum` per bundle. The `enum` fully
qualified name corresponds to the resource bundle `baseName`. Make sure there are no conflicts with
existing classes.

The `enum` will look like:

```java
package com.pinterest.l10nmessages.example;

public enum Messages {
  welcome_user("welcome_user");

  public static final String BASENAME = "com.pinterest.l10nmessages.example.Messages";
  // ...
}
```

## Duplicated Keys

When writing `properties` file, nothing prevents to write two different messages with the same key.
The JDK will load the resource bundle without errors but that can lead to unexpected behavior in the
application.

To address this, the annotation processor checks for duplicated keys and will reject them by
default. This can be configured with `onDuplicatedKeys` to accept only when messages are the same,
or to always accept (back to JDK behavior).

## MessageFormat validation

A message is valid if it is possible to create a `MessageFormat` instance from it. The type of
`MessageFormat` can be changed with `messageFormatAdapterProviders` annotation attribute.

### ICU4J, JDK or JDK with named arguments

By default, the annotation processor
([as the fluent API](fluent-api.md#icu4j-jdk-or-jdk-with-named-arguments)) will use `ICU4J` if it is
available. If not, it will use the `JDK` extended with
[named arguments](fluent-api.md#named-arguments-with-jdk-messageformat) support.

Use `messageFormatAdapterProviders` to set a specific version of `MessageFormat` to use.

For example, to force strict `JDK` validation:

```java

@L10nProperties(
        baseName = "com.pinterest.l10nmessages.example.Messages",
        messageFormatAdapterProviders = MessageFormatAdapterProviders.JDK
)
public class Application {

}
```

### Validation targets

Use `messageFormatValidationTargets` to choose which properties files will be checked. By default,
both `root` and `localized` files are validated. To fully disable the checks use
`messageFormatValidationTargets={}`.

For example, to check only the root file:

```java

@L10nProperties(
        baseName = "com.pinterest.l10nmessages.example.Messages",
        messageFormatValidationTargets = {ROOT})
public class Application {

}
```

## Properties keys to Enum values

`Properties` keys must be turned into valid Java identifiers when generating the `enum` for strong
typing.

`ToJavaIdentifiers.ESCAPING_AND_UNDERSCORE` is the default conversion type. It aims at creating
shorter and more natural Java identifiers.

`.` is converted to `_`. If the first key character is invalid as identifier first character but
would be valid as a second character then `_` is prepended. Finally, all other invalid characters
are replaced with the following escape sequence `_u{codepoint}_`.

| Properties Key    | Java identifier   |
| ----------------- | ----------------- |
| dot.to.underscore | dot_to_underscore |
| 1_invalid_first   | \_1_invalid_first |
| with space        | with_u32_space    |
| collision.issue   | collision_issue   |
| collision_issue   | collision_issue   |
| class             | \_class           |

That conversion can lead to collisions which will make the `enum` compilation fail. In that case,
modify the keys accordingly. If that's not possible, use `ToJavaIdentifiers.ESCAPING_ONLY` instead.
All invalid characters will be replaced with the escape sequence. The drawback will be less natural
identifiers.

| Properties Key  | Java identifier     |
| --------------- | ------------------- |
| collision.issue | collision_u46_issue |