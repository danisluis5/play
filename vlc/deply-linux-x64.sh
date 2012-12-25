#!/bin/bash

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86_64.pom \
  -Dclassifier=natives \
  -Dpackaging=jar \
  -Dfile=libvlc-linux-x86_64.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86_64.pom \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=empty.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86_64.pom \
  -Dclassifier=javadoc \
  -Dpackaging=jar \
  -Dfile=empty.jar
