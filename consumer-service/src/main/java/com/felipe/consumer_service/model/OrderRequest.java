package com.felipe.consumer_service.model;

import java.util.List;

public class OrderRequest {

    private String clientId;
    private List<String> products;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

}
