package com.example.atrifychallenge.resource;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import com.example.atrifychallenge.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderApi {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public void addNewOrder(@RequestBody DonutOrder newOrder) {
        orderService.addOrderToQ(newOrder);
    }

    @GetMapping("/orders")
    public ResponseEntity<Collection<DonutOrder>> getOrders() {
        return ResponseEntity.ok().body(orderService.allDonutOrderInQ());
    }

    @GetMapping("/orderstatus/{id}")
    public ResponseEntity<PositionAndWaitTime> getPositionAndWaitTime(@PathVariable("id") Long id) {

        PositionAndWaitTime positionAndWaitTime = orderService.checkPositionAndWait(id);

        System.out.println("Queue Position "
                + positionAndWaitTime.getPosition()
                + " Approximate Wait time"
                + positionAndWaitTime.getWaitTime());

        return ResponseEntity.ok().body(orderService.checkPositionAndWait(id));
    }

    @DeleteMapping("/delete/{clientid}")
    public ResponseEntity<?> deleteCancelOrder(@PathVariable("clientid") Long clientId) {
        orderService.cancelOrder(clientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
