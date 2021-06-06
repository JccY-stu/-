package com.southwind.mmall002.vo;

import lombok.Data;

/**
 * 购物车 VO
 *
 * @Author Chengzhi
 * @Date 2021/5/9 17:33
 * @Version 1.0
 */
@Data
public class CartVO {
    //购物车
    /**
     * 购物车信息 ID
     */
    private Integer id;
    /**
     * 购物车商品 数量
     */
    private Integer quantity;
    /**
     * 购物车某件商品 花费
     */
    private Float cost;
    /**
     * 购物车某件商品 ID
     */
    private Integer productId;
    /**
     * 购物车某件商品 名称
     */
    private String name;
    /**
     * 购物车某件商品 单价
     */
    private Float price;
    /**
     * 购物车某件商品 图片
     */
    private String fileName;
    /**
     * 购物车某件商品 库存
     */
    private Integer stock;
    /**
     * 购物车某件商品 店铺 ID
     */
    private Integer storeId;
    /**
     * 购物车某件商品 店铺 名称
     */
    private String storeName;
}
