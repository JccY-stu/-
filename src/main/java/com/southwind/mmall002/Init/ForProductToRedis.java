package com.southwind.mmall002.Init;

import com.southwind.mmall002.controller.ProductCategoryController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/5/27 10:03
 * @Version 1.0
 */
@Component
public class ForProductToRedis {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ForProductToRedis.class);

    @Autowired
    ProductService productService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 为所有的商品添加键值对 当有新商品加入时，每加入一次，则调用一次此方法
     * @return
     */
    public Boolean toRedis() {
        //为所有的商品添加 redis 键值对，存储其浏览量;
        List<Product> allProductAndViewVO = GetProductList.allProductAndViewVO;
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 设置 redis 键值对
            redisTemplate.opsForValue().set(viewProductVO.getId(), viewProductVO.getViews());
            LOGGER.info(viewProductVO.getId() + "该商品存入 Redis 的浏览次数为：" + redisTemplate.opsForValue().get(viewProductVO.getId()));
        }
        return true;
    }
}
