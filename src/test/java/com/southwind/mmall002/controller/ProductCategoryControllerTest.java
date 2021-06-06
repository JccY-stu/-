package com.southwind.mmall002.controller;


import com.southwind.mmall002.service.ProductCategoryService;
import com.southwind.mmall002.vo.ProductCategoryVO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;



/**
 * @Author Chengzhi
 * @Date 2021/5/11 17:03
 * @Version 1.0
 */
class ProductCategoryControllerTest {

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    void list() {
        List<ProductCategoryVO> allProductCategoryVO = productCategoryService.getAllProductCategoryVO();
        System.out.println(allProductCategoryVO);
    }

    //模糊查询
    @Test
    void likelist() {
        String s = "水";
//        List<Product> userInfos = productCategoryService.getAllProductCategoryVO();
//        productCategoryService.
    }

    @Test
    void list2() {

        List<ProductCategoryVO> allProductCategoryVO = productCategoryService.getAllProductCategoryVO();
        LOGGER.info(String.valueOf(allProductCategoryVO));
    }
}