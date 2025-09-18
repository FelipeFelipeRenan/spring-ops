package com.felipe.inventory_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felipe.inventory_service.dto.InventoryResponse;
import com.felipe.inventory_service.dto.UpdateInventoryRequest;
import com.felipe.inventory_service.service.InventoryService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/v1/api/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{sku}")
    public Mono<InventoryResponse> getStock(@PathVariable String sku){
        return inventoryService.getStockBySku(sku)
                .map(quantity -> new InventoryResponse(sku, quantity));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> setStock(@Valid @RequestBody UpdateInventoryRequest request) {
        return inventoryService.setInitialStock(request.sku(), request.quantity()).then();
    }
}
