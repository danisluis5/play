package com.github.axet.play.vlc;

import com.sun.jna.ptr.LongByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface MemfileSize extends StdCallCallback {
    /**
     * 
     * @param vlc
     * @return VLC_SUCCESS ; VLC_EGENERIC
     */
    int size(LongByReference size);
}
