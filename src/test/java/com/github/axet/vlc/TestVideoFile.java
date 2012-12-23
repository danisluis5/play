package com.github.axet.vlc;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.github.axet.play.PlayVideo;
import com.github.axet.play.PlayVideoFile;
import com.sun.jna.NativeLibrary;

public class TestVideoFile extends JFrame {
    private static final long serialVersionUID = -2449941177902198161L;

    PlayVideoFile c;

    JProgressBar progressBar;

    public TestVideoFile() {
        progressBar = new JProgressBar();
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        c = new PlayVideoFile();

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

    public void run() {
        File f = new File("/Users/axet/Documents/Morrowind - Rats In My House (Let's Play #2).mp4");
        c.open(f);
        c.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // NativeLibrary.addSearchPath("vlc",
        // "/Applications/VLC.app/Contents/MacOS/lib");

        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/build/VLC.app/Contents/MacOS/lib/");

        TestVideoFile t = new TestVideoFile();
        t.run();
    }
}
