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
        String name = args.length == 0 ? "test.mp3" : args[0];
       
        File f = new File(name);
        InputStream is = null;

        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TestSoundStream t = new TestSoundStream();
        t.open(is);
    }
}
