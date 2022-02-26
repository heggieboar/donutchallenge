package com.example.atrifychallenge.service;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.domain.PriorityQ;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    //One Q for all orders
    private final PriorityQ pq = PriorityQ.getInstance();
    private int premiumIndex = -1;
    private int cartSize = 0;

    @Override
    public void addOrderToQ(DonutOrder donutOrder) throws EntityExistsException {

        // add order to the Q
        //check if client order exists
        for (DonutOrder order : pq.getPriorityQ()) {
            if (order.getCustomerID().equals(donutOrder.getCustomerID())) {
                throw new EntityExistsException();
            }
        }

        //get new premium index in case an order has been pull
        if (pq.getPriorityQ().size() > 0) {
            if (cartSize > 0) {
                premiumIndex = premiumIndex - cartSize;
                cartSize = 0;
            }
        } else {
            premiumIndex = -1; // if the Q is empty - reset our premium index
        }


        if (donutOrder.getCustomerID() < 1001) {
            pq.getPriorityQ().add(++premiumIndex, donutOrder);
        } else {
            pq.getPriorityQ().add(donutOrder);
        }

        log.info("Size {} ", pq.getPriorityQ().size());

    }

    /*private void sortQ() {
        //sort the list by Id and seconds

        Map<Boolean, List<DonutOrder>> sortedList = pq.getPriorityQ()
                .stream()
                .sorted(Comparator.comparing(DonutOrder::getStartTimeStamp)
                        .thenComparing(DonutOrder::getCustomerID))
                .collect(Collectors.partitioningBy(o -> o.getCustomerID() < 1001));
    }*/

    @Override
    public PositionAndWaitTime checkPositionAndWait(Long clientId) {

        PositionAndWaitTime positionAndWaitTime = null;
        int position;

        for (DonutOrder donutOrder : pq.getPriorityQ()) {
            if (donutOrder.getCustomerID().equals(clientId)) {
                position = pq.getPriorityQ().indexOf(donutOrder) + 1;

                positionAndWaitTime = new PositionAndWaitTime(donutOrder.getStartTimeStamp(),
                        position);
            }
        }
        return positionAndWaitTime;
    }

    @Override
    public Collection<DonutOrder> allDonutOrderInQ() {
        return pq.getPriorityQ();
    }

    @Override
    public Collection<DonutOrder> getNextDelivery() {
        List<DonutOrder> cart = new ArrayList<>();
        long result = 0;
        cartSize = 0;

        for (DonutOrder order : pq.getPriorityQ()) {

            result += order.getDonutQty();
            int MAX_DONUT_COUNT = 50;
            if (result > MAX_DONUT_COUNT) {
                //donut count has been acceded
                //rollback and remove the extra order
                result = result - order.getDonutQty();
                break;
            } else {
                cartSize++;
                cart.add(order);
            }
        }

        for (DonutOrder cartOrder : cart) {
            pq.getPriorityQ().removeIf(qOrder -> cartOrder.getCustomerID()
                    .equals(qOrder.getCustomerID()));
        }
        log.info("Total donuts in cart {} ", result);
        return cart;
    }

    @Override
    public boolean cancelOrder(Long clientId) {
        DonutOrder order = pq.getPriorityQ()
                .stream()
                .filter(client -> clientId.equals(client.getCustomerID()))
                .findFirst()
                .orElse(new DonutOrder());

        return pq.getPriorityQ().remove(order);
    }
}
