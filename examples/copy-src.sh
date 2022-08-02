#!/usr/bin/env bash

set -eu

jdk_project_srcs=("bazel/src" "bazel-local/src" "gradle/app/src" "maven-explicit/src" "maven-explicit/src")

icu_project_srcs=("bazel-icu/src" "bazel-icu-local/src" "gradle-icu/app/src" "maven-icu-explicit/src")

for project_src in "${jdk_project_srcs[@]}" ; do
  rm -rf "${project_src}"
  cp -r "maven/src" "${project_src}"
done

for project_src in "${icu_project_srcs[@]}" ; do
  rm -rf "${project_src}"
  cp -r "maven-icu/src" "${project_src}"
done