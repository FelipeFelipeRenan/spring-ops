package com.felipe.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipe.order_service.model.Order;
import com.felipe.order_service.service.OrderService;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {
    
    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllItems(){
        return orderService.getALlItems();
    }

    @PostMapping
    public void createOrder(@RequestBody Order order){
        orderService.createOrder(order);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId){
        return orderService.getOrderById(orderId);
    } 

    @GetMapping("/testing")
    public String testGet(){
        return "<h1>ola mundo</h1>";
    }
}
