package com.southwind.mmall002.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.entity.UserAddress;
import com.southwind.mmall002.mapper.UserAddressMapper;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    UserAddressMapper userAddressMapper;
    @Autowired
    private CartService cartService;

    /**
     * 渲染 地址管理页面
     * @param session
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        User user = (User)session.getAttribute("user");
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        modelAndView.addObject("addressList",userAddressService.list(wrapper));
        return modelAndView;
    }

    /**
     * 删除 对应地址项
     * @param id
     * @return
     */
    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
        userAddressService.removeById(id);
        return "redirect:/userAddress/list";
    }

    /**
     * 新建 用户地址
     * @param session
     * @param province
     * @param city
     * @param area
     * @param home
     * @param remark
     * @return
     */
    @GetMapping("/createNewAddress")
    public String createNewAddress(HttpSession session,String province,String city,String area,String home,String remark){
       //写入数据库
        User user = (User) session.getAttribute("user");


        //通过条件查询是否存在默认地址
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("isdefault",1);
        wrapper.eq("user_id",user.getId());
        UserAddress oldAddress = userAddressMapper.selectOne(wrapper);
        //判断是否存在默认地址
        if(oldAddress != null){//存在默认地址
            oldAddress.setIsdefault(0);//改为非默认
            userAddressMapper.updateById(oldAddress);
        }
        //写到 service 层
        UserAddress userNewAddress = new UserAddress();
        userNewAddress.setUserId(user.getId());
        userNewAddress.setIsdefault(1);
        userNewAddress.setAddress(province + city + area + home);
        userNewAddress.setRemark(remark);

        //写入数据库
        userAddressService.save(userNewAddress);
        return "redirect:/userAddress/list";
    }
}
