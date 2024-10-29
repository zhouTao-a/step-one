package com.cloud.redis.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BloomFilterRedisTemplate extends RedisTemplate<String, Object> {

    private final BloomFilter<String> bloomFilter;
    private final Random random = new Random();
    private final RedisSerializer<Object> valueSerializer;

    public BloomFilterRedisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer<Object> valueSerializer) {
        setConnectionFactory(connectionFactory);
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000000, 0.01);
        this.valueSerializer = valueSerializer;

        // 设置序列化器
        setKeySerializer(new StringRedisSerializer());
        setValueSerializer(this.valueSerializer);
        setHashKeySerializer(new StringRedisSerializer());
        setHashValueSerializer(this.valueSerializer);
        afterPropertiesSet();
    }

    @Override
    public ValueOperations<String, Object> opsForValue() {
        return new BloomFilterValueOperations(super.opsForValue());
    }

    private class BloomFilterValueOperations implements ValueOperations<String, Object> {
        private final ValueOperations<String, Object> delegate;

        BloomFilterValueOperations(ValueOperations<String, Object> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void set(String key, Object value) {
            bloomFilter.put(key);
            delegate.set(key, value);
        }

        @Override
        public Boolean setIfAbsent(String key, Object value) {
            bloomFilter.put(key);
            return delegate.setIfAbsent(key, value);
        }

        @Override
        public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
            bloomFilter.put(key);
            return delegate.setIfAbsent(key, value, timeout, unit);
        }

        @Override
        public Boolean setIfAbsent(String key, Object value, Duration timeout) {
            bloomFilter.put(key);
            return delegate.setIfAbsent(key, value, timeout);
        }

        @Override
        public Boolean setIfPresent(String key, Object value) {
            return delegate.setIfPresent(key, value);
        }

        @Override
        public Boolean setIfPresent(String key, Object value, long timeout, TimeUnit unit) {
            return delegate.setIfPresent(key, value, timeout, unit);
        }

        @Override
        public Boolean setIfPresent(String key, Object value, Duration timeout) {
            return delegate.setIfPresent(key, value, timeout);
        }

        /**
         * 处理缓存击穿和缓存穿透
         * @param key
         * @return
         */
        public Object get(Object key) {
            // 1. 检查 key 是否为 String 类型
            if (!(key instanceof String)) {
                return delegate.get(key);
            }
            String stringKey = (String) key;

            // 2. 使用布隆过滤器检查 key 是否可能存在
            if (!bloomFilter.mightContain(stringKey)) {
                return null;
            }

            // 3. 从 Redis 中获取值
            Object value = delegate.get(stringKey);
            if (value != null) {
                return value;
            }

            // 4. 处理缓存击穿
            return handleCachePenetration(stringKey);
        }

        private Object handleCachePenetration(String key) {
            String lockKey = "lock:" + key;
            Boolean acquired = delegate.setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
            if (!Boolean.TRUE.equals(acquired)) {
                // 等待一段时间后重试
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return get(key);
            }

            try {
                // 双重检查
                Object value = delegate.get(key);
                if (value == null) {
                    // 查询数据库并更新缓存
                    value = queryDatabase(key);
                    if (value == null || value.equals("")) {
                        // 缓存空值，避免再次穿透到数据库
                        delegate.set(key, "", Duration.ofMinutes(5));
                    } else {
                        // 添加随机过期时间，防止缓存雪崩
                        long randomTimeout = 3600 + random.nextInt(300);
                        delegate.set(key, value, Duration.ofSeconds(randomTimeout));
                    }
                }
                return value;
            } finally {
                delegate.getOperations().delete(lockKey);
            }
        }

        private Object queryDatabase(String key) {
            // 模拟数据库查询
            return "value from database";
        }

        @Override
        public Object getAndDelete(String key) {
            return delegate.getAndDelete(key);
        }

        @Override
        public Object getAndExpire(String key, long timeout, TimeUnit unit) {
            return delegate.getAndExpire(key, timeout, unit);
        }

        @Override
        public Object getAndExpire(String key, Duration timeout) {
            return delegate.getAndExpire(key, timeout);
        }

        @Override
        public Object getAndPersist(String key) {
            return delegate.getAndPersist(key);
        }

        @Override
        public Object getAndSet(String key, Object value) {
            bloomFilter.put(key);
            return delegate.getAndSet(key, value);
        }

        @Override
        public List<Object> multiGet(Collection<String> keys) {
            return delegate.multiGet(keys);
        }

        @Override
        public Long increment(String key) {
            bloomFilter.put(key);
            return delegate.increment(key);
        }

        @Override
        public Long increment(String key, long delta) {
            bloomFilter.put(key);
            return delegate.increment(key, delta);
        }

        @Override
        public Double increment(String key, double delta) {
            bloomFilter.put(key);
            return delegate.increment(key, delta);
        }

        @Override
        public Long decrement(String key) {
            bloomFilter.put(key);
            return delegate.decrement(key);
        }

        @Override
        public Long decrement(String key, long delta) {
            bloomFilter.put(key);
            return delegate.decrement(key, delta);
        }

        @Override
        public Integer append(String key, String value) {
            bloomFilter.put(key);
            return delegate.append(key, value);
        }
        @Override
        public String get(String key, long start, long end) {
            return delegate.get(key, start, end);
        }

        @Override
        public void set(String key, Object value, long offset) {
            bloomFilter.put(key);
            delegate.set(key, value, offset);
        }

        @Override
        public Long size(String key) {
            return delegate.size(key);
        }

        @Override
        public Boolean setBit(String key, long offset, boolean value) {
            bloomFilter.put(key);
            return delegate.setBit(key, offset, value);
        }

        @Override
        public Boolean getBit(String key, long offset) {
            return delegate.getBit(key, offset);
        }

        @Override
        public void multiSet(Map<? extends String, ?> map) {
            map.keySet().forEach(bloomFilter::put);
            delegate.multiSet(map);
        }

        @Override
        public Boolean multiSetIfAbsent(Map<? extends String, ?> map) {
            map.keySet().forEach(bloomFilter::put);
            return delegate.multiSetIfAbsent(map);
        }

        @Override
        public List<Long> bitField(String key, BitFieldSubCommands subCommands) {
            return delegate.bitField(key, subCommands);
        }

        @Override
        public RedisOperations<String, Object> getOperations() {
            return BloomFilterRedisTemplate.this;
        }

        /**
         * 设置随机过期时间来处理缓存雪崩问题
         * @param key
         * @param value
         * @param timeout
         * @param unit
         */
        @Override
        public void set(String key, Object value, long timeout, TimeUnit unit) {
            bloomFilter.put(key);
            long randomTimeout = timeout + random.nextInt(300); // 添加随机过期时间
            delegate.set(key, value, randomTimeout, unit);
        }

        @Override
        public void set(String key, Object value, Duration timeout) {
            bloomFilter.put(key);
            Duration randomTimeout = timeout.plusSeconds(random.nextInt(300)); // 添加随机过期时间
            delegate.set(key, value, randomTimeout);
        }

    }
}