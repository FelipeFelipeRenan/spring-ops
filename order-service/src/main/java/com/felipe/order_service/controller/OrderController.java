package com.felipe.order_service.controller;

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

    @PostMapping
    public void createOrder(@RequestBody Order order){
        orderService.createOrder(order);
    }

    @GetMapping("/{orderID}")
    public Order getOrderById(@PathVariable String orderId){
        return orderService.getOrderById(orderId);
    } 

}
