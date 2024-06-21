package com.felipe.order_service.config;

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

@Configuration
@EnableCaching
public class RedisCacheConfig implements CachingConfigurer {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = cacheConfiguration();
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    private static class CustomCacheErrorHandler implements CacheErrorHandler {

        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleCacheGetError'");
        }

        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleCachePutError'");
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleCacheEvictError'");
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'handleCacheClearError'");
        }
       
    }
}
