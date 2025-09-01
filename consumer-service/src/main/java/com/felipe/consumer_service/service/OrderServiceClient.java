package com.felipe.consumer_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.consumer_service.model.OrderRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceClient {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; // Objeto para conversão JSON

    public OrderServiceClient(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderRequest(OrderRequest request) {
        try {
            // Converter o objeto para JSON
            String jsonRequest = objectMapper.writeValueAsString(request);
            // Envio da mensagem JSON para o RabbitMQ
            rabbitTemplate.convertAndSend("orderExchange", "orderRoutingKey", jsonRequest);
        } catch (JsonProcessingException e) {
            // Tratar exceção, se necessário
            e.printStackTrace();
        }
    }
}
