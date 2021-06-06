package com.southwind.mmall002.service.kill;

import com.southwind.mmall002.entity.kill.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.mmall002.vo.Kill.KillVO;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-28
 */
public interface StockService extends IService<Stock> {

    //得到 秒杀商品列表
    List<KillVO> getKillVOList();
}
