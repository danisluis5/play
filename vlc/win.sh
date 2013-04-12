#!/bin/bash

(if ! [ -e ../../vlc/contrib/win32 ]; then mkdir -p ../../vlc/contrib/win32 && cd ../../vlc/contrib/win32 && ../bootstrap --host=$HOST; make prebuilt; fi) || exit 1

#rm -f ../../vlc/contrib/$HOST/include/strmif.h || exit 1
#rm -f ../../vlc/contrib/$HOST/include/uuids.h || exit 1
#rm -f ../../vlc/contrib/$HOST/include/dxva2api.h || exit 1

(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && autoreconf -fi && ./bootstrap; fi) || exit 1

(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then mkdir -p build && cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --host=$HOST --with-contrib=$PWD/../../../vlc/contrib/$HOST --enable-shared --disable-live555; fi) || exit 1

(cd build && make)  || exit 1

(if ! [ -e "build/vlc-2.0.5" ]; then cd build && make package-win-strip; fi) || exit 1

