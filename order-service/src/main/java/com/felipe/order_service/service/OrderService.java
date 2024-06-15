package com.felipe.order_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipe.order_service.model.Order;
import com.felipe.order_service.repository.OrderRepository;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public void createOrder(Order order){
        orderRepository.save(order);
    }

    public Order getOrderById(String orderId){
        return orderRepository.getById(orderId);
    }
}
