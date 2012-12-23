package com.github.axet.play.vlc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.LongByReference;

/**
 * 
 */
public class MemFile extends Structure {
    public MemFileOpen open;
    public MemFileClose close;
    public MemFileSize size;
    public MemFileSeek seek;
    public MemFileRead read;

    @Override
    protected List<?> getFieldOrder() {
        return Arrays.asList(new String[] { "open", "size", "close", "seek", "read" });
    }

    public MemFile() {
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
