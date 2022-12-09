Artifacts are published to Maven Central through Sonatype staging area.

Only `l10nmessages`, `l10nmessages-proc`, `l10nmessages-mvn-plugin`, and `l10nmessages-parent` are
published. Examples and integration tests jars are not (see the modules definition in the release
profile and the default profile).

[Sonatype documentation for publishing to Maven](https://central.sonatype.org/publish/publish-guide/)

### Setup GPG keys

Make sure the GPG key is accessible locally to sign artifacts

### Sonatype credential in your settings.xml

```xml title=~/.m2
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
</settings>
```

### Prepare the commit for the release

Figure out the next version based on [SemVer](https://semver.org/)
and [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/). 

Update this document with new version while doing the release.

```shell
export NEW_VERSION=1.0.2
mvn versions:set -DnewVersion=${NEW_VERSION} -DgenerateBackupPoms=false
mvn versions:set-scm-tag -DnewTag=${NEW_VERSION} -DgenerateBackupPoms=false
./update-non-maven-versions.sh ${NEW_VERSION}
git commit -am "release: ${NEW_VERSION}"
git push
git tag ${NEW_VERSION}
git push origin ${NEW_VERSION}
```

Review commit and wait for CI to succeed!

### Run the deploy

This will prompt for the GPG key

```shell
GPG_TTY=$(tty) mvn clean deploy -Prelease
```

### Prepare next snapshot version

The next snapshot version is the release version with PATCH incremented by one and suffixed
with "SNAPSHOT"

```shell
export NEW_VERSION=1.0.2-SNAPSHOT
mvn versions:set -DnewVersion=${NEW_VERSION} -DgenerateBackupPoms=false
mvn versions:set-scm-tag -DnewTag=HEAD -DgenerateBackupPoms=false
./update-non-maven-versions.sh ${NEW_VERSION}
git commit -am "release: prepare next version ${NEW_VERSION}"
git push
```

Again, check CI succeed.

### Publish the release

Go to [Sonatype](https://oss.sonatype.org/#stagingRepositories) to review the staged deployment and
release it to Maven Central.
