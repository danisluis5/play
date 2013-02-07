#!/bin/bash

sudo apt-get install -y binutils-mingw-w64-x86-64 binutils-mingw-w64-i686

DEBS="http://people.videolan.org/~funman/win/gcc-mingw-w64-base_4.6.3-14+9_amd64.deb \
      http://people.videolan.org/~funman/win/mingw-w64-x86-64-dev_3.0~svn5496-1_all.deb \
      http://people.videolan.org/~funman/win/gcc-mingw-w64-x86-64_4.6.3-14+9_amd64.deb \
      http://people.videolan.org/~funman/win/g++-mingw-w64-x86-64_4.6.3-14+9_amd64.deb \
      http://people.videolan.org/~funman/win/mingw-w64-i686-dev_3.0~svn5496-1_all.deb \
      http://people.videolan.org/~funman/win/gcc-mingw-w64-i686_4.6.3-14+9_amd64.deb \
      http://people.videolan.org/~funman/win/g++-mingw-w64-i686_4.6.3-14+9_amd64.deb \
      http://people.videolan.org/~funman/win/g++-mingw-w64_4.6.3-14+9_all.deb \
      http://people.videolan.org/~funman/win/gcc-mingw-w64_4.6.3-14+9_all.deb \
      http://people.videolan.org/~funman/win/mingw-w64-dev_3.0~svn5496-1_all.deb \
      http://people.videolan.org/~funman/win/mingw-w64_3.0~svn5496-1_all.deb \
      $DEBS"

for DEB in $DEBS; do
  D=$(basename $DEB)
  if ! [ -e $D ]; then
    wget $DEB || exit 1
    sudo dpkg -i $D || exit 1
  fi
done

(if ! [ -e ../../vlc/contrib/win32 ]; then mkdir -p ../../vlc/contrib/win32 && cd ../../vlc/contrib/win32 && ../bootstrap --host=$HOST; make prebuilt; fi) || exit 1

#rm -f ../../vlc/contrib/$HOST/include/strmif.h || exit 1
#rm -f ../../vlc/contrib/$HOST/include/uuids.h || exit 1
#rm -f ../../vlc/contrib/$HOST/include/dxva2api.h || exit 1

(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && autoreconf -fi && ./bootstrap; fi) || exit 1

(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then mkdir -p build && cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --host=$HOST --with-contrib=`pwd`/../../../vlc/contrib/$HOST --enable-shared --disable-live555; fi) || exit 1

(cd build && make)  || exit 1

(if ! [ -e "build/vlc-2.0.5" ]; then cd build && make package-win-strip; fi) || exit 1

