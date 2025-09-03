package com.felipe.product_catalog_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @NotBlank(message = "Product name cannot be blank") @Size(max = 255) String name,

        String description,

        @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.01") BigDecimal price,

        String brand,
        String category,

        Boolean isActive

) {

}
