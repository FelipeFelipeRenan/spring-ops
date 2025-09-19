package com.felipe.inventory_service.consumer;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.felipe.inventory_service.dto.ProductEvent;
import com.felipe.inventory_service.service.InventoryService;

@Configuration
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final InventoryService inventoryService;

    public ProductEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Bean
    public Consumer<ProductEvent> productEvents() {
        return event -> {
            log.info("Recebido evento de produto: {}", event);
            if ("PRODUCT_CREATED".equals(event.eventType())) {
                // Se o evento for de criação, inicializa o estoque com 0
                inventoryService.setInitialStock(event.sku(), 0)
                        .doOnSuccess(success -> log.info("Estoque inicializado para o SKU: {}", event.sku()))
                        .subscribe();
            }
            //TODO: condicional para evento PRODUCT_DELETED, para limpar o estoque
        };
    }

}