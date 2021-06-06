package com.southwind.mmall002.Init;

import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/5/25 21:10
 * @Version 1.0
 *
 * 初始化 商品列表
 */

@Component
public class GetProductList {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(GetProductList.class);
    public static List<Product> allProductAndViewVO = new ArrayList<>();

    @Autowired
    private ProductService productService;

    @Autowired
    private ForProductToRedis forProductToRedis;

    /**
     * 项目启动时初始化商品列表
     */
    @PostConstruct
    public void init() {
        //将所有商品信息设置为全局变量
        allProductAndViewVO = productService.list();
        LOGGER.info("初始加载的商品数量为：" + allProductAndViewVO.size());
        //将商品---浏览量 键值对 存入 Redis
        Boolean aBoolean = forProductToRedis.toRedis();
        LOGGER.info("商品---浏览量 键值对初始化成功！" + aBoolean);
    }
}
