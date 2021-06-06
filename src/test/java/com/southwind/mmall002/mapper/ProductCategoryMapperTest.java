package com.southwind.mmall002.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @Author Chengzhi
 * @Date 2021/5/9 12:24
 * @Version 1.0
 */
@SpringBootTest
class ProductCategoryMapperTest {

    @Autowired
    private ProductCategoryMapper mapper;

    @Test
    void test(){
        mapper.selectList(null).forEach(System.out::println);
    }

}