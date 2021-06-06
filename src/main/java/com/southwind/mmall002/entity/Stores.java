package com.southwind.mmall002.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-05-19
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Stores implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "store_id", type = IdType.AUTO)
      private Integer storeId;

    private String storeName;

    private Integer adminId;

    private Integer quantity;

    // 注意 驼峰命名法，与数据库表中字段对应
    private Integer orderQuantity;

}
