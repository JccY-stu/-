package com.southwind.mmall002.entity.kill;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Stock implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 库存
     */
      private Integer count;

      /**
     * 已售
     */
      private Integer saled;

      /**
     * 商品ID
     */
        private Integer id;

      /**
     * 商品名称
     */
      private String name;

      /**
     * 乐观锁，版本号
     */
      private String version;

      /**
      * 秒杀时长
      */
      private Integer killtime;

      /**
       * 所属店铺 ID
       */
      private Integer storeId;

      /**
       * 秒杀价格
       */
      private Double price;

  @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;

}
