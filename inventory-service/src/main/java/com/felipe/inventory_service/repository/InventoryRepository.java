package com.felipe.inventory_service.repository;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class InventoryRepository {

    // Template reativo para interagir com o Redis, com Chave String e Valor Long
    private final ReactiveRedisTemplate<String, Long> redisTemplate;
    // Prefixo para evitar colisões de chaves no Redis e organizar os dados
    public static final String KEY_PREFIX = "inventory::";

    public InventoryRepository(ReactiveRedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(String sku) {
        return KEY_PREFIX + sku;
    }

    public Mono<Long> getStock(String sku) {
        // Pega o valor da chave. Se a chave não existir, retorna 0.
        return redisTemplate.opsForValue().get(getKey(sku)).defaultIfEmpty(0L);
    }

    public Mono<Boolean> setStock(String sku, long quantity) {
        // Define o valor de uma chave
        return redisTemplate.opsForValue().set(getKey(sku), quantity);
    }

    public Mono<Long> incrementStock(String sku, long amount) {
        // Operação atomica de incremento
        return redisTemplate.opsForValue().increment(getKey(sku), amount);
    }

    public Mono<Long> decrementStock(String sku, long amount) {
        // Operação atomica de decremento
        return redisTemplate.opsForValue().decrement(getKey(sku), amount);
    }
}
