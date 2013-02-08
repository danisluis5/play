package com.github.axet.play;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.junit.Test;

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
        TestSoundFile t = new TestSoundFile();
        File f = new File(args[0]);
        t.run(f);
    }
}
