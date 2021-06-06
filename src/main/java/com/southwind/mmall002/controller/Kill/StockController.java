package com.southwind.mmall002.controller.Kill;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.RateLimiter;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.entity.kill.Stock;
import com.southwind.mmall002.exception.BizException;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.OrderService;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.kill.StockService;
import com.southwind.mmall002.vo.Kill.KillVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小杨
 * @since 2021-05-28
 */
@Controller
@RequestMapping("/stock")
@ResponseBody
public class StockController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    //创建令牌桶实例
    private RateLimiter rateLimiter = RateLimiter.create(20);//每秒令牌桶创建十个请求

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    //接口加盐方法
    public String getKillMD5(Integer id,HttpSession session){
        String md5;
        try{
            md5 = orderService.getKillMd5(id,session);
        }catch (Exception e){
            e.printStackTrace();
            return "获取秒杀接口加盐的 MD5 失败：" +e.getMessage();
        }
        return md5;
    }

    //开发一个秒杀方法 乐观锁防止超卖，令牌桶限流
    @GetMapping("/killtoken/{productId}")
    public String killtoken(@PathVariable("productId")Integer productId,
                            HttpSession session){
        LOGGER.info("秒杀商品的 ID = " + productId);
        //首先进行接口加盐
        String MD5 = getKillMD5(productId, session);
        String md5 = MD5;
        //加入令牌桶的限流措施
        //注意：限流之后商品不能百分百的卖掉，有些请求被抛弃，保留一小部分的商品
        if(!rateLimiter.tryAcquire(3,TimeUnit.SECONDS)){
            LOGGER.info( "抛弃的请求：抢购失败，当前秒杀活动过于火爆,请重试！");
        }
        try {//根据秒杀商品的 ID 调用秒杀业务
            int orderId = orderService.kill(productId,session,md5);
            return "秒杀成功！订单ID为：" + orderId;
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //添加秒杀商品 设置过期时间
    @PostMapping("/killProductTime")
    @ResponseBody
    public JSONObject addkillProductToRedis(@RequestBody List<Stock> stockList){
        JSONObject result = new JSONObject();
        String msg = "";
        try {
            for (Stock stock : stockList) {
                redisTemplate.opsForValue().set("kill"+stock.getId(),"id",stock.getKilltime(), TimeUnit.MINUTES);
                LOGGER.info(stock.getName() + "开启秒杀成功！！！...");
            }
            msg ="商品已经全部开启秒杀活动啦~";
        }catch (Exception e){
            throw new BizException("开启秒杀活动失败...");
        }
        result.put("code",200);
        result.put("msg",msg);
        return result;
    }

    /**
     * 上架秒杀 对应商品信息
     * @return
     */
    @PostMapping("/killProduct")
    @ResponseBody
    public String toKillProductPage(String  productKillName,
                                    Integer productKillId,
                                    Integer productKillQuantity,
                                    Integer killTime,
                                    Double productKillPrice,
                                    HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        LOGGER.info(String.valueOf("您要加入秒杀的商品 ID 是："+ productKillId));
        LOGGER.info(String.valueOf("您要加入秒杀的商品 名称 是："+ productKillName));
        LOGGER.info(String.valueOf("您要加入秒杀的商品 数量 是："+ productKillQuantity));
        LOGGER.info(String.valueOf("您要加入秒杀的商品 价格 是："+ productKillPrice));
        LOGGER.info(String.valueOf("所指涉的该商品的 秒杀时长 为："+ killTime));
        //组装 Stock 实体类
        Stock stock = new Stock();
        stock.setStoreId(user.getStoreId());
        stock.setId(productKillId);
        stock.setName(productKillName);
        //默认加入秒杀时 已售为 0 件
//        stock.setSaled(0);
        stock.setCount(productKillQuantity);
        stock.setPrice(productKillPrice);
        stock.setKilltime(killTime);
        //写入秒杀 Stock 表
        boolean save = stockService.save(stock);
        if(!save)
            throw new RuntimeException("新增秒杀商品失败！");
        return "已经写入 Stock 表数据库";
    }

    /**
     * 删除 秒杀商品
     * 跳转 店铺商品页面
     * @return
     */
    @DeleteMapping("/killProduct")
    @ResponseBody
    public JSONObject deleteProductMsg(@RequestBody Stock stock) throws Exception {
        JSONObject result = new JSONObject();
        LOGGER.info(String.valueOf("您要删除秒杀的商品是："+ stock.getName()));
        String msg = "";
        try {
            stockService.removeById(stock.getId());
            msg ="商品下架成功~";
        }catch (Exception e){
            throw new Exception("删除失败...");
        }
        result.put("code",200);
        result.put("msg",msg);
        return result;
    }

    /**
     * 获取 秒杀商品列表
     * 以 JSON 格式 传递给前端
     */
    @GetMapping("/killProductList")
    @ResponseBody
    public JSONObject getMyStoreProducts(HttpSession session){
        JSONObject result = new JSONObject();
        User user = (User) session.getAttribute("user");
        LOGGER.info("用户 商铺 ID 为:" + user.getStoreId());
        QueryWrapper wrapper = new QueryWrapper();
        //店铺ID 应当通过传入的方式
        wrapper.eq("store_id",user.getStoreId());
        //获取店铺商品列表
        List<Stock> killStockList = stockService.list(wrapper);
        LOGGER.info("店铺 秒杀 商品数量为："+killStockList.size());
        result.put("code", "0");
        result.put("msg", "操作成功！");
        result.put("count",killStockList.size());
        result.put("data", killStockList);
        return result;
    }

    /**
     * 渲染 商城秒杀商品页面
     * @param session
     * @return 跳转 秒杀页面
     */
    @GetMapping("/allKillProduct")
    public ModelAndView showAllKillProduct(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allKillProduct");
        List<KillVO> killVOList = stockService.getKillVOList();
        modelAndView.addObject("killProduct",killVOList);
        //传递 “我的购物车"信息
        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else{
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }

    /**
     * 通过商品 ID 搜索显示 秒杀商品详情
     * @param id
     * @param session
     * @return 找到 跳转其详情页面 未找到 则返回主页面
     */
    @GetMapping("/findById/{id}")
    public ModelAndView findById(@PathVariable("id") Integer id, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        //取出该集合
        Product productById = productService.getById(id);
        Stock stockById = stockService.getById(id);
        //组装成 KillVO
        KillVO killVO = new KillVO();
        BeanUtils.copyProperties(productById,killVO);
        BeanUtils.copyProperties(stockById,killVO);
        //查到则设置跳转视图为商品详情
        modelAndView.setViewName("killProductDetail");
        //商品信息
        modelAndView.addObject("killProduct",killVO);
        //传递“我的购物车"信息
        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else{
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }
}

