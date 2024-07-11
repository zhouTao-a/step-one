package com.cloud.redis.controller;

import com.cloud.redis.controller.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.System.out;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/test")
    public void test(){
        redisService.test();
    }

    @GetMapping("/stringGet")
    public void stringGet(){
        redisService.stringGet();
    }

    @GetMapping("/listGet")
    public void listGet(){
        redisService.listGet();
    }

    @GetMapping("/hashGet")
    public void hashGet(){
        redisService.hashGet();
    }

    @GetMapping("/setGet")
    public void setGet(){
        redisService.setGet();
    }

    @GetMapping("/sortSetGet")
    public void sortSetGet(){
        redisService.sortSetGet();
    }

}