package com.southwind.mmall002.vo;

import lombok.Data;

/**
 * 订单详情 VO
 */
@Data
public class OrderDetailVO {

    //得到订单的 id
    private Integer id;
    private String name;
    private String fileName;
    private Integer quantity;
    private Float price;
    private Float cost;
}
