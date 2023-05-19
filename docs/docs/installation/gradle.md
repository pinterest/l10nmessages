---
title: Gradle
---

For `Gradle` the main steps are:

- Add the runtime dependency with `implementation()`
- Configure the annotation processor using `annotationProcessor()`
- Register `properties` files with the annotation processor to re-process files on changes

## JDK only

The library is added for both runtime (`implementation`) and annotation processing
`annotationProcessor`.

The runtime library is added using `implementation` entry the the annotation processor with
`annotationProcessor`

```kotlin title=build.gradle.kts
dependencies {
    implementation("com.pinterest.l10nmessages:l10nmessages:1.0.3")
    annotationProcessor("com.pinterest.l10nmessages:l10nmessages-proc:1.0.3")
    annotationProcessor(files("src/main/resources/**/*.properties"))
}
```

## With ICU4J

Same as "JDK only" and add the `icu4j` dependency to both the annotation processor and runtime
dependencies

```kotlin title=build.gradle.kts
dependencies {
    implementation("com.pinterest.l10nmessages:l10nmessages:1.0.3")
    implementation("com.ibm.icu:icu4j:72.1")

    annotationProcessor("com.pinterest.l10nmessages:l10nmessages-proc:1.0.3")
    annotationProcessor("com.ibm.icu:icu4j:72.1")
    annotationProcessor(files("src/main/resources/**/*.properties"))
}
```
