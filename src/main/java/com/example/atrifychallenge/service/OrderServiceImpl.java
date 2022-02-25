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


    @Override
    public void addOrderToQ(DonutOrder donutOrder) throws EntityExistsException {

        // add order to the Q
        //check if client order exists
        for (DonutOrder order : pq.getPriorityQ()) {
            if (order.getCustomerID().equals(donutOrder.getCustomerID())) {
                throw new EntityExistsException();
            }
        }
        pq.getPriorityQ().add(donutOrder);
        this.sortQ();

    }

    private void sortQ() {
        //sort the list by Id and seconds

        Map<Boolean, List<DonutOrder>> sortedList =  pq.getPriorityQ()
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

        long MAX_SIZE = 50L;
        long result = 0;
        Collection<DonutOrder> newList = new ArrayList<>();
        Collection<DonutOrder> orders = this.allDonutOrderInQ();
        for (DonutOrder anOrder : orders) {

            result += anOrder.getDonutQty();
            newList.add(anOrder);
            if (result > 50) {
                break;
            }
        }

        return newList;
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
