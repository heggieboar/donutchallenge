package com.example.atrifychallenge;

import com.example.atrifychallenge.domain.DonutOrder;
import com.example.atrifychallenge.service.OrderServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Random;

@SpringBootApplication
public class AtrifyChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtrifyChallengeApplication.class, args);
    }

    @Bean
    CommandLineRunner run(OrderServiceImpl service) {

        return args -> {
            service.addOrderToQ(new DonutOrder(10001L, 30L));
            service.addOrderToQ(new DonutOrder(5L, 13L));
            service.addOrderToQ(new DonutOrder(85L, 21L));
            service.addOrderToQ(new DonutOrder(8855L, 15L));
            service.addOrderToQ(new DonutOrder(1L, 30L));
            service.addOrderToQ(new DonutOrder(885L, 10L));
            service.addOrderToQ(new DonutOrder(1515L, 20L));
        };
    }
}
