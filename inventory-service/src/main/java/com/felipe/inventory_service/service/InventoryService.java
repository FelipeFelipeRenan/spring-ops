package com.felipe.inventory_service.service;

import org.springframework.stereotype.Service;

import com.felipe.inventory_service.repository.InventoryRepository;

import reactor.core.publisher.Mono;

@Service
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public Mono<Long> getStockBySku(String sku){
        return inventoryRepository.getStock(sku);
    }

    public Mono<Boolean> setInitialStock(String sku, long quantity){
        // Logica para permitir apenas quantidade positiva
        if(quantity < 0){
            return Mono.error(new IllegalArgumentException("Quantidade deve ser positiva"));
        }

        return inventoryRepository.setStock(sku, quantity);
    
    }
}
