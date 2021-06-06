package com.southwind.mmall002.mapper;

import com.southwind.mmall002.entity.Orders;
import com.southwind.mmall002.entity.kill.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/6/1 17:57
 * @Version 1.0
 */
@SpringBootTest
class StockMapperTest {

    @Autowired
    StockMapper stockMapper;

    @Autowired
    OrdersMapper ordersMapper;
    //获取商品信息
    @Test
    void test() {
        Stock stock = new Stock();
        stock.setId(1);
        Stock stock1 = stockMapper.selectCustomerById(stock.getId());
        System.out.println(stock1.getName());
    }

}