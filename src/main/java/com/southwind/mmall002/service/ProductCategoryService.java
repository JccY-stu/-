package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.vo.ProductCategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    //获得所有的商品分类信息
    public List<ProductCategoryVO> getAllProductCategoryVO();
}
