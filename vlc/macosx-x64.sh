#!/bin/bash

./macosx.sh

# pack:
rm -f libvlc-mac-x86_64.jar

(cd natives && jar cf ../libvlc-mac-x86_64.jar *)

mvn install:install-file -Dfile=libvlc-mac-x86_64.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-mac-x86_64
