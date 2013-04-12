#!/bin/bash

export HOST=x86_64-w64-mingw32
export VLC=vlc-2.0.6

# install yasm and build manually:
# make install PKGS=ffmpeg

sudo apt-get -t experimental install -y  binutils-mingw-w64-x86-64 gcc-mingw-w64-x86-64 g++-mingw-w64-x86-64 mingw-w64-tools mingw-w64-x86-64-dev || exit 1

./win.sh || exit 1

# pack:
rm -f libvlc-windows-x86_64.jar || exit 1

rm -rf natives || exit 1
mkdir -p natives || exit 1

cp build/$VLC/*.dll natives/ || exit 1
cp -r build/$VLC/plugins natives/ || exit 1

(cd natives && jar cf ../libvlc-windows-x86_64.jar *) || exit 1

mvn install:install-file -Dfile=libvlc-windows-x86_64.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-windows-x86_64 || exit 1

