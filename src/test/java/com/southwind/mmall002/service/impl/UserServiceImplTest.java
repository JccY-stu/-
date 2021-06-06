package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.mapper.UserMapper;
import com.southwind.mmall002.service.OrderService;
import com.southwind.mmall002.vo.Seller.StoreFanMsgVO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Chengzhi
 * @Date 2021/5/21 16:29
 * @Version 1.0
 */
@SpringBootTest
class UserServiceImplTest {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderService orderService;

    @Test
    void  findAllUserBySubStoreID() {
        List<StoreFanMsgVO> storeFanMsgVOList = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        //店铺ID 应当通过传入的方式
        wrapper.eq("sub_storeid",1);
        List<User> userList = userMapper.selectList(wrapper);
        LOGGER.info("关注1号店铺的用户数量为：" + String.valueOf(userList.size()));
        //赋给 fanMsgVo 类型对象
        for (User user : userList) {
            wrapper.clear();
            StoreFanMsgVO storeFanMsgVO = new StoreFanMsgVO();
            BeanUtils.copyProperties(user, storeFanMsgVO);
            LOGGER.info("fanMsgVo.id = " + storeFanMsgVO.getId());
            wrapper.eq("user_id", storeFanMsgVO.getId());
            wrapper.eq("store_id",1);
            int wealth = orderService.list(wrapper).size();//得到订单数量
            storeFanMsgVO.setOrderQuantity(wealth);
            LOGGER.info(storeFanMsgVO.getUserName()+"在该店铺的订单数量为：" +wealth);
            storeFanMsgVOList.add(storeFanMsgVO);
        }
    }
}