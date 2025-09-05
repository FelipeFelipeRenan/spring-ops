package com.felipe.product_catalog_service.config;

import java.lang.reflect.Method;
import java.util.StringJoiner;

import org.springframework.data.domain.Pageable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component("cacheKeyGenerator")
public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringJoiner joiner = new StringJoiner("::", target.getClass().getSimpleName() + "-", "");
        joiner.add(method.getName());

        for (Object param : params){
            if(param instanceof Pageable pageable){
                joiner.add("page=" + pageable.getPageNumber());
                joiner.add("size=" + pageable.getPageSize());
                pageable.getSort().forEach(order -> 
                    joiner.add("sort="+ order.getProperty() + ":" + order.getDirection())
                );
            }
        }
        return joiner.toString();
   }
    
}
