package com.southwind.mmall002.vo.Seller;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 店铺商品 VO
 *
 * @Author Chengzhi
 * @Date 2021/5/23 10:10
 * @Version 1.0
 */
@Data
public class StoreProductVO {

    private Integer id;
    private String name;
    private Float price;
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
