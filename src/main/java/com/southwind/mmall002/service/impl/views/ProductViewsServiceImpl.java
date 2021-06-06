package com.southwind.mmall002.service.impl.views;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.southwind.mmall002.Init.GetProductList;
import com.southwind.mmall002.controller.ProductController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.ViewsRank;
import com.southwind.mmall002.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author Chengzhi
 * @Date 2021/5/24 19:06
 * @Version 1.0
 */
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class ProductViewsServiceImpl {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    //自定义 所有商品排行榜 Key
    public static final String VIEW_RANK = "view_rank";

    //自定义 店铺商品排行榜 Key
    public static final String VIEWS_RANK = "viewS_rank";

    /**
     * 初始化 所有商品的浏览次数 写入 Redis
     */
    public void writeViewsToRedis() {
        //为所有的商品添加 redis 键值对，存储其浏览量;
        //从全局变量中取出
        List<Product> allProductAndViewVO = GetProductList.allProductAndViewVO;
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 设置 redis 键值对
            redisTemplate.opsForValue().set(viewProductVO.getId(), viewProductVO.getViews());
            LOGGER.info(viewProductVO.getId()+"该商品存入 Redis 的浏览次数为：" + redisTemplate.opsForValue().get(viewProductVO.getId()));
        }
    }

    /**
     * 获取 所有商品的浏览次数 从 Redis 里
     * @return 所有商品ID --- 浏览次数  Map
     */
    public Map getAllViewsFormRedis() {
        List<Product> allProductAndViewVO = GetProductList.allProductAndViewVO;
        //键---商品 ID
        //值---商品 浏览次数
        Map<Integer,Integer> viewsList = new HashMap();
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 获取 redis 键值对
            Integer views = (Integer) redisTemplate.opsForValue().get(viewProductVO.getId());
            //重新装载到 列表里 ID --- 浏览次数
            viewsList.put(viewProductVO.getId(),views);
        }
        return viewsList;
    }

    /**
     * 获取 该店铺商品的浏览次数 从 Redis 里
     * @param ID 店铺 ID
     * @return 店铺商品ID --- 浏览次数  Map
     */
    public Map getStoreViewsFormRedis(Integer ID) {
        // 拿到店铺的所有商品
        List<Product> allProductAndViewVO = productService.getAllByStoreId(ID);
        //键---商品 ID
        //值---商品 浏览次数
        Map<Integer,Integer> viewsList = new HashMap();
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 获取 redis 键值对
            Integer views = (Integer) redisTemplate.opsForValue().get(viewProductVO.getId());
            //重新装载到 列表里 ID --- 浏览次数
            viewsList.put(viewProductVO.getId(),views);
        }
        return viewsList;
    }

    /**
     * 强制 Redis 里的浏览次数刷新到 数据库
     * 例如 商家统计商品浏览量排行榜时
     * 例如 定时刷入任务
     */
    @Async
    @Scheduled(fixedDelay = 300000 ) //三十秒写回数据库
    public void writeViewsToMQL() {
        List<Product> allProductAndViewVO = GetProductList.allProductAndViewVO;
        for (Product viewProductVO : allProductAndViewVO) {
            LOGGER.info(String.valueOf(viewProductVO.getViews()));
            // 获取 redis 键值对
            Integer views = (Integer) redisTemplate.opsForValue().get(viewProductVO.getId());
            //重新装载到 列表里
            viewProductVO.setViews(views);
        }
        //更新 数据库
        productService.updateBatchById(allProductAndViewVO);
    }

    /**
     * 通过 ID 改变商品浏览量 写回 Redis
     */
    public Integer writeViewsToRedisByID(Integer Id){
        Integer view = (Integer) redisTemplate.opsForValue().get(Id);
        LOGGER.info("该商品的浏览次数为："+view);
        redisTemplate.opsForValue().set(Id,++view);
        LOGGER.info("该商品的浏览次数为："+view);
        return view;
    }

    /**
     * 获取 项目商品浏览量 排行 来源 Redis
     */
    public void getAllViewsRank(){
        //写入 Zset 数据结构 新增排行榜
        long start = System.currentTimeMillis();
        /**
         * 从 Redis 里面得到所有的商品浏览次数
         * 循环添加至 Zset 数据结构，即排行榜结构
         * 如果要变为本店店铺的商品 排行榜 则 重载，修改 allViewFormRedis = getMystoreViewsFormRedis（）;
         */
        HashMap allViewsFormRedis = (HashMap) getAllViewsFormRedis();
        //循环添加至 Zset 数据结构，即排行榜结构
        allViewsFormRedis.forEach((key, value) -> {
            redisTemplate.opsForZSet().add(VIEW_RANK, key, Double.parseDouble((value.toString())));
        });
        System.out.println("添加时间:" + (System.currentTimeMillis() - start));
        System.out.println("批量新增时间:" + (System.currentTimeMillis() - start));
        System.out.println("受影响行数：" + redisTemplate.opsForZSet().size(VIEW_RANK));

        List<ViewsRank> viewsRankList = new ArrayList();

        Set<String> range = redisTemplate.opsForZSet().reverseRange(VIEW_RANK, 0, GetProductList.allProductAndViewVO.size());
        System.out.println("获取到的排行列表:" + JSON.toJSONString(range));
        Set rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(VIEW_RANK, 0, GetProductList.allProductAndViewVO.size());
        System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));
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
        //
        System.out.println(viewsRankList.getClass());
        System.out.println(viewsRankList.get(0).getId());
        result.put("code", "0");
        result.put("msg", "操作成功！");
    }

    /**
     * 获取 店铺商品浏览量 排行 来源 Redis
     * @return List<ViewsRank> 列表
     */
    public List getSellerStoreViewsRank(Integer storeID){
        //写入 Zset 数据结构 新增排行榜
        HashMap allViewsFormRedis = (HashMap) getStoreViewsFormRedis(storeID);
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
        //迭代器赋值 将排行榜键值对 放入 viewRankList
        Iterator<ZSetOperations.TypedTuple<String>> iterator = rangeWithScores.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            Object value = typedTuple.getValue();
            Double score = typedTuple.getScore();
            ViewsRank viewsRank = new ViewsRank(value,score);
            viewsRankList.add(viewsRank);
            System.out.println("获取RedisZSetCommands.Tuples的区间值:" + viewsRank.Id + "---->" + viewsRank.views);
        }
        return viewsRankList;
    }

}
