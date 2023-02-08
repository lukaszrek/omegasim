package org.anomek.omegasim.scenarios;

public class Event {
    public long timestamp;
    public Runnable runnable;

    public Event(long timestamp, Runnable runnable) {
        this.timestamp = timestamp;
        this.runnable = runnable;
    }
}
