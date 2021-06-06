package com.southwind.mmall002.controller.Seller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.FanJson;
import com.southwind.mmall002.entity.OptionDetail;
import com.southwind.mmall002.service.OptionDetailService;
import com.southwind.mmall002.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/5/20 10:50
 * @Version 1.0
 */
@SpringBootTest
class AdminControllerTest {


    @Autowired
    private OptionDetailService optionDetailService;

    @Autowired
    private UserService userService;
    @Test
    void welcome() {

        OptionDetail byId = optionDetailService.getById(35);
        System.out.println(byId.getCreateTime());
    }

    @Test
    void showFansMsg() {
        // 店铺粉丝列表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sub_storeid",1);
        List fanslist = userService.list(wrapper);

        //装载成FanJson 对象
        FanJson fanJson = new FanJson();
        fanJson.setCode(0);
        fanJson.setMsg("");
        fanJson.setCount(1000);
        fanJson.setData(fanslist);

        JSON json = (JSON) JSON.toJSON(fanJson);
        System.out.println(json);
    }
}