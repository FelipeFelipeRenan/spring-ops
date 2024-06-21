package com.felipe.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<?> createOrder(@RequestBody Order order){
        if(orderService.createOrder(order)){
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } 
        return new ResponseEntity<>("Cannot create new order", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId){
        Order order =  orderService.getOrderById(orderId); 
        if(order != null){
            return new ResponseEntity<>(order, HttpStatus.FOUND);
        } 
        return new ResponseEntity<>("Cannot found the order with given ID: "+orderId, HttpStatus.NOT_FOUND);
    } 

    @GetMapping("/testing")
    public String testGet(){
        return "<h1>ola mundo, este é um teste do jenkins rsrsrs</h1>";
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable String orderId ){
        if(orderService.deleteOrderById(orderId)){
            return new ResponseEntity<> ("Deleted order with given order ID: "+orderId, HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot be able to delete order with given ID: "+orderId, HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable String orderId, @RequestBody Order order) {
        Order existingOrder = orderService.getOrderById(orderId);
        if (existingOrder != null) {
            order.setOrderId(orderId);
            orderService.updateOrder(order);
            return new ResponseEntity<>("Update successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<> ("Cannot found the order with the given ID: "+orderId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
