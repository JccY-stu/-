package com.southwind.mmall002.service;

import com.southwind.mmall002.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.vo.Seller.StoreProductVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
public interface ProductService extends IService<Product> {

    //通过 商品分类 ID 查找商品
    public List<Product> findByCategoryId(String type,Integer categoryId);

    //通过 商品名称 查找商品
    public List<Product> findByName(String name);

    //通过店铺 ID 查询所有的商品信息
    public List<StoreProductVO> findAllProductByStoreId(Integer storeId);

    //通过店铺 ID 查询所有的商品信息（与上方法得到的数据类型不同）
    public List<Product> getAllByStoreId(Integer storeId);
}
