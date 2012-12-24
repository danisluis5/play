package com.github.axet.vlc;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.github.axet.play.PlaySound;
import com.sun.jna.NativeLibrary;

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
    PlaySound p2 = new PlaySound();

    public void run() {
        // File f = new
        // File("/Users/axet/Documents/globalnews_20121222-1554a.mp3");
        File f = new File("/home/axet/Desktop/1.ogg");

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
            }
        });

        p.open(f);
        p.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // NativeLibrary.addSearchPath("vlc",
        // "/Applications/VLC.app/Contents/MacOS/lib");

        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/natives/lib");
        //NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/build/VLC.app/Contents/MacOS/lib/");

        TestSoundFile t = new TestSoundFile();
        t.run();
    }
}
