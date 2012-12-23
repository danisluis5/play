package com.github.axet.vlc;

import java.io.File;

import javax.swing.JFrame;

import com.github.axet.play.PlaySoundFile;
import com.sun.jna.NativeLibrary;

public class TestSound extends JFrame {

    PlaySoundFile p = new PlaySoundFile();

    public void run() {
        // NativeLibrary.addSearchPath("vlc",
        // "/Applications/VLC.app/Contents/MacOS/lib");
        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/build/VLC.app/Contents/MacOS/lib/");

        //File f = new File("/Users/axet/Documents/globalnews_20121222-1554a.mp3");
       File f = new File("/Users/axet/Documents/1.ogg");

        p.open(f);
        p.play();

        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestSound t = new TestSound();
        t.run();
    }
}
