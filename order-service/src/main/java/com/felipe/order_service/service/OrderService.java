package com.felipe.order_service.service;

import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.felipe.order_service.model.Order;
import com.felipe.order_service.model.ProductAvailabilityRequest;
import com.felipe.order_service.model.ProductAvailabilityResponse;
import com.felipe.order_service.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public ProductAvailabilityResponse checkProductAvailability(ProductAvailabilityResponse request){
        boolean available = true;
        double totalPrice = 0.0;

        for(Order item : request.getProducts()){
            Order product = orderRepository.getById(item.getOrderId());
            if (product == null || product.getQuantity() < item.getQuantity()) {
                available = false;
                break;
            }
            totalPrice += product.getPrice() * item.getQuantity();
        }

        ProductAvailabilityResponse response = new ProductAvailabilityResponse();
        response.setAvailable(available);
        response.setTotalPrice(totalPrice);
        response.setProducts(request.getProducts());
        return response;
    }

    @Cacheable("orders")
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackGetAllItems")    
    public List<Order> getALlItems(){
        return orderRepository.getAllItems();
    }

    @CachePut(value = "orders", key = "#order.id")
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackCreateOrder")
    public boolean createOrder(Order order){
        orderRepository.save(order);
        return true;
    }

    @Cacheable(value = "orders", key = "#orderId")
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackGetOrderById")

    public Order getOrderById(String orderId){
        return orderRepository.getById(orderId);
    }

    @CacheEvict(value = "orders", key = "#orderId")
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackDeleteOrderByI")
    public boolean deleteOrderById(String orderId){
        orderRepository.deleteById(orderId);
        return true;
    }

    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackUpdateOrder")
    public boolean updateOrder(Order order){
        orderRepository.update(order);
        return true;
    }
  // Métodos de fallback
  public List<Order> fallbackGetAllItems(Throwable t) {
    // Log a falha e fornecer uma resposta padrão
    System.err.println("Fallback triggered for getAllItems due to: " + t.getMessage());
    return Collections.emptyList();
}

public boolean fallbackCreateOrder(Order order, Throwable t) {
    // Log a falha e fornecer uma resposta padrão
    System.err.println("Fallback triggered for createOrder due to: " + t.getMessage());
    return false;
}

public Order fallbackGetOrderById(String orderId, Throwable t) {
    // Log a falha e fornecer uma resposta padrão
    System.err.println("Fallback triggered for getOrderById due to: " + t.getMessage());
    return new Order(); // ou retornar null ou uma instância vazia de Order
}

public boolean fallbackDeleteOrderById(String orderId, Throwable t) {
    // Log a falha e fornecer uma resposta padrão
    System.err.println("Fallback triggered for deleteOrderById due to: " + t.getMessage());
    return false;
}

public boolean fallbackUpdateOrder(Order order, Throwable t) {
    // Log a falha e fornecer uma resposta padrão
    System.err.println("Fallback triggered for updateOrder due to: " + t.getMessage());
    return false;
}
}
