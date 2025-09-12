package com.felipe.product_catalog_service.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String brand,
        String category,
        boolean isActive,
        OffsetDateTime  createdAt,
        OffsetDateTime updatedAt,
        int version) {
}
