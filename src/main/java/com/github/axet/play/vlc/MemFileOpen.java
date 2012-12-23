package com.github.axet.play.vlc;

import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface MemFileOpen extends StdCallCallback {
    /**
     * 
     * @param vlc
     * @return VLC_SUCCESS ; VLC_EGENERIC
     */
    int open();
}
