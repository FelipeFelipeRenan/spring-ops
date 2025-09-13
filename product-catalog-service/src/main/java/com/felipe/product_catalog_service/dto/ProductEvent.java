package com.felipe.product_catalog_service.dto;

import java.math.BigDecimal;

public record ProductEvent(
    String eventType,
    String sku,
    String name,
    BigDecimal price
) {
    
}
