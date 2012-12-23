package com.github.axet.play;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_player_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public class PlaySoundFile extends PlaySound {

    MemFile mem;

    RandomAccessFile file;
    FileChannel fc;

    libvlc_media_player_t m;

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
            case libvlc_event_type_t.libvlc_MediaPlayerPositionChanged:
                for (Listener l : listeners) {
                    float pos = LibVlc.INSTANCE.libvlc_media_player_get_position(m);
                    l.position(pos);
                }
                break;
            default:
                break;
            }
        }
    };

    public PlaySoundFile() {
    }

    public void open(final File f) {
        mem = new MemFile();

        mem.open = new MemFileOpen() {
            @Override
            public int open() {
                try {
                    file = new RandomAccessFile(f, "r");
                    fc = file.getChannel();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.close = new MemFileClose() {
            @Override
            public int close() {
                try {
                    fc.close();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.size = new MemFileSize() {
            @Override
            public int size(LongByReference size) {
                try {
                    size.setValue(file.length());
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.seek = new MemFileSeek() {
            @Override
            public int seek(long pos) {
                try {
                    fc.position(pos);
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        mem.read = new MemFileRead() {
            @Override
            public int read(Pointer buf, int bufSize) {
                ByteBuffer b = ByteBuffer.allocate(bufSize);
                try {
                    int len = fc.read(b);

                    if (len == -1)
                        return 0;

                    byte[] bb = b.array();
                    buf.write(0, bb, 0, len);

                    return len;
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
            }
        };

        mem.write();

        String vlc_args[] = { "-I", "dumy", "--ignore-config" };
        libvlc_instance_t inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);

        libvlc_media_t fl = LibVlc.INSTANCE.libvlc_media_new_location(inst,
                "memfile://" + mem.getOpen() + "/" + mem.getClose() + "/" + mem.getSize() + "/" + mem.getSeek() + "/"
                        + mem.getRead());

        m = LibVlc.INSTANCE.libvlc_media_player_new(inst);

        LibVlc.INSTANCE.libvlc_media_player_set_media(m, fl);

        libvlc_event_manager_t ev = LibVlc.INSTANCE.libvlc_media_player_event_manager(m);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerEndReached, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerPositionChanged, evets, null);
    }

    public void play() {
        setVolume(100);
        LibVlc.INSTANCE.libvlc_media_player_play(m);
    }

    public void stop() {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m, 0);
        LibVlc.INSTANCE.libvlc_media_player_stop(m);
    }

    public void close() {
    }

    @Override
    public void setVolume(int v) {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m, v);
    }
}
