package com.southwind.mmall002.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.controller.file.UploadController;
import com.southwind.mmall002.entity.OrderDetail;
import com.southwind.mmall002.entity.Orders;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.enums.ResultEnum;
import com.southwind.mmall002.exception.MallException;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.OrderDetailService;
import com.southwind.mmall002.service.OrderService;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.vo.CartVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**

 *
 * @author 小杨
 * @since 2021-05-09
 */
@Controller
@RequestMapping("/orders")
public class OrderController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    /**
     * 确认 订单信息
     * 减少 库存信息
     * 跳转至 成功提交订单页面
     * @param orders
     * @param session
     * @param address
     * @param remark
     * @return
     */
    @PostMapping("/settlement3")
    public ModelAndView settlement3(Orders orders, HttpSession session,String address,String remark){

        User user = (User) session.getAttribute("user");
        List<CartVO> allCartVOByUserId = cartService.findAllCartVOByUserId(user.getId());

        //将新地址和备注存进地址
        //保存订单信息到数据库
        orderService.save(orders,user,address,remark);
        ModelAndView modelAndView = new ModelAndView("settlement3");

        //“我的购物车"信息
        modelAndView.addObject("cartList", allCartVOByUserId);

        //减少 订单对应的商品的库存
        try {
            for (CartVO cartVO : allCartVOByUserId) {
                LOGGER.info("该订单所需要的商品数量为：" + String.valueOf(cartVO.getQuantity()));
                Product product = productService.getById(cartVO.getProductId());
                product.setStock((product.getStock()) - cartVO.getQuantity());
                productService.updateById(product);
            }
            LOGGER.info("修改库存成功！~");
        }catch (Exception e){
            LOGGER.error("【添加购物车】库存不足！");
            throw new MallException(ResultEnum.STOCK_ERROR);
        }

        //订单信息
        System.out.println(orders.getUserAddress());
        modelAndView.addObject("orders",orders);

        return modelAndView;
    }

    /**
     * 查看 我的订单
     * @param session
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderList");
        User user = (User) session.getAttribute("user");

        // 订单列表
        modelAndView.addObject("list",orderService.findAllOrederVOByUserId(user.getId()));

        // 购物车列表
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        return modelAndView;
    }

    /**
     * 删除 我的订单
     * 只删除订单详情 即 order_Detail 表中的数据
     * 不删除 orders 中的数据
     *
     * 更新 也要删除 orders 中的数据
     */
    @GetMapping("/deleteById/{id}")
    public String delete(@PathVariable("id") Integer id,HttpSession session){

        //打印日志
        LOGGER.info("获取后端传过来的订单 ID 为：" + id);

        //获得满足 id 字段 == id 的 order_id 字段
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("order_id").eq("id",id);
        OrderDetail one = orderDetailService.getOne(wrapper);

        //得到 order_id 字段
        Integer orderId = one.getOrderId();

        //删除该订单
        boolean b = orderDetailService.removeById(id);
        LOGGER.info("删除 order_Detail 表中数据 " + b);

        //通过 orderId 删除 order 表中的数据
        boolean b1 = orderService.removeById(orderId);
        LOGGER.info("删除 order 表中数据 " + b1);

        //重定向回订单列表页面
        return "redirect:/orders/list";
    }
}

