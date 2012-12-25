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

    public void run() {
        File f = new File("/home/axet/Desktop/10 Second Holiday Video.mp4");
        c.open(f);
        c.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        VLC.setPath("/home/axet/source/mircle/play/vlc/natives");

        TestVideoFile t = new TestVideoFile();
        t.run();
    }
}
