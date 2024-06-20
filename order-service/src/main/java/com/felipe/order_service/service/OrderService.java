package com.felipe.order_service.service;

import java.util.List;

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

    public List<Order> getALlItems(){
        return orderRepository.getAllItems();
    }

    public boolean createOrder(Order order){
        orderRepository.save(order);
        return true;
    }

    public Order getOrderById(String orderId){
        return orderRepository.getById(orderId);
    }

    public boolean deleteOrderById(String orderId){
        orderRepository.deleteById(orderId);
        return true;
    }

    public boolean updateOrder(Order order){
        orderRepository.update(order);
        return true;
    }

}
