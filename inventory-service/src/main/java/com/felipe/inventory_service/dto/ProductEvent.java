package com.felipe.inventory_service.dto;

import java.math.BigDecimal;

public record ProductEvent(
    String eventType,
    String sku,
    String name,
    BigDecimal price
) {
    
}
