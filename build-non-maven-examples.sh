set -e

mvn clean install

cd examples/bazel-local/
bazel clean; bazel build //:l10nbazel; bazel-bin/l10nbazel
cd -;

cd examples/bazel-icu/
bazel clean; bazel build //:l10nbazel; bazel-bin/l10nbazel
cd -;

cd examples/gradle/
gradle clean build run
cd -;

cd examples/gradle-icu/
gradle clean build run
cd -;

cd examples/javac/
./build.sh
./build-steps.sh
cd -;



