package com.southwind.mmall002.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Chengzhi
 * @Date 2021/5/8 20:10
 * @Version 1.0
 *
 * 拦截器，加了    @TableField(fill = FieldFill.INSERT) 注解之后自动触发拦截器的方法
 *
 * 添加 注册时间、更新时间
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);//时间不匹配有bug 将new Date()改掉
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
    }
}
