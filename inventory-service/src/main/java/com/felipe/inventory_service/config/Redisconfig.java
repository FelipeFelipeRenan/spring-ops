package com.felipe.inventory_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Redisconfig {
    
    @Bean
    public ReactiveRedisTemplate<String, Long> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory ){
        // Serizalizador para as chaves (String)
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        // Serializador para os valores (Long), que sarão guardados como String no Redis
        GenericToStringSerializer<Long> valueSerializer = new GenericToStringSerializer<>(Long.class);        
        
        // Constroi o contexto de serialização
        RedisSerializationContext<String, Long> context = RedisSerializationContext
            .<String, Long>newSerializationContext(keySerializer)
            .hashKey(keySerializer)
            .value(valueSerializer)
            .hashValue(valueSerializer)
            .build();
    
        // Cria e retorna o template com a configuração especifica
        return new ReactiveRedisTemplate<>(factory, context);
    
    }
}
