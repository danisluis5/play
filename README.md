# play

Play - is a Java library to play Video / Music.

It would be based on VLC or multi based on VLC / QTMovie / gstreams.

Right now I'm testing VLC on all platroms, but if it fails. I will support platform dependent librarys (Windows - VLC,
Mac - QTMovie, Linux - gstreams).

Why another Java Video Library?

- VLCJ - windows library, never been tested on Mac and Linux platform. It is worth mentioning author do not care about community, unless you have paid subscription. Also I'm sure VLCJ will never be released under LGPL licence.

# Features
  - native maven support (take it from central repo!)
  - native librarys in place (no longer need to search for vlc.dll or libvlc.so!)
  - support for all platforms (win, linux, mac)
  - native stream support (InputStream to the VLC!)

# This library just works!

  - Windows (32)
  - Windows (64) - (under PROGRESS)
  - Linux (32)
  - Linux (64)
  - Mac OS X (32) (NOT SUPPORTED)
  - Mac OS X (64) (failed to play video, investigating)

## Example Sound Player

    package com.github.axet.vlc;
    
    import java.awt.BorderLayout;
    import java.io.File;
    
    import javax.swing.JFrame;
    import javax.swing.JProgressBar;
    import javax.swing.SwingUtilities;
    
    import com.github.axet.play.PlaySound;
    
    public class TestSoundFile extends JFrame {
    
        JProgressBar progressBar;
    
        public TestSoundFile() {
    
            progressBar = new JProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
    
            getContentPane().add(progressBar, BorderLayout.CENTER);
    
            setSize(300, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    
        PlaySound p = new PlaySound();
    
        public void run(File f) {
            p.addListener(new PlaySound.Listener() {
                @Override
                public void position(final float pos) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setValue((int) (100.0 * pos));
                        }
                    });
                }
    
                @Override
                public void stop() {
                    System.out.println("actual streaming stop");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            p.close();
                            dispose();
                        }
                    });
                }
    
                @Override
                public void start() {
                    System.out.println("actual streaming start");
                }
            });
    
            p.open(f);
            System.out.println("run play");
            p.play();
        }
    
        public static void main(String[] args) {
            File f = new File(args[0]);
            TestSoundFile t = new TestSoundFile();
            t.run(f);
        }
    }

## Example Sound InputStream

    package com.github.axet.vlc;
    
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.InputStream;
    
    import javax.swing.JFrame;
    import javax.swing.SwingUtilities;
    
    import com.github.axet.play.PlaySound;
    
    public class TestSoundStream extends JFrame {
        private static final long serialVersionUID = 27911591221853186L;
    
        PlaySound p = new PlaySound();
    
        public TestSoundStream() {
            setSize(300, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
    
            p.addListener(new PlaySound.Listener() {
                @Override
                public void position(final float pos) {
                }
    
                @Override
                public void stop() {
                    System.out.println("actual streaming stop");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            p.close();
                            dispose();
                        }
                    });
                }
    
                @Override
                public void start() {
                    System.out.println("actual streaming start");
                }
            });
        }
    
        public void open(InputStream is) {
            p.open(is);
            p.play();
        }
    
        public static void main(String[] args) {
            TestSoundStream t = new TestSoundStream();
    
            File f = new File(args[0]);
            InputStream is = null;
    
            try {
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    
            t.open(is);
        }
    }


## Example Video Player

    package com.github.axet.vlc;
    
    import java.awt.BorderLayout;
    import java.io.File;
    
    import javax.swing.JFrame;
    import javax.swing.JProgressBar;
    import javax.swing.SwingUtilities;
    
    import com.github.axet.play.PlayVideo;
    import com.github.axet.play.VLC;
    import com.sun.jna.NativeLibrary;
    
    public class TestVideoFile extends JFrame {
        private static final long serialVersionUID = -2449941177902198161L;
    
        PlayVideo c;
    
        JProgressBar progressBar;
    
        public TestVideoFile() {
            super("PLAYER");
    
            progressBar = new JProgressBar();
            getContentPane().add(progressBar, BorderLayout.SOUTH);
    
            c = new PlayVideo();
    
            c.addListener(new PlayVideo.Listener() {
                @Override
                public void position(final float pos) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setValue((int) (100.0 * pos));
                        }
                    });
                }
    
                @Override
                public void stop() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            c.close();
                            dispose();
                        }
                    });
                }
    
                @Override
                public void start() {
                }
            });
    
            getContentPane().add(c, BorderLayout.CENTER);
    
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            setSize(500, 500);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    
        public void run(File f) {
            c.open(f);
            c.play();
        }
    
        public static void main(String[] args) {
            File f = new File(args[0]);
            TestVideoFile t = new TestVideoFile();
            t.run(f);
        }
    }

## Example Video InputStream

    package com.github.axet.vlc;
    
    import java.awt.BorderLayout;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.InputStream;
    
    import javax.swing.JFrame;
    import javax.swing.JProgressBar;
    import javax.swing.SwingUtilities;
    
    import com.github.axet.play.PlayVideo;
    
    public class TestVideoSteam extends JFrame {
        private static final long serialVersionUID = -2449941177902198161L;
    
        PlayVideo c;
    
        public TestVideoSteam() {
            c = new PlayVideo();
    
            c.addListener(new PlayVideo.Listener() {
                @Override
                public void position(final float pos) {
                    System.out.println("no position event for inputstream possible");
                }
    
                @Override
                public void stop() {
                    System.out.println("actual streaming stop");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            c.close();
                            dispose();
                        }
                    });
                }
    
                @Override
                public void start() {
                    System.out.println("actual streaming start");
                }
            });
    
            getContentPane().add(c, BorderLayout.CENTER);
    
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            setSize(500, 500);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    
        public void open(InputStream is) {
            c.open(is);
            c.play();
        }
    
        public static void main(String[] args) {
            TestVideoSteam t = new TestVideoSteam();
    
            File f = new File(args[0]);
    
            InputStream is = null;
            try {
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    
            t.open(is);
        }
    }


## Central Maven Repo

    <dependency>
      <groupId>com.github.axet</groupId>
      <artifactId>play</artifactId>
      <version>0.0.5</version>
    </dependency>
    
# Licence

LGPL 3.0

Next releases will be compatible with VLC project licence (I will change licence if required)
