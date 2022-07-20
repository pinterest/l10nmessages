---
title: Introduction
---

`L10nMessages` is a library that makes internationalization (i18n) and localization (l10n) of Java
applications easy and safe.

It provides a [fluent API](fluent-api) to load and format messages that is built on top of Java
standard libraries. In addition to simplifying the overall setup, it brings strong typing and
compile time checks to the mix when used with the [annotation processor](annotation-processor).

The library can be used standalone in which case it will rely on JDK message formatting capabilities
to which it adds named argument support.

It can also be used with [ICU4J](https://unicode-org.github.io/icu/userguide/icu4j/), the de-facto
standard library for internationalization in Java. The supported APIs, while similar to JDK's, are
more extensive and includes regular updates from Unicode. This is the recommended choice for better
message formatting and advanced internationalization in general.

Check the [Getting Started](getting-started) guide for a quick overview!

## Benefits over plain JDK / ICU4J:

- Easier to get started with the `@L10nProperties` annotation and with the fluent API:
  `L10nMessages`
  - No need to manually setup and manage:
    [resource bundles](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) ,
    [message formats](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html),
    caching, and more
- Strong typing of message keys, message arguments and resource bundle names
  - The annotation processor: `L10nPropertiesProcessor` generates an `Enum` of keys for each
    resource bundle registered
  - Use the enum to initialize `L10nMessages` and the enum keys in `L10nMessages#format()` to
    reference existing messages
  - Optional "argument builders" / `FormatContext` for messages with arguments to provide maximal
    typing
- Compile time checks
  - Missing `properties` file
  - Message formats validity (on root and localized files)
  - Duplicated keys in `properties` files
- For Java 8, try to load properties files as `UTF-8` and fallback to `ISO-8859-1`. This is the
  default behavior in Java 9+
- Simple caching. Easily integrates with other library like Guava cache.
- Share arguments between subsequent calls to `L10nMessages#format()` calls
- Graceful failure for invalid message formats and missing messages
- Runtime checks for missing arguments
- Prioritize use of ICU4J message format over JDK
- Simple named argument support for JDK

## Goals

### Lightweight

The runtime library aims at being minimal. It has no dependencies, and the goal is to keep it that
way in the future. The annotation processor is packaged in a different `jar` to keep the runtime
size minimal.

`L10nMessages` provides
[named arguments support with JDK MessageFormat](fluent-api#named-arguments-with-jdk-messageformat)
. If you can't or don't wish to integrate with ICU4J because of its size or dependencies but still
want named argumentss, this is an interesting trade off.

### For newcomers or seasoned developers

If you are new to Java or to Internationalization, you may not know where to start. Java has some
documentation, but it takes time to go through all of it and to figure out how to effectively use
the different libraries together
([resource bundles](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) ,
[message formats](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html), caching,
etc.).

If you are used to doing internationalization in Java, you already know that a lot is available out
of the box. Yet, you probably don't want to go through the pain of manually configuring and writing
the same glue over and over again. The fluent API, the addition of build time checks and strong
typing in `L10nMessages` is probably something you have been looking for.

### Quick to setup, easy to use and to follow best practicies

With `L10nMessages`, you should be able to be [up and running in a few minutes](getting-started),
for the main [Java build systems](installation). Some internationalization
[best practices with ICU4J](icu4j) are also documented.

### Safer that "plain" Java

Plain Java internationalization lacks in type safety and message format validation. Default error
handling will hard fail which might not be the best behavior for production systems. `L10nMessages`
privileges graceful fallbacks and [provides hooks to monitor failures](fluent-api#failure-handling).
