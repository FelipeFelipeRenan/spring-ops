package com.felipe.product_catalog_service.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String brand,
        String category,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        int version) {
}
