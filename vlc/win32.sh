#!/bin/bash


if ! [ -e g++-mingw-w64_4.6.3-14+9_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/g++-mingw-w64_4.6.3-14+9_all.deb
	sudo dpkg -i g++-mingw-w64_4.6.3-14+9_all.deb
fi

if ! [ -e gcc-mingw-w64_4.6.3-14+9_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/gcc-mingw-w64_4.6.3-14+9_all.deb
	sudo dpkg -i gcc-mingw-w64_4.6.3-14+9_all.deb
fi

if ! [ -e mingw-w64-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-dev_3.0~svn5408-1_all.deb
	sudo dpkg -i mingw-w64-dev_3.0~svn5408-1_all.deb
fi

if ! [ -e mingw-w64-i686-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-i686-dev_3.0~svn5408-1_all.deb
	sudo dpkg -i mingw-w64-i686-dev_3.0~svn5408-1_all.deb
fi

if ! [ -e mingw-w64-tools_3.0~svn5408-1_i386.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-tools_3.0~svn5408-1_i386.deb
	sudo dpkg -i mingw-w64-tools_3.0~svn5408-1_i386.deb
fi

if ! [ -e mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb
	sudo dpkg -i mingw-w64-x86-64-dev_3.0~svn5408-1_all.deb
fi

if ! [ -e mingw-w64_3.0~svn5408-1_all.deb ] ; then
	wget http://people.videolan.org/~funman/win/mingw-w64_3.0~svn5408-1_all.deb
	sudo dpkg -i mingw-w64_3.0~svn5408-1_all.deb
fi

(if ! [ -e ../../vlc/contrib/win32 ]; then mkdir -p ../../vlc/contrib/win32 && cd ../../vlc/contrib/win32 && ../bootstrap --host=i586-mingw32msvc; make prebuilt; fi)

rm -f ../../vlc/contrib/i586-mingw32msvc/include/strmif.h
rm -f ../../vlc/contrib/i586-mingw32msvc/include/uuids.h

rm -f ../../vlc/contrib/i586-mingw32msvc/include/dxva2api.h

#rm -f ../../vlc/contrib/i586-mingw32msvc/include/libavcodec/dxva2.h
#touch ../../vlc/contrib/i586-mingw32msvc/include/libavcodec/dxva2.h
#ln -sf /usr/i686-w64-mingw32/include/dxva.h ../../vlc/contrib/i586-mingw32msvc/include/libavcodec/dxva2.h

(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && autoreconf -fi && ./bootstrap; fi)

(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then mkdir -p build && cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --host=i586-mingw32msvc --with-contrib=`pwd`/../../../vlc/contrib/i586-mingw32msvc --enable-shared --disable-live555; fi)
cd build && make

if ! [ -e "build/_win32" ]; then cd build && make install; fi

# pack:
rm -f libvlc-windows-x86.jar

mkdir -p natives/

cp build/_win32/bin/*.dll natives

find build/_win32/lib/vlc/plugins/ -name *.dll -exec cp {} natives \;

(cd natives && jar cf ../libvlc-windows-x86.jar *)

mvn install:install-file -Dfile=libvlc-windows-x86.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-windows-x86

