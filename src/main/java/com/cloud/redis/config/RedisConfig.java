package com.cloud.redis.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
* redis乱码解决
 * 存取的序列化方式应该一致
*/
@Slf4j
@Configuration
public class RedisConfig implements CachingConfigurer {


	/**
	 * RedisTemplate配置
	 * 高效的 JSON 序列化和反序列化，使用阿里巴巴的 Fastjson 库
	 */
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(factory);
//
//		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
//
//		// 设置值（value）的序列化采用FastJsonRedisSerializer。
//		redisTemplate.setValueSerializer(fastJsonRedisSerializer);
//		redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
//		// 设置键（key）的序列化采用StringRedisSerializer。
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}

	/**
	 * RedisTemplate配置
	 * 高效的 JSON 序列化和反序列化，使用阿里巴巴的 Fastjson 库
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);

		// 创建 StringRedisSerializer
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

		// 创建 GenericJackson2JsonRedisSerializer，用于处理复杂对象
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();


		// 设置 key 的序列化方式
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);

		// 设置 value 的序列化方式
		redisTemplate.setValueSerializer(new RedisSerializer<Object>() {
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
		});

		redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}


	/**
	 * 使用 JDK 的序列化机制，适用于 Java 对象的序列化和反序列化。
	 * @param factory
	 * @return
	 */
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(factory);
//
//		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
//
//		// 设置值（value）的序列化采用JdkSerializationRedisSerializer。
//		redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
//		redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
//		// 设置键（key）的序列化采用StringRedisSerializer。
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}

	//  使用 Jackson2JsonRedisSerializer 使用 Jackson 进行 JSON 序列化和反序列化，通常与 Spring 集成较好。
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(factory);
//
//		// 配置Jackson ObjectMapper
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//		// 使用 Jackson2JsonRedisSerializer 并配置 ObjectMapper
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//		// 设置值（value）的序列化采用 Jackson2JsonRedisSerializer
//		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//		// 设置键（key）的序列化采用 StringRedisSerializer
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}
}






