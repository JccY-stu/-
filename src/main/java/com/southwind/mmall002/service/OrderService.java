package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.vo.OrderVO;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
public interface OrderService extends IService<Orders> {

    //提交新的订单
    boolean save(Orders orders, User user,String address,String remark);

    //通过 UserId 查找所有的订单信息
    List<OrderVO> findAllOrederVOByUserId(Integer id);

    //用来处理秒杀的下单方法并返回订单 id 加入 md5 签名 接口隐藏
    Integer kill(Integer id,HttpSession session,String md5);

    //接口加盐 返回 md5 字符串
    String getKillMd5(Integer id, HttpSession session);
}
