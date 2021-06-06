package com.southwind.mmall002.controller;


import com.southwind.mmall002.controller.file.UploadController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.ProductCategoryService;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.impl.views.ProductViewsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Controller
@RequestMapping("/productCategory")
public class ProductCategoryController {

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductViewsServiceImpl productViewsService;

    /**
     * 渲染 商城主页面
     * @param session
     * @return
     */
    @GetMapping("/list")
    public ModelAndView List(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");

        //商品信息
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        //把浏览量存在 redis 里面，每次都从 redis 里面取
        Map viewsList = productViewsService.getAllViewsFormRedis();
        modelAndView.addObject("viewsList",viewsList);

        LOGGER.info("商品数量为：" + viewsList.size());
        //“我的购物车”信息
        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else{
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        // 商品浏览量信息
        return modelAndView;
    }
}

