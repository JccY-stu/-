package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.vo.CartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
public interface CartService extends IService<Cart> {
    //通过 UserId 获得购物车列表
    public List<CartVO> findAllCartVOByUserId(Integer id);
}
