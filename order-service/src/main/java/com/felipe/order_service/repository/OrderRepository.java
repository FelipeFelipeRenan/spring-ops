package com.felipe.order_service.repository;

import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.felipe.order_service.model.Order;

@Repository
public class OrderRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public OrderRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void save(Order order){
        dynamoDBMapper.save(order);
    }

    public Order getById(String orderId){
        return dynamoDBMapper.load(Order.class, orderId);
    }
}
