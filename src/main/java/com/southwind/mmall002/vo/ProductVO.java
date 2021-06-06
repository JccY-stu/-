package com.southwind.mmall002.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 商品VO
 *
 * @Author Chengzhi
 * @Date 2021/5/8 19:27
 * @Version 1.0
 */

@Data
@AllArgsConstructor
public class ProductVO {
    private Integer id;
    private String name;
    private Float price;
    private String fileName;
    private Integer storeId;
    private String storeName;
}
