#!/bin/bash


if ! [ -e g++-mingw-w64_4.6.3-14+9_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/g++-mingw-w64_4.6.3-14+9_all.deb || exit 1
	sudo dpkg -i g++-mingw-w64_4.6.3-14+9_all.deb || exit 1
fi

if ! [ -e gcc-mingw-w64_4.6.3-14+9_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/gcc-mingw-w64_4.6.3-14+9_all.deb || exit 1
	sudo dpkg -i gcc-mingw-w64_4.6.3-14+9_all.deb || exit 1
fi

if ! [ -e mingw-w64-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-dev_3.0~svn5408-1_all.deb || exit 1
	sudo dpkg -i mingw-w64-dev_3.0~svn5408-1_all.deb || exit 1
fi

if ! [ -e mingw-w64-i686-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-i686-dev_3.0~svn5408-1_all.deb || exit 1
	sudo dpkg -i mingw-w64-i686-dev_3.0~svn5408-1_all.deb || exit 1
fi

if ! [ -e mingw-w64-tools_3.0~svn5408-1_i386.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-tools_3.0~svn5408-1_i386.deb || exit 1
	sudo dpkg -i mingw-w64-tools_3.0~svn5408-1_i386.deb || exit 1
fi

if ! [ -e mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb || exit 1
	sudo dpkg -i mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb || exit 1
fi

if ! [ -e mingw-w64_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64_3.0~svn5408-1_all.deb || exit 1
	sudo dpkg -i mingw-w64_3.0~svn5408-1_all.deb || exit 1
fi

(if ! [ -e ../../vlc/contrib/win32 ]; then mkdir -p ../../vlc/contrib/win32 && cd ../../vlc/contrib/win32 && ../bootstrap --host=i586-mingw32msvc; make prebuilt; fi) || exit 1

rm -f ../../vlc/contrib/i586-mingw32msvc/include/strmif.h || exit 1
rm -f ../../vlc/contrib/i586-mingw32msvc/include/uuids.h || exit 1
rm -f ../../vlc/contrib/i586-mingw32msvc/include/dxva2api.h || exit 1

(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && autoreconf -fi && ./bootstrap; fi) || exit 1

(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then mkdir -p build && cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --host=i586-mingw32msvc --with-contrib=`pwd`/../../../vlc/contrib/i586-mingw32msvc --enable-shared --disable-live555; fi) || exit 1

(cd build && make)  || exit 1

(if ! [ -e "build/vlc-2.0.5" ]; then cd build && make package-win-strip; fi) || exit 1

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

