#!/bin/bash

zip README.jar README

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86.pom \
  -Dclassifier=natives \
  -Dpackaging=jar \
  -Dfile=libvlc-linux-x86.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86.pom \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=README.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86.pom \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=README.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc-linux-x86.pom \
  -Dclassifier=javadoc \
  -Dpackaging=jar \
  -Dfile=README.jar
