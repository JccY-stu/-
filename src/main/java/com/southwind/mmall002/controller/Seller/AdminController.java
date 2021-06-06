package com.southwind.mmall002.controller.Seller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.southwind.mmall002.entity.Stores;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.*;

import com.southwind.mmall002.service.impl.Email.EmailSendService;
import com.southwind.mmall002.vo.Seller.StoreFanMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Author Chengzhi
 * @Date 2021/5/19 9:30
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private StoresService storesService;

    @Autowired
    private OptionDetailService optionDetailService;

    @Autowired
    private EmailSendService emailSendService;


    /**
     * 跳转 后台管理
     * @return
     */
    @GetMapping("/adminIndex")
    public String Index(){
        return "indexA";
    }


    /**
     * 跳转 用户信息（展示和修改）
     * @param session
     * @return
     */
    @GetMapping("/adminMsg")
    public String Msg(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(user.getRole());
        return "/page/user-setting";
    }

    /**
     * 修改 用户信息(Restful风格)
     * @param session
     * @param username
     * @param email
     * @param phone
     * @param remark
     * @return
     */
    @PostMapping("/adminMsg")
    public String reviseUserInfo(HttpSession session,String username,String email,String phone,String remark) {
        //新建 User 对象 装载数据
        User user = (User) session.getAttribute("user");
        user.setEmail(email);
        user.setMobile(phone);
        user.setUserName(username);
        user.setRemark(remark);
        //更新 User 表
        userService.updateById(user);
        return "redirect:/admin/adminMsg";
    }
    /**
     * 跳转 用户密码界面
     * @return
     */
    @GetMapping("/adminPwd")
    public String AdminPwd(){
        return "/page/user-password";
    }

    /**
     * 修改 用户密码
     * @param new_password
     * @return
     */
    @PostMapping("/adminPwd")
    public String reviseAdminPwd(String new_password,HttpSession session ){
        //新建 User 对象 装载数据
        User user = (User) session.getAttribute("user");
        //加密后放入 User 对象
        user.setPassword(DigestUtils.md5DigestAsHex(new_password.getBytes()));
        //更新 User 表
        userService.updateById(user);
        return "redirect:/admin/adminPwd";
    }
    /**
     * 主页面
     * @return
     */
    @GetMapping("/welcome")
    public ModelAndView welcome(HttpSession session){
        ModelAndView modelAndView = new ModelAndView("/page/welcome-1");

        // 店铺粉丝数量
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sub_id",1);
        modelAndView.addObject("fanQuantity",userService.list(wrapper).size());
        System.out.println(userService.list(wrapper).size());

        // 店铺商品数量
        modelAndView.addObject("productQuantity",storesService.getById(1));
        wrapper.clear();
        wrapper.eq("store_id",1);

        // 订单数量
        Stores stores = storesService.getOne(wrapper);
        modelAndView.addObject("orderQuantity",stores);

        // 日志信息
        User user = (User) session.getAttribute("user");
        wrapper.clear();
        wrapper.eq("id",user.getId());
        modelAndView.addObject("optionDetail",optionDetailService.list(wrapper));

        //商品最高浏览数

        return modelAndView;
    }

    /**
     * 获取 粉丝信息
     */
    @GetMapping("/fansMsg")
    @ResponseBody
    public JSONObject showFansMsg(HttpSession session){
        User user = (User) session.getAttribute("user");
        JSONObject result = new JSONObject();
        //获取 FanMsgVo 列表
        List<StoreFanMsgVO> fanslist = userService.findAllUserBySubStoreID(1);

        result.put("code", "0");
        result.put("msg", "操作成功！");
        result.put("count",fanslist.size());
        //此处再加入一个订单数量
        //则查询每个用户在该店的订单数量，写入 orderQuantity 然后 传给前端 涉及到联表查询，先放下不做

        //更新：已经将 orderQuantity 通过 findAllUserBySubStoreID() 写入 StoreFanMsgVO
        result.put("data", fanslist);
        return result;
    }

    /**
     * 异步 发送邮件
     */
    @PostMapping("/Email")
    @ResponseBody
    public void sendEmail(@RequestBody List<User> userList,HttpSession session){

        // Atomic 类保证原子性 异步发送邮件
        AtomicInteger userIndex = new AtomicInteger(0);

        while(userIndex.get() < userList.size()) {
            LOGGER.info("" + userIndex.get());
            emailSendService.executeAsyncTask1(userList.get(userIndex.getAndIncrement()),session);
            LOGGER.info("" + userIndex.get());
            emailSendService.executeAsyncTask2(userList.get(userIndex.getAndIncrement()),session);
            LOGGER.info("" + userIndex.get());
        }
    }

    /**
     * 跳转 商品上架
     * @return
     */
    @GetMapping("/newProduct")
    public String toNewProduct(){
        return "/page/form";
    }

    /**
     * 跳转 店铺商品
     * @return
     */
    @GetMapping("/ProductsList")
    public String showProductsList(){
        return "/page/myProduct";
    }

    /**
     * 跳转 上架秒杀商品
     * @return
     */
    @GetMapping("/killProduct")
    public String showMyKillProduct(){
        return "/page/killProduct";
    }

}
