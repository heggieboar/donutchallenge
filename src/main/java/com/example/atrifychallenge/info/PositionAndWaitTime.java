package com.example.atrifychallenge.info;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;


public class PositionAndWaitTime {

    private final Long timePassed ;
    private final int position;

    public PositionAndWaitTime(Long start, int position) {
        this.timePassed = Instant.now().getEpochSecond() - start;
        this.position = position;
    }

    public Long getTimePassed() {
        return timePassed;
    }

    public int getPosition() {
        return position;
    }
}
