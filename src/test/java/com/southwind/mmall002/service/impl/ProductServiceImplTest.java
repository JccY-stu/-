package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/12 8:33
 * @Version 1.0
 */
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ProductMapper productMapper;

    @Test
    void findByName() {
        String name = "为";
        Product product = new Product();
        product.setName(name);

        //模糊查询 条件封装（前后都有 %name%）
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(product.getName()), "name", product.getName());
        List<Product> productList = productMapper.selectList(queryWrapper);

        //打印查看是否查找到
        for (Product product1 : productList) {
            System.out.println(product1.getId() + "  "+ product1.getName() + "  " +product1.getStock() + "  " + product1.getCategoryleveloneId());
        }
    }
}