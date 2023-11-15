---
title: Bazel
---

For `Bazel` main steps are:

- Add [external dependencies](#external-dependencies), for example using `rules_jvm_external`
- Register `properties` files using a `filegroup`
- Configure the annotation processor using a `java_plugin`
- Add a Java rule with `java_binary` to build the application

## JDK only

Create a `filegroup` to register the properties files that you wish to use

```python title=BUILD
filegroup(
    name="l10nresources",
    srcs=glob(["src/main/resources/**/*.properties"])
)
```

Use a `java_plugin` to configure the annotation processor that processes the registered properties
files

```python title=BUILD
java_plugin(
    name="l10nmessages_proc",
    processor_class="com.pinterest.l10nmessages.L10nPropertiesProcessor",
    deps=[
        "@maven//:com_pinterest_l10nmessages_l10nmessages_proc",
        "@maven//:com_pinterest_l10nmessages_l10nmessages"
    ],
    resources=["l10nresources"]
)
```

In `java_binary`, add the resources previously define in `resources`, register the annotation
processor in `plugins` and finally add a dependency on the library runtime in `deps`

```python title=BUILD
java_binary(
    name="l10nbazel",
    srcs=glob(["src/main/java/**/*.java"]),
    resources=["l10nresources"],
    plugins=["l10nmessages_proc"],
    deps=[
        "@maven//:com_pinterest_l10nmessages_l10nmessages"
    ],
    main_class="com.pinterest.l10nmessages.example.Application",
)
```

## With ICU4J

Same as "JDK only" and add the `icu4j` dependency to both the annotation processor and runtime
dependencies

```python title=BUILD

filegroup(
    name="l10nresources",
    srcs=glob(["src/main/resources/**/*.properties"])
)

java_plugin(
    name="l10nmessages_proc",
    processor_class="com.pinterest.l10nmessages.L10nPropertiesProcessor",
    deps=[
        "@maven//:com_pinterest_l10nmessages_l10nmessages_proc",
        "@maven//:com_pinterest_l10nmessages_l10nmessages",
        "@maven//:com_ibm_icu_icu4j"
    ],
    resources=["l10nresources"]
)

java_binary(
    name="l10nbazel",
    srcs=glob(["src/main/java/**/*.java"]),
    resources=["l10nresources"],
    plugins=["l10nmessages_proc"],
    deps=[
        "@maven//:com_pinterest_l10nmessages_l10nmessages",
        "@maven//:com_ibm_icu_icu4j"
    ],
    main_class="com.pinterest.l10nmessages.example.Application",
)
```

## External dependencies

Use [rules_jvm_external](https://github.com/bazelbuild/rules_jvm_external) to fetch external
dependencies from Maven.

```python WORKSPACE
RULES_JVM_EXTERNAL_TAG = "4.2"
RULES_JVM_EXTERNAL_SHA = "cd1a77b7b02e8e008439ca76fd34f5b07aecb8c752961f9640dea15e9e5ba1ca"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.ibm.icu:icu4j:72.1"
        "com.pinterest.l10nmessages:l10nmessages:1.0.4"
        "com.pinterest.l10nmessages:l10nmessages-proc:1.0.4"
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)
```
