package com.github.axet.vlc;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.github.axet.play.PlaySound;

public class TestSoundURL extends JFrame {

    JProgressBar progressBar;

    public TestSoundURL() {

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

    public void run(URL f) {
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // test multichannel

        // p2.open(f);
        // p2.play();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        URL f;
        try {
            f = new URL(args[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        TestSoundURL t = new TestSoundURL();
        t.run(f);
    }
}
