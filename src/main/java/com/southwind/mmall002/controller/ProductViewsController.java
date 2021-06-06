package com.southwind.mmall002.controller;


import com.alibaba.fastjson.JSONObject;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.entity.ViewsRank;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.impl.views.ProductViewsServiceImpl;
import com.southwind.mmall002.vo.Seller.StoreProductVO;
import com.southwind.mmall002.vo.Seller.StoreProductViewVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author Chengzhi
 * @Date 2021/5/24 18:41
 * @Version 1.0
 */
@Controller
@RequestMapping("/productView")
public class ProductViewsController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsController.class);

    @Autowired
    private ProductViewsServiceImpl productViewsService;

    @Autowired
    private ProductService productService;

    /**
     * 初始化 所有商品的浏览次数 写入 Redis
     * productViewsService 实现
     * id 为键  views 为值
     */
    public void ToRedis() {
        productViewsService.writeViewsToRedis();
    }

    /**
     * 强制 Redis 里的浏览次数刷新到 数据库
     * 例如 商家统计商品浏览量排行榜时
     * 例如 定时刷入任务
     */
    public void ToMQL(){
        productViewsService.writeViewsToMQL();
    }

    @GetMapping("/storeProductViews")
    @ResponseBody
    public JSONObject getSellerStoreViewsRank(HttpSession session){
        User seller = (User) session.getAttribute("user");
        //获得 List<ViewsRank> 的列表
        List<ViewsRank> sellerStoreViewsRank = productViewsService.getSellerStoreViewsRank(seller.getStoreId());
        LOGGER.info("该店铺的商品个数为：" + String.valueOf(sellerStoreViewsRank.size()));
        //获得 该店铺的商品列表
        ArrayList<StoreProductVO> allProductByStoreId = (ArrayList<StoreProductVO>) productService.findAllProductByStoreId(seller.getStoreId());
        LOGGER.info("该店铺的商品个数为：" + String.valueOf(allProductByStoreId.size()));
        //新建 组装列表
        List<StoreProductViewVO> storeProductViewVOS = new ArrayList<>();
        //开始组装
        for(int i = 0 ; i < allProductByStoreId.size() ; i++){
            StoreProductViewVO storeProductViewVO = new StoreProductViewVO();

            storeProductViewVO.setViews(sellerStoreViewsRank.get(i).views);
            storeProductViewVO.setId(sellerStoreViewsRank.get(i).Id);
            storeProductViewVO.setPrice(allProductByStoreId.get(i).getPrice());
            storeProductViewVO.setName(allProductByStoreId.get(i).getName());
            storeProductViewVO.setStock(allProductByStoreId.get(i).getStock());

            storeProductViewVOS.add(storeProductViewVO);
        }
        LOGGER.info("本店商品存入 Redis 的 商品---浏览量 个数为：" + storeProductViewVOS.size() + "个元素");
        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("msg", "操作成功！");
        result.put("data",storeProductViewVOS);
        return result;
    }
}
