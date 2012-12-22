package com.github.axet.vlc;

import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.axet.play.LibVlc;
import com.github.axet.play.libvlc_instance_t;
import com.github.axet.play.libvlc_media_player_t;
import com.github.axet.play.libvlc_media_t;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

public class TestSound {

    public void run() {
        String vlc_args[] = { "-I", "dumy", // No special interface
                "--ignore-config", // Don't use VLC's config
        };

        // NativeLibrary.addSearchPath("vlc",
        // "/Applications/VLC.app/Contents/MacOS/lib");
        NativeLibrary.addSearchPath("vlc", "/Users/axet/source/mircle/play/vlc/master/VLC.app/Contents/MacOS/lib/");

        libvlc_instance_t inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);

        libvlc_media_player_t m = LibVlc.INSTANCE.libvlc_media_player_new(inst);

        libvlc_media_t fl = LibVlc.INSTANCE.libvlc_media_new_path(inst,
                "/Users/axet/Downloads/globalnews_20121222-1554a.mp3");

        LibVlc.INSTANCE.libvlc_media_player_set_media(m, fl);
        LibVlc.INSTANCE.libvlc_media_player_play(m);
        
        try {
            Thread.sleep(1000*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestSound t = new TestSound();
        t.run();
    }
}
