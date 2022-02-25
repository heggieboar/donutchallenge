package com.example.atrifychallenge.info;


import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class PositionAndWaitTime {

    private final long waitTime;
    private final int position;


    public PositionAndWaitTime(Date startTime, int position) {
        this.position = position;
        this.waitTime = new Date(System.currentTimeMillis()).getTime() - startTime.getTime();
    }

    public long getWaitTime() {
        return waitTime;
    }

    public int getPosition() {
        return position;
    }

    public String getTimeDifference(long waitingTime) {
       return (waitingTime / (1000 *60)) % 60 + "Min" + (waitingTime / 1000) % 60 + " Sec";
    }

}
