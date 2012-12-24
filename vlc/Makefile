# make clean - clean source vlc dir
# make build - build current working branch
# make master - pull last changes from master repo and build
# make pack - pack native jar from build repo

all: pack

.PHONY: build

/usr/bin/libtool:
	sudo apt-get install -y libtool

/usr/bin/autoreconf:
	sudo apt-get install -y autoconf

/usr/bin/i586-mingw32msvc-gcc:
	sudo apt-get install -y gcc-mingw32

mingw32-runtime_3.17.0-0videolan_all.deb:
	wget http://people.videolan.org/~jb/debian/mingw32-runtime_3.17.0-0videolan_all.deb

/usr/share/lintian/overrides/mingw32-runtime: mingw32-runtime_3.17.0-0videolan_all.deb
	sudo dpkg -i mingw32-runtime_3.17.0-0videolan_all.deb

/usr/bin/lua5.1:
	sudo apt-get install -y lua5.1 liblua5.1-0-dev

/usr/include/libraw1394:
	sudo apt-get install -y libraw1394-dev

/usr/include/dc1394:
	sudo apt-get install -y libdc1394-22-dev

/usr/include/libavc1394:
	sudo apt-get install -y libavc1394-dev

/usr/include/dvdread:
	sudo apt-get install -y libdvdread-dev

/usr/include/shout:
	 sudo apt-get install -y libshout3-dev

/usr/include/libavcodec:
	 sudo apt-get install -y libavcodec-dev

/usr/include/libavformat:
	sudo apt-get install -y libavformat-dev

/usr/include/libswscale:
	 sudo apt-get install -y libswscale-dev

/usr/include/libpostproc:
	sudo apt-get install -y libpostproc-dev

/usr/include/faad.h:
	sudo apt-get install -y libfaad-dev

/usr/include/dbus-1.0/dbus:
	sudo apt-get install -y libdbus-1-dev

/usr/include/a52dec:
	sudo apt-get install -y liba52-0.7.4-dev

/usr/include/xcb:
	sudo apt-get install -y libxcb1-dev

/usr/include/xcb/shm.h:
	sudo apt-get install -y libxcb-shm0-dev

/usr/include/xcb/composite.h:
	sudo apt-get install -y libxcb-composite0-dev

/usr/include/xcb/xv.h:
	sudo apt-get install -y libxcb-xv0-dev

/usr/include/xcb/glx.h:
	 sudo apt-get install -y libxcb-glx0-dev libgl1-mesa-dev

/usr/include/alsa:
	sudo apt-get install -y libasound2-dev

/usr/include/gcrypt.h:
	sudo apt-get install -y libgcrypt11-dev

/usr/bin/msgfmt:
	sudo apt-get install -y gettext

/usr/include/zlib.h:
	 sudo apt-get install -y zlib1g-dev

/usr/bin/g++:
	sudo apt-get install -y g++

deps: /usr/bin/autoreconf /usr/bin/libtool /usr/bin/i586-mingw32msvc-gcc /usr/share/lintian/overrides/mingw32-runtime /usr/bin/lua5.1 /usr/include/libraw1394 /usr/include/dc1394 /usr/include/libavc1394 /usr/include/dvdread /usr/include/shout /usr/include/libavcodec /usr/include/libavformat /usr/include/libswscale /usr/include/libpostproc /usr/include/faad.h /usr/include/dbus-1.0/dbus /usr/include/a52dec /usr/include/xcb /usr/include/xcb/shm.h /usr/include/xcb/composite.h /usr/include/xcb/xv.h /usr/include/xcb/glx.h /usr/include/alsa /usr/include/gcrypt.h /usr/bin/msgfmt /usr/include/zlib.h /usr/bin/g++

build-linux: deps
	mkdir -p build
	mkdir -p ../../vlc/contrib/linux
	(cd ../../vlc/contrib/linux && ../bootstrap)
	(cd ../../vlc && ./bootstrap)
	(cd ./build/ && ../../../vlc/configure)
	(cd ./build/ && make)

build-win32: deps
	mkdir -p build
	mkdir -p ../../vlc/contrib/win32
	(cd ../../vlc/contrib/win32 && ../bootstrap --host=i586-mingw32msvc)
	(if [ ! -e ../../vlc/contrib/i586-mingw32msvc ]; then cd ../../vlc/contrib/win32 && make prebuilt; fi)
	(cd ../../vlc && ./bootstrap)
	(cd ./build/ && ../../../vlc/extras/package/win32/configure.sh --enable-shared --disable-mad --disable-live555 --host=i586-mingw32msvc)

build-macosx:
	rm -rf build/VLC.app
	mkdir -p build
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin ./bootstrap)
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin make)
	(cd ./build/ && CFLAGS=-g PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PWD/../../vlc/extras/tools/build/bin HOME=/ bash ../../../vlc/extras/package/macosx/build.sh)

clean:
	(cd ../../vlc && git clean -fdx)

master-setup:
	(cd ../../vlc/; git remote add vlc git://git.videolan.org/vlc.git)
	(cd ../../vlc/; git fetch vlc)
	(cd ../../vlc/; git checkout -b vlc vlc/master)

.PHONY: master

# CFLAGS - for debuging, add debug information to the libvlc
# PATH - to ignore macports /opt/local/bin folder
# HOME=/ - to ignore ~/.gitconfig which is not compatible between git 1.8 and git 1.7 (apple default)

master:
	rm -rf master/
	mkdir -p master/
	(cd ../../vlc/ && git fetch vlc)
	(cd ../../vlc/ && git checkout vlc)
	(cd ../../vlc/ && git pull --rebase)
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin ./bootstrap)
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin make)
	(cd ./master/ && CFLAGS=-g PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PWD/../../vlc/extras/tools/build/bin HOME=/ bash ../../../vlc/extras/package/macosx/build.sh)

pack:
	rm -f vlc-mac.jar

	(cd ./build/VLC.app/Contents/MacOS/lib && jar cf ../../../../../vlc-mac.jar *)
	(cd ./build/VLC.app/Contents/MacOS/plugins && jar uf ../../../../../vlc-mac.jar *)
	
	mvn install:install-file -Dfile=vlc-mac.jar \
	  -DgroupId=org.videolan \
      -DartifactId=vlc \
      -Dversion=1.3.0-SNAPSHOT \
      -Dpackaging=jar \
      -Dclassifier=natives-mac \
      -DgeneratePom=true \
      -DcreateChecksum=true
