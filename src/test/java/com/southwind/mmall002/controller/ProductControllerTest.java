package com.southwind.mmall002.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.vo.Seller.StoreProductVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/23 10:23
 * @Version 1.0
 */
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @Test
    void getMyStoreProducts() {
        //获取店铺商品列表
        List<StoreProductVO> productList = productService.findAllProductByStoreId(1);
        for (StoreProductVO storeProductVO : productList) {
            System.out.println();
        }
    }
}