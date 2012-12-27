package com.github.axet.play;

import java.awt.image.Kernel;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

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

        File lib = null;

        {
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            for (Thread thread : map.keySet()) {
                StackTraceElement[] stack = thread.getStackTrace();
                if (stack.length == 0)
                    continue;

                for (StackTraceElement main : stack) {
                    String mainClass = main.getClassName();

                    String path;
                    try {
                        Class<?> cls = Class.forName(mainClass);
                        CodeSource src = cls.getProtectionDomain().getCodeSource();
                        if (src == null)
                            continue;
                        path = src.getLocation().getPath();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    // windows return path with %20
                    try {
                        path = URLDecoder.decode(path, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    File natives = new File(path);
                    natives = new File(natives.getParent());
                    natives = FileUtils.getFile(natives, "natives");
                    if (VLC.checkPath(natives)) {
                        lib = natives;
                        break;
                    }
                }
            }
        }

        if (lib == null)
            throw new RuntimeException("VLC natives not found" + "\n"
                    + "you have to place natives next to the /classes folder or /main.jar file in the /natives folder");

        VLC.setPath(lib);
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

    public static boolean checkPath(File path) {
        String[] any = new String[] { "libvlc.so", "libvlc.dylib", "libvlc.dll" };
        for (String l : any) {
            File vlc = FileUtils.getFile(path, l);
            if (vlc.exists())
                return true;
        }

        return false;
    }

    public static void setPath(File path) {
        if (Platform.isLinux() || Platform.isMac()) {
            LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", path.toString(), 1);
        }
        if (Platform.isWindows()) {
            Kernel32.INSTANCE.SetEnvironmentVariable("VLC_PLUGIN_PATH", path.toString());
        }

        NativeLibrary.addSearchPath("vlccore", path.toString());
        NativeLibrary.addSearchPath("vlc", path.toString());

        NativeLibrary.getInstance("vlccore");
        NativeLibrary.getInstance("vlc");
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
