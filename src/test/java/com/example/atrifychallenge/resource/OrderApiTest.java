package com.example.atrifychallenge.resource;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.info.PositionAndWaitTime;
import com.example.atrifychallenge.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class OrderApiTest {

    private MockMvc mockMvc;

    int minId = 1;
    int maxId = 20;
    int minQty = 1;
    int maxQty = 30;
    Date time;
    List<DonutOrder> orders = new ArrayList<>();
    PositionAndWaitTime positionAndWaitTime;
    java.util.Map<PositionAndWaitTime, DonutOrder> waitAndPositionOrders = new HashMap<>();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderApi orderApi;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderApi).build();

        /*for(int count = 0; count < 10; count++) {
            DonutOrder anOrder = new DonutOrder(createIds(), createQtys());
            anOrder.setStartTimeStamp(createRandomDate());

            orders.add(anOrder);
            positionAndWaitTime =
                    new PositionAndWaitTime(anOrder.getStartTimeStamp(),
                            orders.indexOf(anOrder));
            waitAndPositionOrders.put(positionAndWaitTime, anOrder);
        }*/
    }

    long createIds() {
        return (int) Math.floor(Math.random() * (maxId - minId + 1) + minId);
    }

    long createQtys() {
        return (int) Math.floor(Math.random() * (maxQty - minQty + 1) + minQty);
    }

    Date createRandomDate() {
        Random r = new Random();
        long unixtime = (long) (1643210885 + r.nextDouble() * 60 * 60 * 24 * 365);
        return new Date(unixtime);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addNewOrder() {
        DonutOrder order = new DonutOrder(createIds(), createQtys());
        order.setStartTimeStamp(createRandomDate());

        Mockito.when(orderApi.addNewOrder(order)).thenReturn(order);

    }

    @Test
    void getOrders() {
        Map<PositionAndWaitTime, DonutOrder> listOrdersWithWait = new HashMap<>();
        Mockito.when(orderApi.getAllOrdersWithWaitingTime()).thenReturn(listOrdersWithWait);
    }

    public Map<PositionAndWaitTime, DonutOrder> getMap() {

        Map<PositionAndWaitTime, DonutOrder> positionAndWaitTimeDonutOrderMap
                = new HashMap<>();

        DonutOrder order1 = new DonutOrder(createIds(), createQtys());
        order1.setStartTimeStamp(createRandomDate());

        DonutOrder order2 = new DonutOrder(createIds(), createQtys());
        order1.setStartTimeStamp(createRandomDate());

        PositionAndWaitTime positionAndWaitTime1 =
                new PositionAndWaitTime(new Date(System.currentTimeMillis()), 1);

        PositionAndWaitTime positionAndWaitTime2 =
                new PositionAndWaitTime(new Date(System.currentTimeMillis()), 2);

        positionAndWaitTimeDonutOrderMap.put(positionAndWaitTime1, order1);
        positionAndWaitTimeDonutOrderMap.put(positionAndWaitTime2, order2);

        return positionAndWaitTimeDonutOrderMap;
    }

    @Test
    void getPositionAndWaitTime() {
    }

    @Test
    void fillCartForDelivery() {
    }

    @Test
    void deleteCancelOrder() {
    }
}