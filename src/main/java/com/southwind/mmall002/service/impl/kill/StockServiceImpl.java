package com.southwind.mmall002.service.impl.kill;


import com.southwind.mmall002.controller.Kill.StockController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.kill.Stock;
import com.southwind.mmall002.mapper.StockMapper;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.kill.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.mmall002.vo.Kill.KillVO;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *  组装 Product 和 Stock 实体 为 allKillProduct 页面做准备
 * @author 小杨
 * @since 2021-05-28
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Override
    public List<KillVO> getKillVOList() {
        //从 Stock 表秒杀商品 列表信息
        List<Stock> killStockList = stockService.list();
        LOGGER.info(String.valueOf(killStockList));
        ArrayList<Integer> idList = new ArrayList<>();
        for (Stock stock : killStockList) {
            Integer id = stock.getId();
            idList.add(id);
        }
        //从 Product 表 查出对应的信息
        List<Product> killProductList = productService.listByIds(idList);
        LOGGER.info("获取到的秒杀商品 数量为：" + killProductList.size());
        //开始组装
        List<KillVO> killVOList = new ArrayList<>();
        for(int i = 0 ;i < killProductList.size(); i++){
            KillVO killVO = new KillVO();
            BeanUtils.copyProperties(killProductList.get(i),killVO);
            BeanUtils.copyProperties(killStockList.get(i),killVO);
            killVOList.add(killVO);
        }
        for (KillVO killVO : killVOList) {
            System.out.println(killVO.getName());
        }
        return killVOList;
    }


}
