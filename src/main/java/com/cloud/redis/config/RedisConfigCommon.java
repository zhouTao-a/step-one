package com.cloud.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.*;

/**
* redis乱码解决
 * 存取的序列化方式应该一致
*/
@Slf4j
@Configuration
public class RedisConfigCommon implements CachingConfigurer {


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
//		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//		// 创建自定义序列化器
//		RedisSerializer<Object> serializer = new RedisSerializer<Object>() {
//			private final StringRedisSerializer stringSerializer = new StringRedisSerializer();
//			private final GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//			@Override
//			public byte[] serialize(Object o) throws SerializationException {
//				if (o instanceof String) {
//					return stringSerializer.serialize((String) o);
//				}
//				return jsonSerializer.serialize(o);
//			}
//
//			@Override
//			public Object deserialize(byte[] bytes) throws SerializationException {
//				if (bytes == null) {
//					return null;
//				}
//				try {
//					return jsonSerializer.deserialize(bytes);
//				} catch (Exception e) {
//					return stringSerializer.deserialize(bytes);
//				}
//			}
//		};
//
//		// 设置值（value）的序列化采用自定义序列化器
//		redisTemplate.setValueSerializer(serializer);
//		redisTemplate.setHashValueSerializer(serializer);
//
//		// 设置键（key）的序列化采用 StringRedisSerializer
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}


	@Bean
	public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);

		// 创建自定义的二进制序列化器
		RedisSerializer<byte[]> serializer = new RedisSerializer<byte[]>() {
			@Override
			public byte[] serialize(byte[] t) throws SerializationException {
				return t;
			}

			@Override
			public byte[] deserialize(byte[] bytes) throws SerializationException {
				return bytes;
			}
		};

		// 设置值（value）的序列化采用自定义二进制序列化器
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);

		// 设置键（key）的序列化采用 StringRedisSerializer
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());

		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	// 序列化方法
	public byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();
	}

	// 反序列化方法
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}

}
