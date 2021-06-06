package com.southwind.mmall002.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 小杨
 * @since 2021-05-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Product01 implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 名称
     */
      private String name;

    private Integer storeId;

    private String storeName;

      /**
     * 描述
     */
      private String description;

      /**
     * 价格
     */
      private Float price;

      /**
     * 库存
     */
      private Integer stock;

      /**
     * 分类1
     */
      private Integer categoryleveloneId;

      /**
     * 分类2
     */
      private Integer categoryleveltwoId;

      /**
     * 分类3
     */
      private Integer categorylevelthreeId;

      /**
     * 文件名称
     */
      private String fileName;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;

      /**
     * 浏览次数
     */
      private Integer view;


}
