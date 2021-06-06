package com.southwind.mmall002.entity;

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
 * @since 2021-05-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class OptionDetail implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer id;

    private String name;

    private String title;

    @TableField("Msg")
    private String Msg;

      @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;


}
