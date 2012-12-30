package com.github.axet.play;

import java.io.File;

import com.github.axet.play.vlc.LibC;
import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;

public class VLC {
    static String vlc_args[] = { "-I", "dumy", "--ignore-config", "--no-xlib", "--no-video-title-show" };

    static libvlc_instance_t inst;

    static int count = 0;

    static Object lock = new Object();

    boolean close = false;

    static {
        // use eclipse + maven-nativedependencies-plugin

        // VLC.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        // 1) under debugger, /Users/axet/source/mircle/play/target/classes/
        //
        // 2) mac osx wihtout debugger path -
        // /Users/axet/source/mircle/mircle/macosx/Mircle.app/Contents/Resources/Java/mircle.jar
        // case above 1) works prefectly
        //
        // 3) pack with maven under debugger
        // /Users/axet/.m2/repository/com/github/axet/play/0.0.3/play-0.0.3.jar

        MavenNatives.mavenNatives(new String[] { "vlccore", "vlc" });

        File path = new File(NativeLibrary.getInstance("vlc").getFile().getParent());

        setPluginPath(path);
    }

    public static void setPluginPath(File path) {
        if (Platform.isLinux() || Platform.isMac()) {
            LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", path.toString(), 1);
        }
        if (Platform.isWindows()) {
            Kernel32.INSTANCE.SetEnvironmentVariable("VLC_PLUGIN_PATH", path.toString());
        }
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
