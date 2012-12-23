package com.github.axet.vlc;

import java.io.File;

import javax.swing.JFrame;

import com.github.axet.play.PlayVideoFile;
import com.sun.jna.NativeLibrary;

public class TestVideoFile extends JFrame {
    private static final long serialVersionUID = -2449941177902198161L;

    PlayVideoFile c;

    public void run() {
        c = new PlayVideoFile();

        getContentPane().add(c);

        pack();

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // NativeLibrary.addSearchPath("vlc",
        // "/Applications/VLC.app/Contents/MacOS/lib");

        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/build/VLC.app/Contents/MacOS/lib/");

        File f = new File("/Users/axet/Documents/Morrowind - Rats In My House (Let's Play #2).mp4");
        c.open(f);
        c.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestVideoFile t = new TestVideoFile();
        t.run();
    }
}
