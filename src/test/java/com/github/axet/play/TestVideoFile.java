package com.github.axet.play;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.github.axet.play.PlayVideo;

public class TestVideoFile extends JFrame{
    PlayVideo c;
    JProgressBar progressBar;

    public TestVideoFile() {
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

    public void run(File f) {
        c.open(f);
        System.out.println("run play");
        c.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String name = args.length == 0 ? "test.mp3" : args[0];

        File f = new File(name);
        TestVideoFile t = new TestVideoFile();
        t.run(f);
    }
}
