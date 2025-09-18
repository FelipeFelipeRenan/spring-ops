package com.felipe.inventory_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateInventoryRequest(
    @NotBlank(message = "SKU não pode estar em branco")
    String sku,

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    long quantity
) {}
