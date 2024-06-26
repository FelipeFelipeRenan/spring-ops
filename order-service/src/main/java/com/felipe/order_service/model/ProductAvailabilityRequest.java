package com.felipe.order_service.model;

import java.util.List;

public class ProductAvailabilityRequest {

    private List<Order> products;

    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }

    
}
