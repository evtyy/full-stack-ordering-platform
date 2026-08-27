package com.palette.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("Start creating redisTemplate object");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //set the redis connection factory object
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //set the redis key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //set the value serialization method
        // redisTemplate.setValueSerializer(new StringRedisSerializer());
        //apply the configuration
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
