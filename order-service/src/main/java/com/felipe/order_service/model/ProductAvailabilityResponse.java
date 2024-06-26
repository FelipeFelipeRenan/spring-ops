package com.felipe.order_service.model;

import java.util.List;

public class ProductAvailabilityResponse {
    
    private boolean available;
    private double totalPrice;
    private List<Order> products;
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public List<Order> getProducts() {
        return products;
    }
    public void setProducts(List<Order> products) {
        this.products = products;
    }


}
