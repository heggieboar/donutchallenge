package com.example.atrifychallenge.service;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface OrderService {

    DonutOrder addOrderToQ(DonutOrder donutOrder);
    PositionAndWaitTime checkPositionAndWait(Long clientId);
    Map<PositionAndWaitTime, DonutOrder> allDonutOrderInQ();
    Collection<DonutOrder> getNextDelivery();
    void cancelOrder(Long id);

}
