package com.example.atrifychallenge.service;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface OrderService {

    void addOrderToQ(DonutOrder donutOrder);
    PositionAndWaitTime checkPositionAndWait(Long clientId);
    Collection<DonutOrder> allDonutOrderInQ();
    Collection<DonutOrder> getNextDelivery();
    boolean cancelOrder(Long id);

}
