package com.southwind.mmall002.vo.Kill;

import lombok.Data;

/**
 *
 * 秒杀商品 VO
 * 由 Product 和 Stock 实体类组装
 *
 * @Author Chengzhi
 * @Date 2021/6/6 10:52
 * @Version 1.0
 */
@Data
public class KillVO {

    private Integer count;
    private Integer saled;
    private Integer id;
    private String name;
    private Integer killtime;
    private Integer storeId;
    private Double price;

    //以下字段从 Product 表获取
    private String fileName;
    private String storeName;
}
