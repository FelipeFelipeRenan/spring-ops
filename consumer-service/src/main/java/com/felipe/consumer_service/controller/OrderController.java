package com.felipe.consumer_service.controller;

import com.felipe.consumer_service.model.OrderRequest;
import com.felipe.consumer_service.service.OrderServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderServiceClient orderServiceClient;

    @Autowired
    public OrderController(OrderServiceClient orderServiceClient){
        this.orderServiceClient = orderServiceClient;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        // Envia a requisição para o serviço de ordem (Order Service) de forma assíncrona
        orderServiceClient.sendOrderRequest(request);
        return new ResponseEntity<>("Pedido recebido para processamento", HttpStatus.OK);
    }
}
