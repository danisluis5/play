#!/bin/bash

mkdir -p build
mkdir -p ../../vlc/contrib/win32
(cd ../../vlc/contrib/win32 && ../bootstrap --host=i586-mingw32msvc)
(if [ ! -e ../../vlc/contrib/i586-mingw32msvc ]; then cd ../../vlc/contrib/win32 && make prebuilt; fi)
(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && ./bootstrap; fi)
(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --enable-shared --disable-mad --disable-live555 --host=i586-mingw32msvc; fi)

