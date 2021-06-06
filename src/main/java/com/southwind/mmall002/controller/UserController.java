package com.southwind.mmall002.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.controller.Verifi.RegVerification;
import com.southwind.mmall002.controller.Verifi.RegsentController;
import com.southwind.mmall002.controller.file.UploadController;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.entity.UserAddress;
import com.southwind.mmall002.mapper.UserAddressMapper;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

//加密算法包
import org.springframework.util.DigestUtils;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Controller
@RequestMapping("/user")
public class UserController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /*对 user表 的操作*/
    @Autowired
    private UserService userService;

    /*对 cart表 的操作*/
    @Autowired
    private CartService cartService;

    /*引入 Redis*/
    @Autowired
    RedisTemplate redisTemplate;

    /*发送 验证码*/
    @Autowired
    RegsentController regsentController;

    /*验证 验证码*/
    @Autowired
    RegVerification regVerification;

    /*对 user_address 表的操作*/
    @Autowired
    private UserAddressMapper userAddressMapper;


    /*传入前端返回的用户名（因格式问题，否则查询 Redis 为 Null）*/
    static String loginNameOne;

    /**
     * 注册 用户注册成功返回登录页面
     * @param user
     * @param model
     * @return
     */
    @PostMapping("/register")
    public String register(User user, Model model,String vCodeFrom) {
        Boolean result = false;
        //转成字符串
        //如果该用户名不存在，则注册成功写入数据库
        try {
            System.out.println("用户输入的验证码为:" + vCodeFrom);
            //核对验证码
            Boolean verfication = regVerification.Verfication(loginNameOne, vCodeFrom, redisTemplate);
            System.out.println("用户密码为：" + user.getPassword());
            String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(md5Password);
            if(verfication == true)
            //写入数据库
            result = userService.save(user);
            else {
                model.addAttribute("error", user.getLoginName() + "验证码错误！");
                return "register";
            }
        } catch (Exception e) {//注册失败则返回注册页面
            model.addAttribute("error", user.getLoginName() + "已经存在啦！！！请重新输入~~~");
            return "register";
        }
        //注册成功返回登录页面
        if (result) {
            model.addAttribute("success","您已经成功注册！");
            //把用户名和密码带过去,自动填入用户名和密码
            model.addAttribute("loginName",user.getLoginName());
            model.addAttribute("password",user.getPassword());
            return "login";
        }
        return "";
    }

    /**
     * 验证 发送注册验证码
     * @param loginName 验证键
     * @return
     */
    @PostMapping("/validate")
    public String  validate(@RequestBody String loginName,Model model) {
        //调用发送验证码 返回该用户验证码的 Key
        //判断短信是否发送成功
        if(redisTemplate.opsForValue().get(loginName) != null){//如果验证码已经发送，则必须等五分钟后才可以再次发送
            System.out.println("验证码已发送，请五分钟后重试！");
            model.addAttribute("error", "验证码已发送，请五分钟后重试！");
            return "register";
        }
        String vCode = regsentController.sendmessage(loginName, redisTemplate);
        loginNameOne = loginName;
        System.out.println("发送的验证码为：" + vCode);
        model.addAttribute("vCode",vCode);
        return "register";
    }

    /**
     * 登录 用户登录
     * @param loginName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String login(String loginName, String password, HttpSession session) {
        QueryWrapper wrapper = new QueryWrapper();
        //wrapper条件必须写数据库字段名字，不能写实体类名字
        wrapper.eq("login_name", loginName);
        //采用同样的加密方式 加密后 比对
        wrapper.eq("password",DigestUtils.md5DigestAsHex(password.getBytes()));
        LOGGER.info("加密前:"+ password);
        LOGGER.info("加密后:"+ DigestUtils.md5DigestAsHex(password.getBytes()));
        User user = userService.getOne(wrapper);
        if (user == null) {//登录失败
            return "login";
        } else {
            session.setAttribute("user", user);
            //必须传给 页面 数据，通过代码复用的方式
            return "redirect:/productCategory/list";
        }
    }

    /**
     * 退出 用户退出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    /**
     * 渲染 用户管理界面
     * 包括 个人信息
     *     订单中心
     *     会员中心
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(HttpSession session){
        //user 的所有信息
        User user = (User) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userInfo");
        //购物车信息
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        //收货地址信息
        //该用户的地址信息同样带过去
        QueryWrapper wrapper = new QueryWrapper();
        //通过条件查询是否存在默认地址
        wrapper.eq("isdefault",1);
        wrapper.eq("user_id",user.getId());
        UserAddress oldAddress = userAddressMapper.selectOne(wrapper);
        //如果存在默认地址,则将默认地址装进 model
        if(oldAddress != null){}
        else {//不存在默认地址,填充其 oldDefault 地址信息 以便输出
            UserAddress newAddress = new UserAddress();
            newAddress.setUserId(user.getId());
            newAddress.setIsdefault(1);
            newAddress.setAddress("您的收货地址待填写...");
            newAddress.setRemark("您的收货备注待填写...");
            userAddressMapper.insert(newAddress);
        }
        // 返回 model
        modelAndView.addObject("address",oldAddress);
        return modelAndView;
    }

    /**
     * 修改 用户个人信息并刷新页面
     * @param session
     * @param r_email
     * @param r_mobile
     * @param province
     * @param city
     * @param area
     * @param remark
     * @return
     */
    @GetMapping("/reviseUserInfo")
    public String reviseUserInfo(HttpSession session,String r_email,String r_mobile,String province,String city,String area,String home,String remark) {
        //新建 User 对象 装载数据
        User user = (User) session.getAttribute("user");
        user.setEmail(r_email);
        user.setMobile(r_mobile);
        //更新 User 表
        userService.updateById(user);
        //新建 UserAddress 对象装载数据
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(user.getId());
        userAddress.setAddress(province+city+area+home);
        userAddress.setRemark(remark);

        QueryWrapper wrapper = new QueryWrapper();
        //通过条件查询是否存在默认地址
        wrapper.eq("isdefault",1);
        wrapper.eq("user_id",user.getId());
        UserAddress oldAddress = userAddressMapper.selectOne(wrapper);
        //判断是否存在默认地址
        if(oldAddress != null){//存在默认地址
            oldAddress.setIsdefault(0);//改为非默认
            userAddressMapper.updateById(oldAddress);
        }
        userAddress.setIsdefault(1);
        //将地址写入数据库
        userAddressMapper.insert(userAddress);
//        //如果不带则无法渲染 userInfo 页面，因为它包含这两部分
//        ModelAndView modelAndView = new ModelAndView("userInfo");
//        //我的购物车信息
//        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
//        //地址信息
//        modelAndView.addObject("address",userAddress);
        //刷新页面
//        return modelAndView;
        //增删改使用重定向，则不用再带Model
        return "redirect:/user/userInfo";
    }


}



