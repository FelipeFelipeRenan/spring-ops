package com.felipe.inventory_service.dtos;

public record InventoryService(
        String sku,
        long quantity) {
}
