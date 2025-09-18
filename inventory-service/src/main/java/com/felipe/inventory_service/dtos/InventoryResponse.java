package com.felipe.inventory_service.dtos;

public record InventoryResponse(
        String sku,
        long quantity) {
}
