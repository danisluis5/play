package com.github.axet.play;

import java.util.ArrayList;

public abstract class PlaySound {

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

    ArrayList<Listener> listeners = new ArrayList<PlaySound.Listener>();

    public PlaySound() {
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    abstract public void close();

    abstract public void setVolume(int v);

    abstract public void play();

    abstract public void stop();

}
