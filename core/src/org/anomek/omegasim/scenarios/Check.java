package org.anomek.omegasim.scenarios;

import java.util.function.Supplier;

public class Check {
    public long timestamp;
    public Supplier<String> test;

    public Check(long timestamp, Supplier<String> test) {
        this.timestamp = timestamp;
        this.test = test;
    }
}

