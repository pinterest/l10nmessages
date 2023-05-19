---
title: Installation
description: Java internationalization made easy and safe!
---

:::info

Java 8+ supported

Maven group id is `com.pinterest.l10nmessages` and artifact id `l10nmessages`. 

The latest version is `1.0.3`.

:::

Each build system: `Bazel`, `Gradle` and `Maven` has a slightly different setup, but the main steps
are:

- Set up the annotation processor
- Add the runtime library / fluent API
- Register `properties` files

It is recommended to use the annotation processor together with the runtime library but both can be
used independently. The documentation covers setting up both together.

## Annotation processor

The annotation processor: `com.pinterest.l10nmessages.L10nPropertiesProcessor` is used to generate
enums for strong typing and to perform compile time checks. It must have access to the properties
files through the annotation path or classpath which is build system dependent.

## Fluent API

The fluent API: `com.pinterest.l10nmessages.L10nMessages` is used at runtime to load and format
messages. Properties files are accessed through the classpath as defined by JDK / ICU resource
bundles.

This library has no transitive dependency and is meant to be lightweight.

## Register Properties files

Any change to the `properties` file should re-trigger the generation for the `enums` for strong
typing. `Bazel` and `Gradle` support this easily. `Maven` has
[some limitations](maven.md#be-aware-of-the-annotation-processor-limitations-with-maven) to be aware
of.
