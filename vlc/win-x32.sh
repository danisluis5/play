#!/bin/bash

HOST=i586-mingw32msvc

sudo apt-get -t experimental install -y binutils-mingw-w64-i686 gcc-mingw-w64 gcc-mingw-w64-base gcc-mingw-w64-i686 g++-mingw-w64 g++-mingw-w64-i686 mingw-w64 mingw-w64-dev mingw-w64-tools mingw-w64-i686-dev

. ./win.sh || exit 1

# pack:
rm -f libvlc-windows-x86.jar || exit 1

rm -rf natives || exit 1
mkdir -p natives || exit 1

cp build/vlc-2.0.5/*.dll natives/ || exit 1
cp -r build/vlc-2.0.5/plugins natives/ || exit 1

(cd natives && jar cf ../libvlc-windows-x86.jar *) || exit 1

mvn install:install-file -Dfile=libvlc-windows-x86.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-windows-x86 || exit 1

