package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.vo.Seller.StoreFanMsgVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
public interface UserService extends IService<User> {
    //从 User 表中查询该店铺的粉丝，返回 FanMsgVo 的列表
    public List<StoreFanMsgVO> findAllUserBySubStoreID(Integer subId);
}
