package com.github.axet.vlc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface LibVlc extends Library {

    static LibVlc INSTANCE = (LibVlc) Native.loadLibrary("vlc", LibVlc.class);

    libvlc_media_t libvlc_media_new_path(libvlc_instance_t p_instance, String path);

    libvlc_instance_t libvlc_new(int argc, String[] argv);

    libvlc_media_player_t libvlc_media_player_new(libvlc_instance_t p_libvlc_instance);

    libvlc_event_manager_t libvlc_media_player_event_manager(libvlc_media_player_t p_mi);

    void libvlc_media_player_set_media(libvlc_media_player_t p_mi, libvlc_media_t p_md);

    void libvlc_media_player_set_hwnd(libvlc_media_player_t p_mi, Pointer drawable);

    void libvlc_media_player_set_nsobject(libvlc_media_player_t p_mi, long drawable);

    void libvlc_media_player_set_agl(libvlc_media_player_t p_mi, Pointer drawable);

    int libvlc_media_player_play(libvlc_media_player_t p_mi);

}
