---
title: Maven
---

`Maven` has two different ways to set up annotation processors:

- Default discovery process
- Explicit configuration (recommended)

Regardless, the main underlying steps are:

- Add the runtime dependency
- Configure the annotation processor

:::warning

While it is possible to use the annotation processor with Maven projects, there are some limitations
that can lead to **false positive builds** or making the developer experience cumbersome. Be aware
of them and see
[details and workarounds](#be-aware-of-the-annotation-processor-limitations-with-maven) below.

Consider using the [Maven plugin](#maven-plugin) as an alternative.

:::

## JDK only

### Default discovery process

Just add the annotation processor's library as "compile" dependency to use the default discovery
process. The processor will automatically be detected and the `properties` files will be made
accessible to the processor via the classpath. The runtime library is made available as a transitive
dependency.

```xml title=pom.xml
<dependencies>
  <dependency>
    <groupId>com.pinterest.l10nmessages</groupId>
    <artifactId>l10nmessages-proc</artifactId>
    <version>1.0.5</version>
  </dependency>
</dependencies>
```

:::note

The drawback of this approach is that the annotation processor is declared as `compile` dependency
which is not optimal (vs. only having the runtime library).

:::

### Explicit configuration of the annotation processor (recommanded)

Add a "compile" dependency on the runtime library and configure the annotation process in the
`maven-compiler-plugin`.

```xml title=pom.xml
<project>
  <dependencies>
    <dependency>
      <groupId>com.pinterest.l10nmessages</groupId>
      <artifactId>l10nmessages</artifactId>
      <version>1.0.5</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <annotationProcessorPaths>
            <dependency>
              <groupId>com.pinterest.l10nmessages</groupId>
              <artifactId>l10nmessages-proc</artifactId>
              <version>1.0.5</version>
            </dependency>
          </annotationProcessorPaths>
          <annotationProcessors>com.pinterest.l10nmessages.L10nPropertiesProcessor
          </annotationProcessors>
        </configuration>
        <groupId>org.apache.maven.plugins</groupId>
        <version>${maven.compiler.version}</version>
      </plugin>
    </plugins>
  </build>
</project>
```

## With ICU4J

Same as "JDK only" and add the `icu4j` as "compile" dependency.

```xml title=pom.xml
<project>
  <dependencies>
    <dependency>
      <groupId>com.pinterest.l10nmessages</groupId>
      <artifactId>l10nmessages</artifactId>
      <version>1.0.5</version>
    </dependency>
    <dependency>
      <groupId>com.ibm.icu</groupId>
      <artifactId>icu4j</artifactId>
      <version>72.1</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <annotationProcessorPaths>
            <dependency>
              <groupId>com.pinterest.l10nmessages</groupId>
              <artifactId>l10nmessages-proc</artifactId>
              <version>1.0.5</version>
            </dependency>
          </annotationProcessorPaths>
          <annotationProcessors>com.pinterest.l10nmessages.L10nPropertiesProcessor
          </annotationProcessors>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

## Be aware of the annotation processor limitations with Maven

With Maven (and contrarily to Gradle), it is not possible to register resources as "compile"
dependencies.

Because of that, when a registered `properties` file changes, it is not automatically re-processed
by the annotation processor during the next compilation. To be re-processed, the class that
registers the `properties` file using the `@L10nProperties` must be re-compiled. And for that class
to be re-compiled, it has to be either changed or a full clean compilation performed.

:::warning

To avoid staled `enum` and invalid messages, force a full clean compilation with:
`mvn clean compile`

Consider using the [Maven plugin](#maven-plugin) too.
:::

The following example shows the issue. It starts with an initial successful compilation. Then, the
`properties` file (and only that file) is changed. The following compilation still succeeds even
though we'd expect it to fail as the `welcome_user` message is broken. We'd also expect the `enum`
to be re-generated and re-compiled to reflect the `bye_up` key change but instead it is staled.

```shell
$ cat src/main/resources/com/pinterest/l10nmessages/example/Messages.properties
welcome_user=Welcome {username}!
bye=Bye

$ mvn clean compile
...
[INFO] BUILD SUCCESS

# only change the properties file
$ cat src/main/resources/com/pinterest/l10nmessages/example/Messages.properties
welcome_user=Welcome {usernam!
bye_up=Bye

$ nvm compile
...
[INFO] Nothing to compile - all classes are up to date
[INFO] BUILD SUCCESS

$ mvn clean compile
...
[ERROR] BUILD FAILURE
```

## Maven plugin

The Maven plugin: `l10nmessages-mvn-plugin` addresses the
[limitations of the annotation processor](#be-aware-of-the-annotation-processor-limitations-with-maven)
and can be used as a replacement.

To register `properties` files for processing, use `l10nPropertiesList` element of the plugin
configuration. The same options as with `@L10nProperties` can be provided.

```xml

<configuration>
  <l10nPropertiesList>
    <l10nProperties>
      <baseName>com.pinterest.l10nmessages.Messages</baseName>
    </l10nProperties>
    <l10nProperties>
      <baseName>com.pinterest.l10nmessages.OtherMessages</baseName>
      <messageFormatValidationTargets>ROOT</messageFormatValidationTargets>
    </l10nProperties>
  </l10nPropertiesList>
</configuration>
```

Runtime dependencies are like with the annotation processor. Only the plugin
`l10nmessages-mvn-plugin` replaces the annotation processor's configuration. If using `icu4j` it
needs to be provided as `compile` dependency and as `plugin` dependency

The full configuration looks like:

```xml title=pom.xml
<project>
  <dependencies>
    <dependency>
      <groupId>com.pinterest.l10nmessages</groupId>
      <artifactId>l10nmessages</artifactId>
      <version>1.0.5</version>
    </dependency>
    <dependency>
      <groupId>com.ibm.icu</groupId>
      <artifactId>icu4j</artifactId>
      <version>72.1</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>com.pinterest.l10nmessages</groupId>
        <artifactId>l10nmessages-mvn-plugin</artifactId>
        <version>1.0.5</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <l10nPropertiesList>
            <l10nProperties>
              <baseName>com.pinterest.l10nmessages.example.Messages</baseName>
            </l10nProperties>
          </l10nPropertiesList>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>com.ibm.icu</groupId>
            <artifactId>icu4j</artifactId>
            <version>72.1</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
</project>
```

With the plugin now setup, the `enum`s generation and the message validation is done by default
during the `generate-sources` phase. Any change to the `properties` files will be processed.

```shell
$ mvn generate-sources
```

:::caution

It is possible to use both the annotation processor and the Maven plugin at the same time. But when
doing so, make sure that the `properties` files are only registered once else it will generate
duplicated `enum`s that will break the compilation.
:::

## Development / Watcher

During development, it can become cumbersome to manually call `mvn generate-resources` (or
`mvn clean compile` with the anntation processor) every time a new string is added to the
`properties` file.

Use a watcher to automate the re-generation. For example on Mac OSX:

```shell
brew install fswatch
fswatch -e ".*" -i "\\.properties$" src/ | xargs -n1 -I{} mvn generate-sources
```

It will also perfectly integrate with IDEs. Choose between executing `mvn generate-sources` or
`mvn compile` depending if you want to just generate the `enum` file or if you also want to compile
it. It is sometimes preferable to delegate the compilation to the IDE instead).
