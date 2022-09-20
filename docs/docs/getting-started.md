---
title: Getting Started
---

See the [installation](installation) guide for your build system or start with
[existing examples](examples).

This guide goes through all the steps required to render a localized message in a Java application
starting from scratch.

## Create a resource bundle

Create a root file: `Messages.properties` in the `com.pinterest.l10nmessages.example` package.

The corresponding resource bundle `baseName` is `com.pinterest.l10nmessages.example.Messages`.

`UTF-8` is the recommended encoding for the `properties` files.

In a project that follows the Maven layout, the file would be:

```properties title="src/resources/java/com/pinterest/l10nmessages/example/Messages.properties"
welcome_user=Welcome {username}!
```

## Register the resource bundle with `@L10nProperties`

Add the `@L10nProperties` annotation to the application class to register the resource bundle with
the annotation processor.

```java title="src/main/java/com/pinterest/l10nmessages/example/Application.java"
import com.pinterest.l10nmessages.L10nProperties;

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

}
```

## Enum generated by the annotation processor

Compile your project. The annotation processor should generate the following `enum`:

```java title="target/generated-sources/annotations/com/pinterest/l10nmessages/example/Messages.java"
package com.pinterest.l10nmessages.example;

public enum Messages {
  welcome_user("welcome_user");

  public static final String BASENAME = "com.pinterest.l10nmessages.example.Messages";
  // ...
}
```

## Strong typing using Enum

That `enum` can be used to create the `L10nMessages` instance and then to format a message using the
typed key: `Messages.welcome_user`. The argument: `username` is provided as a key/value pair.

```java title="src/main/java/com/pinterest/l10nmessages/example/Application.java"
import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

  public static void main(String[] args) {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    String localizedMsg = m.format(Messages.welcome_user, "username", "Mary");
    System.out.println(localizedMsg);
    // Welcome Mary!
  }
}
```

For extra typing, consider [argument names typing](fluent-api#argument-names-typing).

## Localization

Localize the root properties file by creating the file `Messages_fr.properties` for "French"

```properties
welcome_user=Bienvenue {username}!
```

Specify the locale wanted for the messages

```java

@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")
public class Application {

  public static void main(String[] args) {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class)
        .locale(Locale.FRENCH)
        .build();
    String localizedMsg = m.format(Messages.welcome_user, "username", "Mary");
    System.out.prinln(localizedMsg);
    // Bienvenue Mary!
  }
}
```

For advanced message formatting and localization, see the [ICU4J guide](icu4j).