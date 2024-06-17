package com.felipe.order_service.repository;

import java.util.List;

import javax.swing.Spring;

import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.felipe.order_service.model.Order;

@Repository
public class OrderRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public OrderRepository(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper = dynamoDBMapper;
    }
    
    public List<Order> getAllItems(){
        return dynamoDBMapper.scan(Order.class, new DynamoDBScanExpression());
    }

    public void save(Order order){
        dynamoDBMapper.save(order);
    }

    public Order getById(String orderId){
        return dynamoDBMapper.load(Order.class, orderId);
    }

    public void deleteById(String orderId){
        Order order = getById(orderId);
        if (order != null) {
            dynamoDBMapper.delete(order);
            
        }
    }

    public void update(Order order){
        save(order);
    }
}
