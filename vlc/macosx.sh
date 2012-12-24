#!/bin/bash

rm -rf build/VLC.app
rm -rf natives
mkdir -p build
(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin ./bootstrap)
(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin make)
(cd ./build/ && CFLAGS=-g PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin HOME=/ bash ../../../vlc/extras/package/macosx/build.sh)
(cd ./build/ && make install)
(mkdir -p natives/plugins)
(mkdir -p natives/lib)
(find build/vlc_install_dir/ -name *plugin.dylib -exec cp {} ${PWD}/natives/plugins \;)
(cp build/vlc_install_dir/lib/*.dylib natives/lib)

