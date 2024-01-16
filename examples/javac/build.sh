# create build output directory
rm -rf output; mkdir output

# We need the properties file both at build time and runtime, copy them to the build output
cp -r src/main/resources/ output

# To build we need the jar (both for the annotation processor and for the regular compilation) and
# the properties files (for the annotation processor) in the classpath
CP="output:../../l10nmessages/target/l10nmessages-1.0.6-SNAPSHOT.jar:../../l10nmessages-proc/target/l10nmessages-proc-1.0.6-SNAPSHOT.jar"
javac -d output -cp $CP src/main/java/com/pinterest/l10nmessages/example/Application.java

# It is the same for runtime
java -cp $CP com.pinterest.l10nmessages.example.Application


