package com.felipe.product_catalog_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
    @NotBlank(message = "SKU cannot be blank")
    @Size(max = 100, message = "SKU must be up to 100 characters")
    String sku, 

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Product name must be up to 255 characters")
    String name,

    String description,

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    BigDecimal price,

    String brand,
    String category

) {}
