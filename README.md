# play

Play - is a Java library to play Video / Music.

It would be based on VLC or multi based on VLC / QTMovie / gstreams.

Right now I'm testing VLC on all platroms, but if it fails. I will support platform dependent librarys (Windows - VLC,
Mac - QTMovie, Linux - gstreams).

Why another Java Video Library?

- VLCJ - windows library, never been tested on Mac and Linux platform. It is worth mentioning author do not care about support for library
users, unless you have paid subscription. Also I'm sure VLCJ will never be released under LGPL licence.

# Features
  - native maven support (take it from central repo!)
  - native librarys in place (no longer need to search for vlc.dll or libvlc.so!)
  - support for all platforms (win, linux, mac)
  - native stream support (InputStream to the VLC!)

# This library just works!

  - Windows XP (32)
  - Windows 8 (32)
  - Linux (32)
  - Linux (64)
  - Mac OS X (64) (failed to play video)

# Licence

LGPL 3.0

Next releases will be compatible with VLC project licence (I will change licence if required)
