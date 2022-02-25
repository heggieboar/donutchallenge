package com.example.atrifychallenge.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Scope("singleton")
public class PriorityQ {

    private static PriorityQ singlePriorityQInstance = null;
    private final List<DonutOrder> donutQ;

    private PriorityQ() {
        this.donutQ = new ArrayList<>();
    }

    public static PriorityQ getInstance() {

        if(singlePriorityQInstance == null)
            singlePriorityQInstance = new PriorityQ();

            return singlePriorityQInstance;
    }

    public List<DonutOrder> getPriorityQ() {
        return this.donutQ;
    }

}
