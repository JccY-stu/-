package com.southwind.mmall002.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.mmall002.entity.*;
import com.southwind.mmall002.entity.kill.Stock;
import com.southwind.mmall002.exception.BizException;
import com.southwind.mmall002.mapper.*;
import com.southwind.mmall002.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.mmall002.vo.OrderDetailVO;
import com.southwind.mmall002.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */


@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存入 订单
     * @param orders
     * @param user
     * @param address
     * @param remark
     * @return
     */
    @Override
    public boolean save(Orders orders, User user,String address,String remark) {
        //判断是新地址还是老地址
        if(orders.getUserAddress().equals("newAddress")){
            //存入数据库
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress(address);
            userAddress.setRemark(remark);
            userAddress.setIsdefault(1);
            userAddress.setUserId(user.getId());

            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("isdefault",1);
            wrapper.eq("user_id",user.getId());
            UserAddress oldDefault = userAddressMapper.selectOne(wrapper);

            //如果存在老的地址,则将老地址改为0,即非默认
            if(oldDefault != null) {//存在老地址
                oldDefault.setIsdefault(0);
                userAddressMapper.updateById(oldDefault);
            }
            userAddressMapper.insert(userAddress);
            orders.setUserAddress(address);
        }

        //存储orders
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());

        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        orderMapper.insert(orders);

        //存储ordersdetail
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());

        //查到购物车记录
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setId(null);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.insert(orderDetail);
        }

        //清空购物车
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("user_id",user.getId());
        cartMapper.delete(wrapper1);
        return true;
    }

    /**
     * 通过 userId 获取所有的订单信息
     * @param id
     * @return
     */
    @Override
    public List<OrderVO> findAllOrederVOByUserId(Integer id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Orders> ordersList = orderMapper.selectList(wrapper);
        List<OrderVO> orderVOList = ordersList.stream()
                .map(e -> new OrderVO(
                        e.getId(),
                        e.getLoginName(),
                        e.getSerialnumber(),
                        e.getUserAddress(),
                        e.getCost()
                )).collect(Collectors.toList());
        //封装OrderDetail
        for (OrderVO orderVO : orderVOList) {
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("order_id",orderVO.getId());
            List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper1);
            List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                Product product = productMapper.selectById(orderDetail.getProductId());
                BeanUtils.copyProperties(product,orderDetailVO);
                BeanUtils.copyProperties(orderDetail,orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }
            orderVO.setOrderDetailVOList(orderDetailVOList);
        }
        return orderVOList;
    }

    /**
     * 秒杀商品
     * 令牌桶接口限流 + 乐观锁防止超卖 + 接口加盐隐藏
     * @param id
     * @return
     */
    @Override
    public Integer kill(Integer id, HttpSession session, String md5) {
        User user = (User) session.getAttribute("user");
        //判断商品是否在秒杀范围内
        //校验 redis 中秒杀商品是否存在
        if(!redisTemplate.hasKey("kill" + id)) {
            throw new BizException("当前商品的抢购活动已经结束啦~~~");
        }
        //验证接口加盐签名
        String hashKey = "KEY_" + user.getId() + "_" + id;
        //获取 redis 里面的 key
        if(!redisTemplate.opsForValue().get(hashKey).equals(md5))
            throw new BizException("当前请求数据不合法，请稍后再试！");
        //校验数据库
        Stock stock = checkStock(id);
        LOGGER.info("商品的名称为:" + stock.getName());
        LOGGER.info("商品的已售为:" + stock.getSaled());
        LOGGER.info("商品的库存为:" + stock.getCount());
        //更新库存
        updateSale(stock);
        //创建订单
        return createOrder(stock,session);
    }

    /**
     * 秒杀接口隐藏
     * 加盐操作
     * 生成MD5签名
     * @param id
     * @param session
     * @return
     */
    @Override
    public String getKillMd5(Integer id,HttpSession session) {
        //1.验证用户合法性 userId 是否存在
        //2.验证商品 id 是否存在
        //3.生成 md5 签名放入 redis 服务
        User user = (User) session.getAttribute("user");
        //生成 hashKey
        String hashKey = "KEY_" + user.getId() + "_" + id;
        //生成 md5  !QS#是一个随机盐，随机生成
        String key = String.valueOf(DigestUtils.md5Digest((user.getId()+id+"!Q*jS#").getBytes()));
        redisTemplate.opsForValue().set(hashKey,key,3600, TimeUnit.SECONDS);
        LOGGER.info("Redis 写入： [{}] [{}]" , hashKey,key);
        return key;
    }

    //校验库存
    private Stock checkStock(Integer id){
        Stock stock = stockMapper.selectById(id);
        if(stock.getSaled().equals(stock.getCount())){
            throw new RuntimeException("库存不足！！！");
        }
        return stock;
    }

    //扣除库存
    private void updateSale(Stock stock){
        LOGGER.info("准备更新库存...");
        //在 SQL 层面完成销量的+1 和 版本号的+1 并且根据商品 ID 和版本号同时查询更新的商品
        stock.setSaled(stock.getSaled() + 1);
        int updateRows = stockMapper.updateSale(stock);//返回更新的条数
        if(updateRows == 0 ){
            throw new RuntimeException("抢购失败，请重试!!!");
        }
    }

    //创建订单
    private Integer createOrder(Stock stock,HttpSession session){
        User user= (User) session.getAttribute("user");
        Orders orders = new Orders();
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());
        orders.setSerialnumber(stock.getName());
        orderMapper.insert(orders);
        return orders.getId();
    }

}
