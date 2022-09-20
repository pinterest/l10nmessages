
## Run spotless / prettier

From root:

```shell
mvn spotless:apply
```

From documentation folder:

Be careful as it breaks some Admonitions, diff must be reviewed carefully

```shell
yarn format
```

Note: removed "sortPom" configuration because it doesn't respect line size. And runing prettier
from yarn vs. Maven because of issue with nested XML, etc.

### Update dependencies

To update Maven dependencies:

```
mvn versions:update-properties
mvn versions:use-next-versions
```

To update ICU4J, think to find/replace in all the code since bazel/gradle example won't be updated
by the Maven command

## Annotation processors

Passing annotation option with `javac`, just use the `-A`. This passes the extra path to scan to
find the properties files.

```shell
$ javac -d outputproc -proc:only \
  -processorpath "../../l10nmessages-proc/target/l10nmessages-proc-1.0.1-SNAPSHOT.jar:../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  -processor "com.pinterest.l10nmessages.L10nPropertiesProcessor" \
  -classpath "../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  -Adebug \
  -Al10nPropertiesProcessor.fallbackInputLocation="src/main/resources" \
  src/main/java/com/pinterest/l10nmessages/example/Application.java
```

To show debug messages in Maven, need to show warnings

```xml

<annotationProcessorPaths>
  <dependency>
    <groupId>com.pinterest.l10nmessages</groupId>
    <artifactId>l10nmessages-proc</artifactId>
    <version>1.0.1-SNAPSHOT</version>
  </dependency>
</annotationProcessorPaths>
<annotationProcessors>com.pinterest.l10nmessages.L10nPropertiesProcessor
</annotationProcessors>
<showWarnings>true</showWarnings>
<compilerArgs>
  <compilerArg>-Adebug</compilerArg>
</compilerArgs>
```

VSCode if configured from a Maven project does run the annotation processor but the properties
are not accessible through the class pass contrarily to Maven build. This is causing build 
failure. To address that we add a way to scan other path, but it will have to be provided 
as param to maven which is not great.



For testing
```
javac -d outputproc -proc:only \
-processorpath "src/main/resources:/Users/jeanaurambault/.m2/repository/com/pinterest/l10nmessages/l10nmessages-proc/1.0.1-SNAPSHOT/l10nmessages-proc-1.0.1-SNAPSHOT.jar:/Users/jeanaurambault/.m2/repository/com/pinterest/l10nmessages/l10nmessages/1.0.1-SNAPSHOT/l10nmessages-1.0.1-SNAPSHOT.jar" \
-processor "com.pinterest.l10nmessages.L10nPropertiesProcessor" \
-classpath "/Users/jeanaurambault/.m2/repository/com/pinterest/l10nmessages/l10nmessages/1.0.1-SNAPSHOT/l10nmessages-1.0.1-SNAPSHOT.jar" \
src/main/java/com/example/App.java
```

## Watcher on Mac OSX

When using the Maven plugin

```
brew install fswatch
fswatch -e ".*" -i "\\.properties$" . | xargs -n1 -I{} mvn generate-sources
```

or if using the annotation processor

```
fswatch -e ".*" -i "\\.properties$" . | xargs -n1 -I{} mvn clean package
```