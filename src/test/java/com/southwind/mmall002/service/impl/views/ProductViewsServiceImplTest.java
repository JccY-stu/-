package com.southwind.mmall002.service.impl.views;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.southwind.mmall002.Init.GetProductList;
import com.southwind.mmall002.entity.ViewsRank;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/25 21:36
 * @Version 1.0
 */
@SpringBootTest
class ProductViewsServiceImplTest {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsServiceImplTest.class);

    //自定义 排行榜 Key
    public static final String VIEWS_RANK = "viewS_rank";

    @Autowired
    ProductViewsServiceImpl productViewsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void getViewsRank() {
        //写入 Zset 数据结构 新增排行榜
        HashMap allViewsFormRedis = (HashMap) productViewsService.getStoreViewsFormRedis(1);
        LOGGER.info("获取到的商品个数为：" + String.valueOf(allViewsFormRedis.size()));
        allViewsFormRedis.forEach((key, value) -> {
            redisTemplate.opsForZSet().add(VIEWS_RANK, key, Double.parseDouble((value.toString())));
        });
        LOGGER.info("已添加至 Zset 数据结构");
        List<ViewsRank> viewsRankList = new ArrayList();
        Set<String> range = redisTemplate.opsForZSet().reverseRange(VIEWS_RANK, 0, allViewsFormRedis.size());
        System.out.println("获取到的排行列表:" + JSON.toJSONString(range));
        Set rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(VIEWS_RANK, 0, allViewsFormRedis.size());
        System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));
        System.out.println("该店铺的商品数量为：" + allViewsFormRedis.size());
        Iterator<ZSetOperations.TypedTuple<String>> iterator = rangeWithScores.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            Double score = typedTuple.getScore();
            ViewsRank viewsRank = new ViewsRank(value,score);
            viewsRankList.add(viewsRank);
            System.out.println("获取RedisZSetCommands.Tuples的区间值:" + viewsRank.Id + "---->" + viewsRank.views);
        }
        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("msg", "操作成功！");
    }
}