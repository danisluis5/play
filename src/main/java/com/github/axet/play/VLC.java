package com.github.axet.play;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_t;

public class VLC {
    static String vlc_args[] = { "-I", "dumy", "--ignore-config", "--no-xlib", "--no-video-title-show" };

    static libvlc_instance_t inst;

    static int count = 0;

    static Object lock = new Object();

    boolean close = false;

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
