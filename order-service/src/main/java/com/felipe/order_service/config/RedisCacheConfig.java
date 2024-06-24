package com.felipe.order_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisCacheConfig implements CachingConfigurer {

    @Bean
    public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisCacheConfiguration cacheConfiguration = cacheConfiguration(objectMapper);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    private static class CustomCacheErrorHandler implements CacheErrorHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(CustomCacheErrorHandler.class);
    
        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            LOGGER.error("Error getting data from cache for key {}: {}", key, exception.getMessage());
        }
    
        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
            LOGGER.error("Error putting data into cache for key {}: {}", key, exception.getMessage());
        }
    
        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
            LOGGER.error("Error evicting data from cache for key {}: {}", key, exception.getMessage());
        }
    
        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            LOGGER.error("Error clearing cache: {}", exception.getMessage());
        }
    }}
