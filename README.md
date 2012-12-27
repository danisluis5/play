# play

Play - is a Java library to play Video / Music.

It would be based on VLC or multi based on VLC / QTMovie / gstreams.

Right now I'm testing VLC on all platroms, but if it fails. I will support platform dependent librarys (Windows - VLC,
Mac - QTMovie, Linux - gstreams).

Why another Java Video Library?

- VLCJ - windows library, never been tested on Mac and Linux platform. It is worth mentioning author do not care about support for library
users, unless you have paid subscription. Also I'm sure VLCJ will never be released under LGPL licence.

# Features
  - building vlc library scripts
  
  - native maven support

  - packing vlc librarys into jar and installing it into local maven repo

  - support for all three platforms.
  
  - native stream support (InputStream to the VLC!)

# Licence

LGPL 3.0

Next releases will be compatible with VLC project licence (I will change licence if required)
