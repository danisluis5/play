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
        String name = args.length == 0 ? "test.mp3" : args[0];

        File f = new File(name);

        InputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TestVideoSteam t = new TestVideoSteam();
        t.open(is);
    }
}
