# Bazel

Assume the project parent is built and the jar is available in the `target` directory. Used a local
repository to depend on the jar file

Build and run: `bazel clean; bazel build //:l10nbazel; bazel-bin/l10nbazel`
