# create build output directory
rm -rf output; mkdir output

# processor needs access to resources, we also need the jar in the classpath to process
# the Application file
rm -rf outputproc; mkdir outputproc
javac -d outputproc -proc:only \
  -processorpath "src/main/resources:../../l10nmessages-proc/target/l10nmessages-proc-1.0.1-SNAPSHOT.jar:../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  -processor "com.pinterest.l10nmessages.L10nPropertiesProcessor" \
  -classpath "../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  src/main/java/com/pinterest/l10nmessages/example/Application.java

# Need the jar in the classpath and the generated enum to compile the Application
javac -d output -proc:none -classpath "outputproc:../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  src/main/java/com/pinterest/l10nmessages/example/Application.java

# We need the jar file and the resources at runtime in addition to the compiled application
java -cp "src/main/resources/:output:../../l10nmessages/target/l10nmessages-1.0.1-SNAPSHOT.jar" \
  com.pinterest.l10nmessages.example.Application


