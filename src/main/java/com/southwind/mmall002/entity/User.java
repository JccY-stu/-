package com.southwind.mmall002.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.southwind.mmall002.enums.GenderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 小杨
 * @since 2021-05-09
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
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
     * 密码
     */
      private String password;

      /**
     * 性别(1:男 0：女)
     */
      private GenderEnum gender;

      /**
     * 身份证号
     */
      private String identityCode;

      /**
     * 邮箱
     */
      private String email;

      /**
     * 手机
     */
      private String mobile;

      /**
       * 数据库村的是图片的名称
       */
      private String fileName;


      /**
       * 备注：可以是商家描述
       */
      private String remark;

      /**
       * 角色：商家和用户
       */
      private String role;

      /**
       * 商家 包括 拥有的店铺ID
       */
      private Integer storeId;

      /**
       * 商家 包括 拥有的店铺名称
       */
      private  String storeName;

  @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}
