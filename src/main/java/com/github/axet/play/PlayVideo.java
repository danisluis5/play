package com.github.axet.play;

import java.awt.Canvas;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.Memfile;
import com.github.axet.play.vlc.MemoryFile;
import com.github.axet.play.vlc.MemoryStream;
import com.github.axet.play.vlc.libvlc_callback_t;
import com.github.axet.play.vlc.libvlc_event_manager_t;
import com.github.axet.play.vlc.libvlc_event_type_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class PlayVideo extends Canvas {
    private static final long serialVersionUID = 3671459899046831361L;

    public interface Listener {
        /**
         * start playing
         */
        public void start();

        /**
         * end reached
         */
        public void stop();

        /**
         * position changed
         * 
         * @param pos
         */
        public void position(float pos);
    }

    ArrayList<Listener> listeners = new ArrayList<PlayVideo.Listener>();

    Memfile mem;

    RandomAccessFile file;
    FileChannel fc;

    VLC vlc;

    VLCMediaPlayer m;

    libvlc_media_t fl;

    boolean pause = false;

    HierarchyListener hh = new HierarchyListener() {
        @Override
        public void hierarchyChanged(HierarchyEvent arg0) {
            if (PlayVideo.this.isShowing()) {
                if (PlayVideo.this.isPlaying()) {
                    float f = PlayVideo.this.getPosition();
                    PlayVideo.this.stop();
                    attach();
                    PlayVideo.this.play();
                    PlayVideo.this.setPosition(f);
                }
            }
        }
    };

    libvlc_callback_t evets = new libvlc_callback_t() {
        @Override
        public void libvlc_callback(IntByReference p_event, Pointer p_user_data) {
            switch (p_event.getValue()) {
            case libvlc_event_type_t.libvlc_MediaPlayerPlaying:
                for (Listener l : listeners) {
                    l.start();
                }
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerEndReached:
                for (Listener l : listeners) {
                    l.position(1.0f);
                    l.stop();
                }
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerPositionChanged:
                for (Listener l : listeners) {
                    float pos = LibVlc.INSTANCE.libvlc_media_player_get_position(m.getInstance());
                    l.position(pos);
                }
                break;
            default:
                break;
            }
        }
    };

    public PlayVideo() {
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    void create() {
        vlc = new VLC();

        m = new VLCMediaPlayer();

        fl = LibVlc.INSTANCE.libvlc_media_new_location(vlc.getInstance(), mem.getMrl());

        LibVlc.INSTANCE.libvlc_media_player_set_media(m.getInstance(), fl);

        libvlc_event_manager_t ev = LibVlc.INSTANCE.libvlc_media_player_event_manager(m.getInstance());
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerPlaying, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerEndReached, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerPositionChanged, evets, null);

        attach();

        addHierarchyListener(hh);

        setVolume(100);
    }

    void attach() {
        if (Platform.isMac())
            LibVlc.INSTANCE.libvlc_media_player_set_nsobject(m.getInstance(), Native.getComponentID(this));

        if (Platform.isLinux())
            LibVlc.INSTANCE.libvlc_media_player_set_xwindow(m.getInstance(), Native.getComponentID(this));

        if (Platform.isWindows())
            LibVlc.INSTANCE.libvlc_media_player_set_hwnd(m.getInstance(), Native.getComponentID(this));
    }

    public void open(final File f) {
        mem = new MemoryFile(f);

        create();
    }

    public void open(final InputStream is) {
        mem = new MemoryStream(is);

        create();
    }

    public void play() {
        LibVlc.INSTANCE.libvlc_media_player_play(m.getInstance());
    }

    public void stop() {
        LibVlc.INSTANCE.libvlc_media_player_stop(m.getInstance());
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        removeHierarchyListener(hh);

        if (fl != null) {
            LibVlc.INSTANCE.libvlc_media_release(fl);
            fl = null;
        }
        if (m != null) {
            m.close();
            m = null;
        }
        if (vlc != null) {
            vlc.close();
            vlc = null;
        }
    }

    public void pause(boolean pause) {
        this.pause = pause;
        LibVlc.INSTANCE.libvlc_media_player_set_pause(m.getInstance(), pause);
    }

    public boolean pause() {
        return pause;
    }

    public int getVolume() {
        return LibVlc.INSTANCE.libvlc_audio_get_volume(m.getInstance());
    }

    public void setVolume(int v) {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m.getInstance(), v);
    }

    public void setPosition(float f) {
        LibVlc.INSTANCE.libvlc_media_player_set_position(m.getInstance(), f);
    }

    public float getPosition() {
        return LibVlc.INSTANCE.libvlc_media_player_get_position(m.getInstance());
    }

    public boolean isPlaying() {
        return LibVlc.INSTANCE.libvlc_media_player_is_playing(m.getInstance());
    }

    /**
     * return media length;
     * 
     * @return ms
     */
    public long getLength() {
        return LibVlc.INSTANCE.libvlc_media_player_get_length(m.getInstance());
    }
}
