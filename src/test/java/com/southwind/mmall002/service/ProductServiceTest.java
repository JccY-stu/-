package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Chengzhi
 * @Date 2021/5/8 22:08
 * @Version 1.0
 */
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @Test
    void test(){
        Map<String,Object> map = new HashMap<>();
        map.put("categorylevelthree_id",655);
        service.listByMap(map).forEach(System.out::println);
    }

    @Test
    void get(){
        System.out.println(service.getById(733));
    }

    @Test
    void findById(){
        Integer id = 1000;
        Product productByIdList = service.getById(id);
        if(productByIdList == null){
            //未查到则返回主页面
            System.out.println("未查找到");
        }
    }
}