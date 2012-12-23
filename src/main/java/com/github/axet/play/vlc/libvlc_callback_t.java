package com.github.axet.play.vlc;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface libvlc_callback_t extends StdCallCallback {
    void libvlc_callback(IntByReference p_event, Pointer p_user_data);
}
