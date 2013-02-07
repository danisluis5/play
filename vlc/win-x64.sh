#!/bin/bash

HOST=x86_64-w64-mingw32
DEBS="http://people.videolan.org/~funman/win/mingw-w64-tools_3.0~svn5496-1_amd64.deb"

. ./win.sh || exit 1

# pack:
rm -f libvlc-windows-x86_64.jar || exit 1

rm -rf natives || exit 1
mkdir -p natives || exit 1

cp build/vlc-2.0.5/*.dll natives/ || exit 1
cp -r build/vlc-2.0.5/plugins natives/ || exit 1

(cd natives && jar cf ../libvlc-windows-x86_64.jar *) || exit 1

mvn install:install-file -Dfile=libvlc-windows-x86_64.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-windows-x86_64 || exit 1

