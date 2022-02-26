package com.example.atrifychallenge.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DonutOrder implements Comparable<DonutOrder> {

    @Id
    private Long id;

    private Long customerID;
    private Long donutQty;
    private Date startTimeStamp;

    public DonutOrder(Long customerID, Long donutQty) {
        this.customerID = customerID;
        this.donutQty = donutQty;

        try {
            Thread.sleep(3000);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        this.startTimeStamp = new java.util.Date(System.currentTimeMillis());
        log.info("***************REMOVE" + new SimpleDateFormat("HH:mm:ss").format(this.startTimeStamp));
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

    public Date getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Date startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public boolean isPremiumCustomer() {
        return this.customerID > 1000;
    }


    @Override
    public int compareTo(DonutOrder o) {
        return 0;
    }
}
