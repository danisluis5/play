package com.github.axet.play.vlc;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * 
 */
public class Memfile extends Structure {
    public MemfileOpen open;
    public MemfileClose close;
    public MemfileSize size;
    public MemfileSeek seek;
    public MemfileRead read;

    @Override
    protected List<?> getFieldOrder() {
        return Arrays.asList(new String[] { "open", "size", "close", "seek", "read" });
    }

    public Memfile() {
    }

    public long getOpen() {
        return getPointer().getLong(fieldOffset("open"));
    }

    public long getClose() {
        return getPointer().getLong(fieldOffset("close"));
    }

    public long getSize() {
        return getPointer().getLong(fieldOffset("size"));
    }

    public long getSeek() {
        return getPointer().getLong(fieldOffset("seek"));
    }

    public long getRead() {
        return getPointer().getLong(fieldOffset("read"));
    }
}
