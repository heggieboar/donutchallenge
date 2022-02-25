package com.example.atrifychallenge.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DonutOrder implements Comparable<DonutOrder> {

    @Id
    private Long id;

    private Long customerID;
    private Long donutQty;
    private Long startTimeStamp;

    public DonutOrder(Long customerID, Long donutQty) {
        this.customerID = customerID;
        this.donutQty = donutQty;
        this.startTimeStamp = Instant.now().getEpochSecond();
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public Long getDonutQty() {
        return donutQty;
    }

    public void setDonutQty(Long donutQty) {
        this.donutQty = donutQty;
    }

    public Long getId() {
        return id;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }


    @Override
    public int compareTo(DonutOrder o) {
        return 0;
    }
}
