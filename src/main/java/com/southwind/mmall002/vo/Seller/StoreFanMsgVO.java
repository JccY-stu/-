package com.southwind.mmall002.vo.Seller;

import com.southwind.mmall002.enums.GenderEnum;
import lombok.Data;


/**
 * 店铺粉丝 VO
 *
 * @Author Chengzhi
 * @Date 2021/5/21 15:54
 * @Version 1.0
 */
@Data
public class StoreFanMsgVO {

    private Integer id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 性别(1:男 0：女)
     */
    private GenderEnum gender;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 在该店铺的订单数量
     */
    private int orderQuantity;

}
