package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.enums.ResultEnum;
import com.southwind.mmall002.entity.Cart;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.exception.MallException;
import com.southwind.mmall002.mapper.CartMapper;
import com.southwind.mmall002.mapper.ProductMapper;
import com.southwind.mmall002.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.mmall002.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 扣除商品 库存
     * @param entity
     * @return
     */
    @Override
    public boolean save(Cart entity) {
        //扣库存
        Product product = productMapper.selectById(entity.getProductId());
        Integer stock = product.getStock() - entity.getQuantity();
        if(stock < 0){
            log.error("【添加购物车】库存不足！stock={}",stock);
            throw new MallException(ResultEnum.STOCK_ERROR);
        }
//        product.setStock(stock);//不扣库存，只做校验 此时是加入购物车就更新库存，肯定是不对的
        productMapper.updateById(product);
        if(cartMapper.insert(entity) == 1){
            return true;
        }
        return false;
    }

    /**
     * 获取 用户购物车信息
     * @param id
     * @return
     */
    @Override
    public List<CartVO> findAllCartVOByUserId(Integer id) {
        List<CartVO> cartVOList = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            CartVO cartVO = new CartVO();
            Product product = productMapper.selectById(cart.getProductId());
            BeanUtils.copyProperties(product,cartVO);
            BeanUtils.copyProperties(cart,cartVO);
            cartVOList.add(cartVO);
        }
        return cartVOList;
    }
}
