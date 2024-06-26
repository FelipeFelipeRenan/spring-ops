package com.felipe.consumer_service.model;

public class OrderResponse {

    private boolean success;
    private String message;

    // Getters e setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
