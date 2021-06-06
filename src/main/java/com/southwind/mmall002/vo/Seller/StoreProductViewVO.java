package com.southwind.mmall002.vo.Seller;

import lombok.Data;

/**
 * 店铺商品 浏览次数 VO
 *
 * @Author Chengzhi
 * @Date 2021/5/27 10:51
 * @Version 1.0
 */
@Data
public class StoreProductViewVO {

    //商品属性
    private String name;
    private Float price;
    private Integer stock;


    //从 Redis 取出的相关属性
    public Object Id;//value
    public Double views;//score

}
