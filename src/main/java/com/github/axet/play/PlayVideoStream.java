package com.github.axet.play;

import java.io.IOException;
import java.io.InputStream;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.MemFile;
import com.github.axet.play.vlc.MemFileClose;
import com.github.axet.play.vlc.MemFileOpen;
import com.github.axet.play.vlc.MemFileRead;
import com.github.axet.play.vlc.MemFileSeek;
import com.github.axet.play.vlc.MemFileSize;
import com.github.axet.play.vlc.libvlc_callback_t;
import com.github.axet.play.vlc.libvlc_event_manager_t;
import com.github.axet.play.vlc.libvlc_event_type_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public class PlayVideoStream extends PlayVideo {
    private static final long serialVersionUID = -2160553665816168745L;

    MemFile mem;

    VLC vlc;
    VLCMediaPlayer m;

    libvlc_callback_t evets = new libvlc_callback_t() {
        @Override
        public void libvlc_callback(IntByReference p_event, Pointer p_user_data) {
            switch (p_event.getValue()) {
            case libvlc_event_type_t.libvlc_MediaPlayerEndReached:
                for (Listener l : listeners) {
                    l.position(1.0f);
                    l.stop();
                }
                stop();
                break;
            default:
                break;
            }
        }
    };

    public PlayVideoStream() {
    }

    public void open(final InputStream is) {
        vlc = new VLC();

        m = new VLCMediaPlayer();

        mem = new MemFile();

        mem.open = new MemFileOpen() {
            @Override
            public int open() {
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.close = new MemFileClose() {
            @Override
            public int close() {
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.size = new MemFileSize() {
            @Override
            public int size(LongByReference size) {
                size.setValue(-1);
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.seek = new MemFileSeek() {
            @Override
            public int seek(long pos) {
                return LibVlc.VLC_EGENERIC;
            }
        };

        mem.read = new MemFileRead() {
            @Override
            public int read(Pointer buf, int bufSize) {
                byte[] b = new byte[bufSize];
                try {
                    int len = is.read(b);

                    if (len == -1)
                        return 0;

                    buf.write(0, b, 0, len);

                    return len;
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
            }
        };

        mem.write();

        libvlc_media_t fl = LibVlc.INSTANCE.libvlc_media_new_location(vlc.getInstance(), "memfile://" + mem.getOpen()
                + "/" + mem.getClose() + "/" + mem.getSize() + "/" + mem.getSeek() + "/" + mem.getRead());

        LibVlc.INSTANCE.libvlc_media_player_set_media(m.getInstance(), fl);

        libvlc_event_manager_t ev = LibVlc.INSTANCE.libvlc_media_player_event_manager(m.getInstance());
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerEndReached, evets, null);

        LibVlc.INSTANCE.libvlc_media_player_set_nsobject(m.getInstance(), Native.getComponentID(this));
    }

    public void play() {
        setVolume(100);
        LibVlc.INSTANCE.libvlc_media_player_play(m.getInstance());
    }

    public void stop() {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m.getInstance(), 0);
        LibVlc.INSTANCE.libvlc_media_player_stop(m.getInstance());
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        if (m != null) {
            LibVlc.INSTANCE.libvlc_media_player_release(m.getInstance());
            m = null;
        }
        if (vlc != null) {
            vlc.close();
            vlc = null;
        }
    }

    @Override
    public void setVolume(int v) {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m.getInstance(), v);
    }
}
