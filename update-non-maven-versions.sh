#!/bin/sh

if [ $# -eq 0 ]; then
    echo "New version required"
    exit 1
fi
NEW_VERSION=$1

echo "Updating to ${NEW_VERSION}"

# We pattern matching for now, we could restrict to a specific version to avoid unexpected
# modification later
CURRENT_VERSION='[0-9]+\.[0-9]+\.[0-9]+(-SNAPSHOT)?'

# Misc
sed -E -i '' -e  "s/version>${CURRENT_VERSION}/version>${NEW_VERSION}/g" dev-notes.md;
sed -E -i '' -e  "s/${CURRENT_VERSION}\/l10nmessages/${NEW_VERSION}\/l10nmessages/g" dev-notes.md;
sed -E -i '' -e  "s/l10nmessages-${CURRENT_VERSION}/l10nmessages-${NEW_VERSION}/g" dev-notes.md;
sed -E -i '' -e  "s/l10nmessages-proc-${CURRENT_VERSION}/l10nmessages-proc-${NEW_VERSION}/g" dev-notes.md;

# Javac
sed -E -i '' -e  "s/l10nmessages-${CURRENT_VERSION}/l10nmessages-${NEW_VERSION}/g" examples/javac/build*.sh;
sed -E -i '' -e  "s/l10nmessages-proc-${CURRENT_VERSION}/l10nmessages-proc-${NEW_VERSION}/g" examples/javac/build*.sh;

# Gradle
sed -E -i '' -e  "s/l10nmessages:${CURRENT_VERSION}/l10nmessages:${NEW_VERSION}/g" examples/{gradle,gradle-icu}/app/build.gradle.kts ;
sed -E -i '' -e  "s/l10nmessages-proc:${CURRENT_VERSION}/l10nmessages-proc:${NEW_VERSION}/g" examples/{gradle,gradle-icu}/app/build.gradle.kts ;

# Bazel
sed -E -i '' -e  "s/l10nmessages-${CURRENT_VERSION}/l10nmessages-${NEW_VERSION}/g" examples/{bazel-local,bazel-icu-local}/l10nmessages_local_maven.BUILD.bzl;
sed -E -i '' -e  "s/l10nmessages-proc-${CURRENT_VERSION}/l10nmessages-proc-${NEW_VERSION}/g" examples/{bazel-local,bazel-icu-local}/l10nmessages_local_maven.BUILD.bzl;

if [[ ${NEW_VERSION} != *SNAPSHOT ]]
then
  # Bazel examples
  echo "Updating to release"
  sed -E -i '' -e  "s/l10nmessages:${CURRENT_VERSION}/l10nmessages:${NEW_VERSION}/g" examples/{bazel,bazel-icu}/WORKSPACE;
  sed -E -i '' -e  "s/l10nmessages-proc:${CURRENT_VERSION}/l10nmessages-proc:${NEW_VERSION}/g" examples/{bazel,bazel-icu}/WORKSPACE;

  # Docs
  sed -E -i '' -e  "s/l10nmessages:${CURRENT_VERSION}/l10nmessages:${NEW_VERSION}/g" docs/docs/installation/{bazel,gradle}.md;
  sed -E -i '' -e  "s/l10nmessages-proc:${CURRENT_VERSION}/l10nmessages-proc:${NEW_VERSION}/g" docs/docs/installation/{bazel,gradle}.md;
  sed -E -i '' -e  "s/The latest version is \`${CURRENT_VERSION}/The latest version is \`${NEW_VERSION}/g" docs/docs/installation/installation.md;
  sed -E -i '' -e  "s/installation-${CURRENT_VERSION}-/installation-${NEW_VERSION}-/g" README.md;

  ## Warning!! this works because only l10nmessages dependencies have x.y.z version (adding any
  sed -E -i '' -e  "s/version>${CURRENT_VERSION}/version>${NEW_VERSION}/g" {docs/docs/installation/maven.md,l10nmessages-mvn-plugin/usage.md};
fi
