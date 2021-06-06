package com.southwind.mmall002.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品分类 VO
 *
 * @Author Chengzhi
 * @Date 2021/5/8 9:24
 * @Version 1.0
 */

@Data
public class ProductCategoryVO {
    private Integer id;
    private String name;
    private List<ProductCategoryVO> children;
    private String bannerImg;
    private String topImg;
    private List<ProductVO> productVOList;
    private Integer views;

    public ProductCategoryVO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
