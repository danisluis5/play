#!/bin/bash

rm -rf build/VLC.app
rm -rf natives
mkdir -p build
(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin ./bootstrap) || exit 1
(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin make) || exit 1
(cd ./build/ && CFLAGS=-g PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin HOME=/ bash ../../../vlc/extras/package/macosx/build.sh) || exit 1
(cd ./build/ && make install) || exit 1
(mkdir -p natives/plugins) || exit 1
(mkdir -p natives/lib) || exit 1
(find build/vlc_install_dir/ -name *plugin.dylib -exec cp {} ${PWD}/natives/plugins \;) || exit 1
(cp build/vlc_install_dir/lib/*.dylib natives/lib) || exit 1
