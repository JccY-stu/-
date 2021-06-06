package com.southwind.mmall002.controller;

import com.southwind.mmall002.Init.GetProductList;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.service.ProductService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


/**
 * @Author Chengzhi
 * @Date 2021/5/24 19:13
 * @Version 1.0
 */
@SpringBootTest
class ProductViewsControllerTest {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsControllerTest.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    @Test
    public void toRedis() {
        //为所有的商品添加 redis 键值对，存储其浏览量;
        List<Product> allProductAndViewVO = GetProductList.allProductAndViewVO;
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 设置 redis 键值对
            redisTemplate.opsForValue().set(viewProductVO.getId(), viewProductVO.getViews());
            LOGGER.info(viewProductVO.getId() + "该商品存入 Redis 的浏览次数为：" + redisTemplate.opsForValue().get(viewProductVO.getId()));
        }
    }

    /**
     * 写回 MYSQL
     */
    @Test
    public void writeViewsToMQL() {
        List<Product> allProductAndViewVO = productService.list();
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 获取 redis 键值对
            Integer views = (Integer) redisTemplate.opsForValue().get(viewProductVO.getId());
            //重新装载到 列表里
            viewProductVO.setViews(views);
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
        }
        //写回 数据库
        boolean save = productService.saveBatch(allProductAndViewVO);
        LOGGER.info("写回数据库成功~" + save);
    }

}