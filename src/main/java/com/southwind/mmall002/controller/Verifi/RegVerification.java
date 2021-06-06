package com.southwind.mmall002.controller.Verifi;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接收前端传来的验证码
 * 与Redis里保存的value进行对比
 * 相同则写入数据库
 * 否则提示出错
 * @param Key 用户验证码在Redis里保存的键名
 * @param scan_code 是用户输入的验证码
 * @param redisTemplate 处理Redis
 * @param user 需要写入数据库的对象
 */

@Controller
public class RegVerification {

    /**
     * 验证 验证码
     * @param loginNameOne
     * @param scan_code
     * @param redisTemplate
     * @return
     */
    @ResponseBody
    public Boolean Verfication(String loginNameOne, String scan_code, RedisTemplate redisTemplate){
        String o = (String) redisTemplate.opsForValue().get(loginNameOne);
        System.out.println("Redis中存储的正确的验证码为：" + o);
        System.out.println("用户输入的验证码为：" + scan_code);
        if(o.equals(scan_code)) {
            System.out.println("验证码正确");
            //调用Mapper写入数据库
            return true;
        }
        else System.out.println("很抱歉，您的验证码有误，请重试");
        return false;
    }
}
