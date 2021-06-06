package com.southwind.mmall002.service.impl;

import com.southwind.mmall002.service.ProductCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Chengzhi
 * @Date 2021/5/8 9:34
 * @Version 1.0
 */
@SpringBootTest//若不加，则不去IOC里面找，必须手动加载运行
class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryService service;
    @Test
    void getAllProductCategoryVO() {
        service.getAllProductCategoryVO();
    }
}