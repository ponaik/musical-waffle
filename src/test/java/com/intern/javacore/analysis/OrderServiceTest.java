package com.intern.javacore.analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        orderService.setOrderList(getPopulatedList());
    }

    @Test
    void testListOfUniqueCitiesWhereOrdersCameFrom() {
        List<String> cities = orderService.getListOfUniqueCitiesWhereOrdersCameFrom();

        assertEquals(2, cities.size());
        assertEquals("[Minsk, Brest]", cities.toString());
    }

    @Test
    void testTotalIncomeForAllCompletedOrders() {
        Double totalIncome = orderService.getTotalIncomeForAllCompletedOrders();

        assertEquals(344.48, totalIncome);
    }

    @Test
    void testMostPopularProductBySales() {
        Map.Entry<String, Long> mostPopularProduct = orderService.getMostPopularProductBySales();

        assertEquals("Mystery Novel", mostPopularProduct.getKey());
        assertEquals(4, mostPopularProduct.getValue());
    }

    @Test
    void testAverageCheckForSuccessfullyDeliveredOrders() {
        Double averageCheck = orderService.getAverageCheckForSuccessfullyDeliveredOrders();

        assertEquals(81.5575, averageCheck);
    }

    @Test
    void testCustomersWithMoreThan5Orders() {
        List<Customer> customersWithMoreThanFiveOrders = orderService.getCustomersWithMoreThan5Orders();

        assertEquals(1, customersWithMoreThanFiveOrders.size());
        assertEquals("Alice Novak", customersWithMoreThanFiveOrders.getFirst().getName());
    }


    private List<Order> getPopulatedList() {
        // Customers (using all-args constructors)
        Customer alice = new Customer(
                "CUST-" + UUID.randomUUID(),
                "Alice Novak",
                "alice.novak@example.com",
                LocalDateTime.of(2021, 3, 12, 10, 30),
                34,
                "Minsk"
        );

        Customer bob = new Customer(
                "CUST-" + UUID.randomUUID(),
                "Bob Petrov",
                "bob.petrov@example.com",
                LocalDateTime.of(2020, 7, 5, 9, 15),
                41,
                "Brest"
        );

        Customer carla = new Customer(
                "CUST-" + UUID.randomUUID(),
                "Carla Ivanova",
                "carla.ivanova@example.com",
                LocalDateTime.of(2022, 1, 20, 14, 45),
                28,
                "Minsk"
        );

        List<Order> result = new ArrayList<>();

        // Helper to create order items quickly (using all-args constructors)
        OrderItem headphones = new OrderItem("Wireless Headphones", 1, 129.99, Category.ELECTRONICS);
        OrderItem tshirt = new OrderItem("Cotton T-Shirt", 2, 19.99, Category.CLOTHING);
        OrderItem mystery = new OrderItem("Mystery Novel", 1, 12.50, Category.BOOKS);
        OrderItem nightLamp = new OrderItem("Night Lamp", 1, 24.00, Category.HOME);
        OrderItem serum = new OrderItem("Face Serum", 1, 35.00, Category.BEAUTY);
        OrderItem blocks = new OrderItem("Building Blocks Set", 1, 45.00, Category.TOYS);
        OrderItem ereader = new OrderItem("E-reader", 1, 89.99, Category.ELECTRONICS);
        OrderItem jeans = new OrderItem("Jeans", 1, 49.99, Category.CLOTHING);

        // Alice: create 7 orders to ensure more than 5 orders
        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 2, 10, 11, 5),
                alice, List.of(headphones), OrderStatus.DELIVERED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 3, 1, 16, 20),
                alice, List.of(tshirt), OrderStatus.SHIPPED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 4, 5, 9, 0),
                alice, List.of(mystery, nightLamp), OrderStatus.DELIVERED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 6, 17, 13, 30),
                alice, List.of(serum), OrderStatus.CANCELLED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 8, 2, 10, 10),
                alice, List.of(blocks, headphones), OrderStatus.PROCESSING));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 9, 12, 18, 5),
                alice, List.of(ereader), OrderStatus.NEW));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 10, 1, 12, 0),
                alice, List.of(jeans), OrderStatus.NEW));

        // Bob: different city, some overlapping products
        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 5, 22, 14, 45),
                bob, List.of(headphones), OrderStatus.DELIVERED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 7, 7, 9, 30),
                bob, List.of(new OrderItem("Mystery Novel", 3, 12.50, Category.BOOKS)), OrderStatus.SHIPPED));

        // Carla: same city as Alice, overlapping and distinct products
        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 2, 28, 8, 20),
                carla, List.of(new OrderItem("Night Lamp", 2, 24.00, Category.HOME)), OrderStatus.DELIVERED));

        result.add(new Order("ORD-" + UUID.randomUUID(), LocalDateTime.of(2023, 11, 3, 17, 40),
                carla, List.of(serum, new OrderItem("Jeans", 2, 49.99, Category.CLOTHING)), OrderStatus.PROCESSING));

        return result;
    }
}
