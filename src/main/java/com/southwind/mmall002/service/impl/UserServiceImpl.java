package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.mapper.UserMapper;
import com.southwind.mmall002.service.OrderService;
import com.southwind.mmall002.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.mmall002.vo.Seller.StoreFanMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderService orderService;

    /**
     * 获得 关注该店铺 粉丝信息列表
     * 填充 FanMsgVo 在该店铺的订单数量
     * @param sub_storeId
     * @return 粉丝列表所需要的实体类
     */
    @Override
    public List<StoreFanMsgVO> findAllUserBySubStoreID(Integer sub_storeId){
            List<StoreFanMsgVO> storeFanMsgVOList = new ArrayList<>();
            QueryWrapper wrapper = new QueryWrapper();
            //店铺ID 应当通过传入的方式
            wrapper.eq("sub_id",1);
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
            return storeFanMsgVOList;
    }
}
