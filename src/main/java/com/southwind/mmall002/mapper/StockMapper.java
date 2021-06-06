package com.southwind.mmall002.mapper;

import com.southwind.mmall002.entity.kill.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 小杨
 * @since 2021-05-28
 */
public interface StockMapper extends BaseMapper<Stock> {

    //根据商品 ID 扣除库存
    Integer updateSale(Stock stock);

    //通过商品 ID 获取商品信息
    Stock selectCustomerById(Integer id);
}
