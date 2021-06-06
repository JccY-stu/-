package com.southwind.mmall002.service.impl.kill;

import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.kill.Stock;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.kill.StockService;
import com.southwind.mmall002.vo.Kill.KillVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Chengzhi
 * @Date 2021/6/6 11:05
 * @Version 1.0
 */
@SpringBootTest
class StockServiceImplTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Test
    void killVOList() {
        //从 Stock 表秒杀商品 列表信息
        List<Stock> killStockList = stockService.list();
        ArrayList<Integer> idList = new ArrayList<>();
        for (Stock stock : killStockList) {
            Integer id = stock.getId();
            idList.add(id);
        }
        //从 Product 表 查出对应的信息
        List<Product> killProductList = productService.listByIds(idList);
        //开始组装
        List<KillVO> killVOList = new ArrayList<>();
        for(int i = 0 ;i < killProductList.size(); i++){
            KillVO killVO = new KillVO();
            BeanUtils.copyProperties(killProductList.get(i),killVO);
            BeanUtils.copyProperties(killStockList.get(i),killVO);
            killVOList.add(killVO);
        }
        for (KillVO killVO : killVOList) {
            System.out.println(killVO.getStoreName());
            System.out.println(killVO.getPrice());
            System.out.println(killVO.getCount());
        }
    }
}