package com.southwind.mmall002.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/9 17:38
 * @Version 1.0
 */
@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    void findAllCartByUserId(){
        cartService.findAllCartVOByUserId(10).forEach(System.out::println);
    }
}