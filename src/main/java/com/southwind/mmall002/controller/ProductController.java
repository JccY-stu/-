package com.southwind.mmall002.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.southwind.mmall002.controller.Seller.AdminController;
import com.southwind.mmall002.entity.Product;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.CartService;
import com.southwind.mmall002.service.ProductCategoryService;
import com.southwind.mmall002.service.ProductService;
import com.southwind.mmall002.service.file.UploadService;
import com.southwind.mmall002.service.impl.views.ProductViewsServiceImpl;
import com.southwind.mmall002.vo.Seller.StoreProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    UploadService uploadService;

    @Autowired
    private ProductViewsServiceImpl productViewsService;



    /**
     * 渲染 商城分类查询页面
     * @param type
     * @param id
     * @param session
     * @return 跳转 主页面
     */
    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") String type,
                             @PathVariable("id") Integer id,
                             HttpSession session
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        //商品列表信息
        modelAndView.addObject("productList", productService.findByCategoryId(type,id));
        //商品分类信息
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        //传递 “我的购物车"信息
        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else{
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        //商品浏览量信息
        //把浏览量存在 redis 里面，每次都从 redis 里面取
        Map viewsList = productViewsService.getAllViewsFormRedis();
        modelAndView.addObject("viewsList",viewsList);

        return modelAndView;
    }

    /**
     * 搜索 通过商品ID搜索显示商品详情
     * @param id
     * @param session
     * @return 找到 跳转其详情页面 未找到 则返回主页面
     */
    @GetMapping("/findById/{id}")
    public ModelAndView findById(@PathVariable("id") Integer id,
                                 HttpSession session
    ){
        ModelAndView modelAndView = new ModelAndView();
        //先判断该商品是否存在
        //取出该集合
        Product productByIdList = productService.getById(id);
        //判断查询结果是否为空
        if(productByIdList == null){
            //未查到则返回主页面
            modelAndView.setViewName("main");
            //分类信息
            modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
            //传递“我的购物车”信息
            User user = (User) session.getAttribute("user");
            if(user == null){
                modelAndView.addObject("cartList",new ArrayList<>());
            }else{
                modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
            }
           return modelAndView;
        }
        //得到所有商品
        //查到则设置跳转视图为商品详情
        modelAndView.setViewName("productDetail");
        //商品信息
        modelAndView.addObject("product",productByIdList);
        //分类信息
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        //商品浏览量信息
        //进入商品详情页面时，将 Redis里 浏览数 +1
        Integer view = productViewsService.writeViewsToRedisByID(id);
        modelAndView.addObject("view", view);
        //传递“我的购物车"信息
        User user = (User) session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else{
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }

    /**
     * 搜索 通过商品名称搜索显示商品详情
     * @param name
     * @param session
     * @return 找到 跳转其详情页面 未找到 则返回主页面
     */
    @GetMapping("/findByName/{name}")
    public ModelAndView findByName(@PathVariable("name") String name,
                                 HttpSession session
    ){
        ModelAndView modelAndView = new ModelAndView();
        //先判断该商品是否存在
        //取出该集合
        List<Product> productByNameList = productService.findByName(name);
        //判断查询结果是否为空
        if(productByNameList.size() == 0){
            //未查到则返回主页面
            modelAndView.setViewName("main");
            //分类信息
            modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
            //“我的购物车”信息
            User user = (User) session.getAttribute("user");
            if(user == null){
                modelAndView.addObject("cartList",new ArrayList<>());
            }else{
                modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
            }
            return modelAndView;
        }
        //查到则设置跳转视图为查找商品页面
        modelAndView.setViewName("searchProduct");
        //模糊查询商品信息
        modelAndView.addObject("productsByName",productByNameList);
        //分类信息
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
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
     * 上架 新商品
     * 参数列表可以换为对象，此处为了更清晰表现则用 name
     * 跳转 商品上架页面
     */
    @PostMapping("/NewProduct")
    public String upNewProduct(String productName,
                               Integer productQuantity,
                               Integer product_One,
                               Integer product_Two,
                               Integer product_Three,
                               Float price,
                               String description,
                               HttpSession session){

        Product product = new Product();
        product.setName(productName);
        product.setStock(productQuantity);
        product.setCategoryleveloneId(product_One);
        product.setCategoryleveltwoId(product_Two);
        product.setCategorylevelthreeId(product_Three);
        product.setPrice(price);
        product.setDescription(description);

        //把当前商家的 店铺 ID 和店铺名称带过去
        User seller = (User) session.getAttribute("user");
        product.setStoreId(seller.getStoreId());
        product.setStoreName(seller.getStoreName());
        productService.save(product);

        return "redirect:/admin/newProduct";
    }

    /**
     * 上传 新商品图片
     * 跳转 上架页面
     * 注意 这些必须先上传商品信息，拿到要上传图片的 商品 ID 因为设置了 商品 ID 自增
     */
    @PostMapping("/ProductPic")
    @ResponseBody
    public String uploadProductPic(@RequestParam("file") MultipartFile file, HttpSession session){
        //调用 UploadService 上传到 /static/images 目录下
        //获得新头像的文件名
        Map map = new HashMap<String,Object>();
        String fileName = "";
        try {
            fileName = uploadService.uploadProductPic(file,session);
        }catch (Exception e) {
            map.put("msg","error");
            map.put("code",0);
            e.printStackTrace();
        }
        //更新数据库 product 表中的 filename
        Product product = new Product();
        //此处应当传入参数，即要上传图片的商品 ID
        product.setId(766);
        product.setFileName(fileName);
        //通过 id 更新
        productService.updateById(product);
        //跳转到商品上架页面
        return "redirect:/admin/newProduct";
    }


    /**
     * 获取 店铺商品列表
     */
    @GetMapping("/storeProduct")
    @ResponseBody
    public JSONObject getMyStoreProducts(HttpSession session){
        JSONObject result = new JSONObject();
        User user = (User) session.getAttribute("user");
        LOGGER.info("用户商铺ID为:" + user.getStoreId());
        //获取店铺商品列表
        List<StoreProductVO> productList = productService.findAllProductByStoreId(user.getStoreId());
        LOGGER.info("该店铺商品数量为："+productList.size());
        result.put("code", "0");
        result.put("msg", "操作成功！");
        result.put("count",productList.size());
        //此处再加入一个订单数量
        //则查询每个用户在该店的订单数量，写入 orderQuantity 然后 传给前端 涉及到联表查询，先放下不做
        result.put("data", productList);
        //加入浏览次数
//        result.put("",);
        return result;
    }

    /**
     * 修改 店铺商品信息
     * 跳转 商品修改页面
     */
    @PostMapping("/ProductMsg")
    @ResponseBody
    public String reviseProductMsg(String productName,Integer productQuantity,Float price, String description,Integer productId){
        LOGGER.info(String.valueOf("进行信息修改的商品是："+ productName));
        //通过 Id 修改商品
        //重构到 service 层
        Product product = new Product();
        product.setName(productName);
        product.setId(productId);
        product.setStock(productQuantity);
        product.setDescription(description);
        product.setPrice(price);
        productService.updateById(product);
        return "您的商品修改成功！请点击右上角关闭该窗口！";
    }

    /**
     * 删除 对应商品信息
     * 跳转 店铺商品页面
     * @return
     */
    @DeleteMapping("/ProductMsg")
    @ResponseBody
    public JSONObject deleteProductMsg(@RequestBody StoreProductVO storeProductVO) throws Exception {
        JSONObject result = new JSONObject();
        LOGGER.info(String.valueOf("您要删除的商品是："+ storeProductVO.getName()));
        String msg = "";
        try {
            productService.removeById(storeProductVO.getId());
            msg ="商品下架成功~";
        }catch (Exception e){
            throw new Exception("删除失败...");
        }
        result.put("code",200);
        result.put("msg",msg);
        return result;
    }
}

