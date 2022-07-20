set -e

mvn clean
cd examples/bazel/; bazel clean; cd -
cd examples/bazel-local/; bazel clean; cd -
cd examples/bazel-icu/; bazel clean; cd -
cd examples/gradle/; gradle clean; cd -
cd examples/gradle-icu/; gradle clean; cd -
