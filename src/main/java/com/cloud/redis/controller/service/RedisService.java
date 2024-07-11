package com.cloud.redis.controller.service;


public interface RedisService{

    void listGet();

    void hashGet();

    void setGet();

    void sortSetGet();

    void stringGet();

    void test();
}
