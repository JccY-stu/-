package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.southwind.mmall002.controller.ProductController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.mapper.ProductMapper;
import com.southwind.mmall002.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.mmall002.vo.Seller.StoreFanMsgVO;
import com.southwind.mmall002.vo.Seller.StoreProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    /**
     * 通过 分类ID 获取对应分类 商品集合
     * @param type
     * @param categoryId
     * @return
     */
    @Override
    public List<Product> findByCategoryId(String type,Integer categoryId) {
        Map<String,Object> map = new HashMap<>();
        map.put("categorylevel"+type+"_id",categoryId);
        return productMapper.selectByMap(map);
    }

    /**
     * 查询 通过 名字 进行模糊查询，返回查询列表
     * @param name
     * @return 模糊查询列表
     */
    @Override
    public List<Product> findByName(String name) {
        Product product = new Product();
        product.setName(name);

        //模糊查询 条件封装（前后都有 %name%）
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(product.getName()), "name", product.getName());
        List<Product> productList = productMapper.selectList(queryWrapper);
        return productList;

    }

    /**
     * 查询 通过店铺 ID 获取所有的商品信息
     * 转化 Product --> StoreProductVO
     * @param storeId
     * @return
     */
    @Override
    public List<StoreProductVO> findAllProductByStoreId(Integer storeId) {
        List<StoreProductVO> storeProductVOList = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        //店铺ID 应当通过传入的方式
        wrapper.eq("store_id",storeId);
        List<Product> productList = productMapper.selectList(wrapper);
        LOGGER.info(storeId + "号店铺商品数量为：" + String.valueOf(productList.size()));
        //赋给 fanMsgVo 类型对象
        for (Product product : productList) {
            wrapper.clear();
            StoreProductVO storeProductVO = new StoreProductVO();
            //通过BeanUtils工具类给实体赋值
            BeanUtils.copyProperties(product, storeProductVO);
            LOGGER.info("storeProductVO.id = " + storeProductVO.getId());
            storeProductVOList.add(storeProductVO);
        }
        return storeProductVOList;
    }

    /**
     * 通过 店铺 ID 获取店铺 所有的商品信息
     * @param storeId
     * @return
     */
    @Override
    public List<Product> getAllByStoreId(Integer storeId) {
        QueryWrapper wrapper = new QueryWrapper();
        //店铺ID 应当通过传入的方式
        wrapper.eq("store_id",storeId);
        List<Product> productList = productMapper.selectList(wrapper);
        LOGGER.info(storeId + "号店铺商品数量为：" + String.valueOf(productList.size()));
        return productList;
    }
}