package com.intern.javacore.analysis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderService {
    private List<Order> orderList;

    public OrderService() {}

    public void setOrderList(List<Order> orderList) { this.orderList = orderList; }

    public List<String> getListOfUniqueCitiesWhereOrdersCameFrom() {
        return orderList.stream()
                .map(Order::getCustomer)
                .map(Customer::getCity)
                .distinct()
                .toList();
    }

    public double getTotalIncomeForAllCompletedOrders() {
        return orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0.0, Double::sum);
    }

    public Map.Entry<String, Long> getMostPopularProductBySales() {
        Map<String, Long> productsBySales = orderList.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.summingLong(OrderItem::getQuantity) // Collectors.counting() to disregard quantity
                ));

        Map.Entry<String, Long> mostPopularProduct =
                productsBySales.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElseThrow();

        return mostPopularProduct;
    }

    public double getAverageCheckForSuccessfullyDeliveredOrders() {
        return orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .map(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .average()
                        .orElse(0))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow();
    }

    public List<Customer> getCustomersWithMoreThan5Orders() {
        Map<Customer, Long> ordersPerCustomer = orderList.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomer,
                        Collectors.counting()
                ));

        List<Customer> customersWithMoreThanFiveOrders = ordersPerCustomer.entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();

        return customersWithMoreThanFiveOrders;
    }
}
