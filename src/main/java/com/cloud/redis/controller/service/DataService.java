package com.cloud.redis.controller.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 使用布隆过滤器和缓存空值
 */
@Service
public class DataService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BloomFilter<CharSequence> bloomFilter;

    public DataService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        // 创建布隆过滤器，预计元素数量为1000000，误判率为0.01
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 10000, 0.001);
    }

    public String getData(String key) {
        // 先检查布隆过滤器
        if (!bloomFilter.mightContain(key)) {
            return null;
        }

        // 查询缓存
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue != null) {
            return cachedValue.equals("") ? null : cachedValue;
        }

        // 查询数据库
        String value = queryDatabase(key);
        if (value != null) {
            redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);
        } else {
            // 缓存空值，避免再次穿透到数据库
            redisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
        }

        return value;
    }

    private String queryDatabase(String key) {
        // 实际的数据库查询逻辑
        return null;
    }
}