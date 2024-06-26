package com.felipe.consumer_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String ORDER_QUEUE = "orderQueue";
    public static final String ORDER_EXCHANGE = "orderExchange";
    public static final String ORDER_ROUTING_KEY = "orderRoutingKey";

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE, false);
    }

    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue orderQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(orderExchange).to(orderExchange).with(ORDER_ROUTING_KEY);
    }
}
