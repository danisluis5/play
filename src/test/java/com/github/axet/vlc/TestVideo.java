package com.github.axet.vlc;

import java.awt.Canvas;

import javax.swing.JFrame;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_player_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class TestVideo {

    Canvas c;

    public void run() {
        JFrame f = new JFrame();

        f.setSize(500, 500);
        f.setLocationRelativeTo(null);

        c = new Canvas();

        f.getContentPane().add(c);

        f.setVisible(true);

        String vlc_args[] = { "-I", "dumy", // No special interface
                "--ignore-config", // Don't use VLC's config
        };

        //NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib");
        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/master/VLC.app/Contents/MacOS/lib/");
        

        libvlc_instance_t inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);

        libvlc_media_player_t m = LibVlc.INSTANCE.libvlc_media_player_new(inst);

        libvlc_media_t fl = LibVlc.INSTANCE.libvlc_media_new_path(inst,
                "/Users/axet/Documents/A Gamer's Xmas.mp4");

        LibVlc.INSTANCE.libvlc_media_player_set_media(m, fl);
        LibVlc.INSTANCE.libvlc_media_player_set_nsobject(m, Native.getComponentID(c));
        LibVlc.INSTANCE.libvlc_media_player_play(m);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestVideo t = new TestVideo();
        t.run();
    }
}
