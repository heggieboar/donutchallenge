package com.example.atrifychallenge.service;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.domain.PriorityQ;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    //One Q for all orders
    private final PriorityQ pq = PriorityQ.getInstance();
    private int premiumIndex = -1;
    private int cartSize = 0;

    @Override
    public DonutOrder addOrderToQ(DonutOrder donutOrder) throws Exception {

        // prevent taking deliveries while inserting
        synchronized (pq.getPriorityQ()) {
            // add order to the Q
            //check if client order exists
            for (DonutOrder order : pq.getPriorityQ()) {
                if (order.getCustomerID().equals(donutOrder.getCustomerID())) {
                    throw new EntityExistsException();
                }
            }

            if(donutOrder.getCustomerID() > 20000) {
                log.error("ID exceeded {} ", donutOrder.getCustomerID());
                throw new Exception("ID exceeds 20000");
            }
            if(donutOrder.getDonutQty() > 50) {
                log.error("Donut quantity exceeded {} ", donutOrder.getDonutQty());
                throw new Exception("Donut Quantity should not exceed 50");
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
        }
        log.info("Size {} ", pq.getPriorityQ().size());
        return donutOrder;
    }

    private void sortQ() {
        //sort the list by Id and seconds

        pq.getPriorityQ().sort(Comparator.comparing(DonutOrder::isPremiumCustomer)
                .thenComparing(DonutOrder::getStartTimeStamp));
    }

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
    public Map<PositionAndWaitTime, DonutOrder> allDonutOrderInQ() {

        Map<PositionAndWaitTime, DonutOrder> ordersAndWaitingTimes = new HashMap<>();

        for (DonutOrder donutOrder : pq.getPriorityQ()) {
            int position = pq.getPriorityQ().indexOf(donutOrder) + 1;

            ordersAndWaitingTimes.put(new PositionAndWaitTime(donutOrder.getStartTimeStamp(),
                    position), donutOrder);

        }
        return ordersAndWaitingTimes;
    }

    @Override
    public Collection<DonutOrder> getNextDelivery() {
        List<DonutOrder> cart = new ArrayList<>();
        long result = 0;
        cartSize = 0;

        synchronized (pq.getPriorityQ()) {
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
        }
        return cart;
    }

    @Override
    public void cancelOrder(Long clientId) {
        //lock list
        //update index
        synchronized (pq.getPriorityQ()) {
            if (clientId < 1001) {
                premiumIndex--;
            }
            pq.getPriorityQ()
                    .removeIf(qOrder -> (qOrder.getCustomerID().equals(clientId)));
        }

    }
}
