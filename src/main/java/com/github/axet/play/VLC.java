package com.github.axet.play;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;

public class VLC {
    static String vlc_args[] = { "-I", "dumy", "--ignore-config" };

    static libvlc_instance_t inst;

    static int count = 0;

    static Object lock = new Object();

    boolean close = false;

    public VLC() {
        synchronized (lock) {
            if (count == 0) {
                inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);
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
}
