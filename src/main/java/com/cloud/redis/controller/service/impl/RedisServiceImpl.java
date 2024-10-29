package com.cloud.redis.controller.service.impl;

import com.cloud.redis.controller.service.RedisService;
import com.cloud.redis.util.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static java.lang.System.*;
import static java.lang.System.out;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisUtils redisUtils;

    private static final String REDIS_TEST_KEY = "TEST:"; // 原文显示
//    private static final String REDIS_TEST_KEY = "STRING:"; // 字符串显示
//    private static final String REDIS_TEST_KEY = "BIN:"; // 二进制显示

    @Override
    public void listGet() {
        String key = REDIS_TEST_KEY + "str_list";
        // 清理之前的测试数据
        redisUtils.del(key);

        // 将数据存入 Redis 列表
        redisUtils.lSet(key, "T");
        redisUtils.lSet(key, "Another Value");
        redisUtils.lSet(key, 123);
        redisUtils.lSet(key, "fds");
        redisUtils.lSet(key, "Aner Vue");

        redisUtils.lUpdateIndex(key, 0, "hello world");
        // 从 Redis 列表中获取数据
        List<Object> list = redisUtils.lGet(key, 0, -1);


        // 使用 IntStream.range 打印每个元素及其索引
        IntStream.range(0, list.size()).forEach(i -> out.println("第 " + i + " 个元素：" + list.get(i)));

        // 打印获取到的数据
        out.println("Retrieved data: " + list);
    }

    @Override
    public void hashGet() {
        String key = REDIS_TEST_KEY + "user1001";

        // 设置单个字段
        redisUtils.setHashField(key, "name", "John");
        redisUtils.setHashField(key, "age", 30);

        // 获取单个字段
        String name = redisUtils.getHashField(key, "name");
        out.println("Name: " + name);

        // 批量设置字段
        Map<String, Object> userFields = new HashMap<>();
        userFields.put("email", "john@example.com");
        userFields.put("country", "USA");
        userFields.put("loginCount", 3);
        redisUtils.setMultipleHashFields(key, userFields);

        // 获取所有字段和值
        Map<Object, Object> user = redisUtils.getAllHashFields(key);
        out.println("User: " + user);

        // 增加字段值
        redisUtils.incrementHashField(key, "age", 1);

        // 检查字段是否存在
        boolean exists = redisUtils.hashFieldExists(key, "age");
        out.println("Age field exists: " + exists);

        // 增加字段值
        redisUtils.incrementHashField(key, "loginCount", 1);

        // 获取字段数量
        long fieldCount = redisUtils.getHashLength(key);
        out.println("Field count: " + fieldCount);
    }

    @Override
    public void setGet() {
        String key = REDIS_TEST_KEY + "userSet";

        // 添加成员到集合
        redisUtils.addMembers(key, "John", "Alice", "Bob");

        // 获取集合中的所有成员
        Set<Object> members = redisUtils.getMembers(key);
        out.println("Members: " + members);

        // 判断成员是否在集合中
        boolean isMember = redisUtils.isMember(key, "Alice");
        out.println("Is Alice a member: " + isMember);

        // 获取集合的成员数量
        long memberCount = redisUtils.getMemberCount(key);
        out.println("Member count: " + memberCount);

        // 随机移除并返回一个成员
        Object poppedMember = redisUtils.popMember(key);
        out.println("Popped member: " + poppedMember);

        // 获取集合的交集
        String otherSet = REDIS_TEST_KEY + "otherSet";
        redisUtils.addMembers(otherSet, "Alice", "Eve");
        Set<Object> intersect = redisUtils.getIntersect(key, otherSet);
        out.println("Intersect: " + intersect);

        // 获取集合的并集
        Set<Object> union = redisUtils.getUnion(key, otherSet);
        out.println("Union: " + union);

        // 获取集合的差集
        Set<Object> difference = redisUtils.getDifference(key, otherSet);
        out.println("Difference: " + difference);
    }

    @Override
    public void sortSetGet() {
        String key = REDIS_TEST_KEY + "userScores";

        // 添加成员到有序集合
        redisUtils. addZMember(key, 100, "John");
        redisUtils.addZMember(key, 200, "Alice");
        redisUtils.addZMember(key, 150, "Bob");

        // 获取有序集合中的所有成员
        Set<Object> members = redisUtils.getZMembers(key);
        out.println("Members: " + members);

        // 获取有序集合中所有成员及其分数
        Set<Object> membersWithScores = redisUtils.getZMembersWithScores(key);
        out.println("Members with scores: " + membersWithScores);

        // 获取有序集合中指定成员的分数
        Double score = redisUtils.getZScore(key, "Alice");
        out.println("Alice's score: " + score);

        // 获取有序集合中成员的排名
        Long rank = redisUtils.getZRank(key, "Alice");
        out.println("Alice's rank: " + rank);

        // 获取有序集合中成员的逆序排名
        Long reverseRank = redisUtils.getZReverseRank(key, "Alice");
        out.println("Alice's reverse rank: " + reverseRank);

        // 移除有序集合中的一个或多个成员
        redisUtils.removeZMembers(key, "John");

        // 为有序集合中的成员的分数加上增量
        Double newScore = redisUtils.incrementZScore(key, "Alice", 50);
        out.println("Alice's new score: " + newScore);

        // 获取有序集合的成员数量
        Long memberCount = redisUtils.getZMemberCount(key);
        out.println("Member count: " + memberCount);

        // 获取有序集合中指定分数区间的成员数量
        Long countByScoreRange = redisUtils.getZCountByScoreRange(key, 100, 200);
        out.println("Count by score range: " + countByScoreRange);
    }

    @Override
    public void stringGet() {
        String key = REDIS_TEST_KEY + "exampleKey";
        String value = "hello";

        // 设置值
        redisUtils.setValue(key, value);
        // 获取值
        Object retrievedValue = redisUtils.getValue(key);
        out.println("Retrieved value: " + retrievedValue);

        // 增加值
        String countKey = REDIS_TEST_KEY + "count";
        redisUtils.setValue(countKey, 1);
        Long incrementedValue = redisUtils.incrementValue(countKey);
        out.println("Incremented value: " + incrementedValue);

        // 减少值
        Long decrementedValue = redisUtils.decrementValue(countKey);
        out.println("Decremented value: " + decrementedValue);

        // 追加值
        redisUtils.appendValue(key, " world");
        Object appendedValue = redisUtils.getValue(key);
        out.println("Appended value: " + appendedValue);

        // 获取子字符串
        String subString = redisUtils.getRange(key, 0, 4);
        out.println("Substring: " + subString);

        // 设置子字符串
        redisUtils.setRange(key, "HELLO", 0);
        Object modifiedValue = redisUtils.getValue(key);
        out.println("Modified value: " + modifiedValue);

        // 同时设置多个 key-value 对
        Map<String, Object> keyValueMap = new HashMap<>();
        keyValueMap.put(REDIS_TEST_KEY + "key1", "value1");
        keyValueMap.put(REDIS_TEST_KEY + "key2", "value2");
        redisUtils.setMultipleValues(keyValueMap);

        // 获取多个 key 的值
        List<String> keys = Arrays.asList(REDIS_TEST_KEY + "key1", REDIS_TEST_KEY + "key2");
        List<Object> values = redisUtils.getMultipleValues(keys);
        out.println("Multiple values: " + values);
    }

    @Override
    public void test() {
        out.println("执行成功");
    }

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        // 添加元素
        list.add("A");
        list.add("B");
        list.add("C");

        // 创建一个线程，向列表中添加元素
        new Thread(() -> {
            list.add("D");
            out.println("Added D");
        }).start();

        // 迭代列表
        for (String item : list) {
            out.println("Item: " + item);
            try {
                Thread.sleep(100); // 模拟处理时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
