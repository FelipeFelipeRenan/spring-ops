package com.felipe.order_service.service;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.felipe.order_service.model.Order;
import com.felipe.order_service.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Cacheable("orders")
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackMethod")    
    public List<Order> getALlItems(){
        return orderRepository.getAllItems();
    }

    @CachePut(value = "orders", key = "#order.id")
    public boolean createOrder(Order order){
        orderRepository.save(order);
        return true;
    }

    @Cacheable(value = "orders", key = "#orderId")
    public Order getOrderById(String orderId){
        return orderRepository.getById(orderId);
    }

    @CacheEvict(value = "orders", key = "#orderId")
    public boolean deleteOrderById(String orderId){
        orderRepository.deleteById(orderId);
        return true;
    }

    public boolean updateOrder(Order order){
        orderRepository.update(order);
        return true;
    }

    public String fallbackMethod(){
        return "You got a bigass error";
    }

}
