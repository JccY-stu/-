package com.southwind.mmall002.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author Chengzhi
 * @Date 2021/5/10 16:57
 * @Version 1.0
 *
 * 测试是否能拿到相应用户的地址
 */

@SpringBootTest
class UserAddressServiceTest {

    @Autowired
    private UserAddressService service;

    @Test
    void test(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",10);
        service.listByMap(map).forEach(System.out::println);
    }
}