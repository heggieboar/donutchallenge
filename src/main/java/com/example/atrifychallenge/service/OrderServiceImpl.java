package com.example.atrifychallenge.service;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.domain.PriorityQ;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

@Service
public class OrderServiceImpl implements OrderService {

    //One Q for all orders
    private final PriorityQ pq = PriorityQ.getInstance();
    private int premiumIndex = -1;
    private int cartSize = 0;
    private final int MAX_DONUT_COUNT = 50;


    @Override
    public void addOrderToQ(DonutOrder donutOrder) throws EntityExistsException {

        // add order to the Q
        //check if client order exists
        for (DonutOrder order : pq.getPriorityQ()) {
            if (order.getCustomerID().equals(donutOrder.getCustomerID())) {
                throw new EntityExistsException();
            }
        }

        if (donutOrder.getCustomerID() < 1001) {
            pq.getPriorityQ().add(++premiumIndex, donutOrder);
        } else {
            pq.getPriorityQ().add(donutOrder);
        }

        //this.sortQ();

        System.out.println();

    }

    private void sortQ() {
        //sort the list by Id and seconds

        Map<Boolean, List<DonutOrder>> sortedList = pq.getPriorityQ()
                .stream()
                .sorted(Comparator.comparing(DonutOrder::getStartTimeStamp)
                        .thenComparing(DonutOrder::getCustomerID))
                .collect(Collectors.partitioningBy(o -> o.getCustomerID() < 1001));

        System.out.println();

        /*for (DonutOrder o : sortedList.get()) {
            System.out.println("ID : " + o.getCustomerID() + " Time : " + o.getStartTimeStamp());
        }*/
    }

    @Override
    public PositionAndWaitTime checkPositionAndWait(Long clientId) {

        PositionAndWaitTime positionAndWaitTime = null;
        int position;

        for (DonutOrder donutOrder : pq.getPriorityQ()) {
            if (donutOrder.getCustomerID().equals(clientId)) {
                position = pq.getPriorityQ().indexOf(donutOrder);

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
        int result = 0;

        for (DonutOrder order : pq.getPriorityQ()) {
            result += order.getDonutQty();
            cartSize++;
            cart.add(order);
            if (result >= MAX_DONUT_COUNT) {
                break;
            }
        }
        System.out.println(result);
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
