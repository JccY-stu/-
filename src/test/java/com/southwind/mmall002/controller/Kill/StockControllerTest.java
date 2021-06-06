package com.southwind.mmall002.controller.Kill;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.kill.Stock;
import com.southwind.mmall002.service.kill.StockService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Author Chengzhi
 * @Date 2021/6/5 11:21
 * @Version 1.0
 */
@SpringBootTest
class StockControllerTest {


    //测试添加秒杀商品
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    StockService stockService;

    //添加秒杀商品 设置过期时间
    @Test
    void addkillProduct(){
        int id = 1;
        redisTemplate.opsForValue().set("kill"+id,"id",180, TimeUnit.SECONDS);
    }

    @Test
    void getMyStoreProducts() {
        QueryWrapper wrapper = new QueryWrapper();
        //店铺ID 应当通过传入的方式
        wrapper.eq("store_id",1);
        //获取店铺商品列表
        List<Stock> killStockList = stockService.list(wrapper);
        for (Stock stock : killStockList) {
            System.out.println(stock.getName());
        }
    }
}