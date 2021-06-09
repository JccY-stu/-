package com.southwind.mmall002.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.controller.file.UploadController;
import com.southwind.mmall002.entity.Cart;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 增加 购物车相应商品数量
     * 减少 商品存货
     * @param productId
     * @param price
     * @param quantity
     * @param session
     * @return 跳转 购物车页面
     */
    @GetMapping("/add/{productId}/{storeId}/{storeName}/{price}/{quantity}")
    public String add(

            @PathVariable("productId") Integer productId,
            @PathVariable("storeId")Integer storeId,
            @PathVariable("storeName")String storeName,
            @PathVariable("price") Float price,
            @PathVariable("quantity") Integer quantity,

            HttpSession session
    ) {
        LOGGER.info("该订单商品的商品ID为：" + productId);
        LOGGER.info("该订单商品所属店铺ID为：" + storeId);
        LOGGER.info("该订单商品的店铺名称为：" + storeName);
        LOGGER.info("该订单商品的价格为：" + price);
        LOGGER.info("该订单商品的数量为：" + quantity);
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setStoreId(storeId);
        cart.setStoreName(storeName);
        cart.setQuantity(quantity);
        cart.setCost(price * quantity);
        User user = (User) session.getAttribute("user");
        cart.setUserId(user.getId());
        //购物车保存成功
        try {
            if (cartService.save(cart)) {//所有购物车
                return "redirect:/cart/findAllCart";
            }
        } catch (Exception e) {//失败则重定向
            return "redirect:/productCategory/list";
        }
        return null;
    }

    /**
     * 查询 购物车信息
     * @param session
     * @return 跳转 购物车页面
     */
    @GetMapping("/findAllCart")
    public ModelAndView findAllCart(HttpSession session) {//查询该用户的所有购物车
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement1");
        User user = (User) session.getAttribute("user");
        //“我的购物车"信息
        modelAndView.addObject("cartList", cartService.findAllCartVOByUserId(user.getId()));
        return modelAndView;
    }

    /**
     * 删除 购物车某项商品
     * @param id
     * @return 跳转 购物车页面
     */
    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        cartService.removeById(id);
        //重定向
        return "redirect:/cart/findAllCart";
    }

    /**
     * 确认 购物车结算
     * @param session
     * @return 跳转 结算页面
     */
    @GetMapping("/settlement2")
    public ModelAndView settlement2(HttpSession session) {//查询该用户的所有购物车
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement2");
        User user = (User) session.getAttribute("user");
        //“我的购物车"信息
        modelAndView.addObject("cartList", cartService.findAllCartVOByUserId(user.getId()));
        //该用户的地址信息同样带过去
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        modelAndView.addObject("addressList",userAddressService.list(wrapper));
        return modelAndView;
    }

    /**
     * 更新 购物车
     * @param id
     * @param quantity
     * @param cost
     * @return
     */
    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String updateCart(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("cost") Float cost
    ){
        Cart cart = cartService.getById(id);
        cart.setQuantity(quantity);
        cart.setCost(cost);
        if(cartService.updateById(cart)){
            return "success";
        }else{
            return "fail";
        }
    }

}


