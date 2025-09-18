package com.felipe.inventory_service.dto;

public record InventoryResponse(
        String sku,
        long quantity) {
}
