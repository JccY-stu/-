package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @Author Chengzhi
 * @Date 2021/5/10 22:13
 * @Version 1.0
 */
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService service;

    @Test
    void test(){
        Orders orders = new Orders();
        orders.setUserId(1);
        orders.setLoginName("1");
        orders.setUserAddress("襄阳");
        orders.setCost(100.0f);
        service.save(orders);
    }

}