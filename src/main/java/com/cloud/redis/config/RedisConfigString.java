package com.cloud.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class RedisConfigString {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        RedisSerializer<Object> valueSerializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object o) throws SerializationException {
                if (o instanceof String) {
                    return stringRedisSerializer.serialize((String) o);
                }
                return genericJackson2JsonRedisSerializer.serialize(o);
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                String value = stringRedisSerializer.deserialize(bytes);
                try {
                    return genericJackson2JsonRedisSerializer.deserialize(bytes);
                } catch (Exception e) {
                    return value;
                }
            }
        };

        return new BloomFilterRedisTemplateString(factory, valueSerializer);
    }

}