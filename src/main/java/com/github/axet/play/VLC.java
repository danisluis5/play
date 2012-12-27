package com.github.axet.play;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.github.axet.play.vlc.LibC;
import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

public class VLC {
    static String vlc_args[] = { "-I", "dumy", "--ignore-config", "--no-xlib", "--no-video-title-show" };

    static libvlc_instance_t inst;

    static int count = 0;

    static Object lock = new Object();

    boolean close = false;

    static {
        // 1) under debugger, eclipse + maven-nativedependencies-plugin
        String path = VLC.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        File natives = new File(path);
        natives = new File(natives.getParent());
        natives = FileUtils.getFile(natives, "natives");
        if (natives.exists()) {
            VLC.setPath(natives);
        }
        // 2) mac osx wihtout debugger path - points to the jar inside the application
        // case above 1) works prefectly
    }

    public VLC() {
        synchronized (lock) {
            if (count == 0) {
                inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);

                if (inst == null)
                    throw new RuntimeException("Unable to instantiate VLC");

                VLCWarmup.warmup(this);
            }

            count++;
        }
    }

    public static void setPath(File path) {
        NativeLibrary.addSearchPath("vlccore", path.toString());
        NativeLibrary.addSearchPath("vlc", path.toString());

        NativeLibrary.getInstance("vlccore");
        NativeLibrary.getInstance("vlc");

        if (Platform.isLinux() || Platform.isMac()) {
            LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", path.toString(), 1);
        }
    }

    public libvlc_instance_t getInstance() {
        synchronized (lock) {
            return inst;
        }
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        synchronized (lock) {
            if (!close) {
                close = true;
                count--;
                if (count == 0) {
                    LibVlc.INSTANCE.libvlc_release(inst);
                    inst = null;
                }
            }
        }
    }

    public libvlc_media_t createMedia(String uri) {
        return LibVlc.INSTANCE.libvlc_media_new_path(getInstance(), uri);
    }
}
