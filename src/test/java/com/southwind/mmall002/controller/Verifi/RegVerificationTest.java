package com.southwind.mmall002.controller.Verifi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/12 22:35
 * @Version 1.0
 */
@SpringBootTest
class RegVerificationTest {


    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void verfication() {
        String loginName  = "";
        String s = (String) redisTemplate.opsForValue().get(loginName);
        System.out.println("获取其值为：" + s);

    }
}