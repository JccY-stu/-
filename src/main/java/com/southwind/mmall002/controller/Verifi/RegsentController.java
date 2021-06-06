package com.southwind.mmall002.controller.Verifi;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 接收前端传来的参数并发送验证码
 * 保存到Redis里
 * 返回该用户存进Redis验证码的key
 */
@Controller
public class RegsentController {

    /**
     * 发送 注册验证码
     * @param loginName
     * @param redisTemplate
     * @return
     */
    @ResponseBody
    public String sendmessage(String loginName,RedisTemplate redisTemplate){
        //这里的userId在真实业务中通过个人身份的令牌获取
        //生成六位数随机验证码
        String code = getCode();
        //设置redis的key，这里设置为 用户名
        String redisKey = loginName;
        //将这个验证码存入redis中，并设置失效时间为5分钟
        redisTemplate.opsForValue().set(redisKey,code,300, TimeUnit.SECONDS);
        System.out.println("存入" + redisKey + "值为：" +code );
        //发送短信
        boolean isSuccess = send(code);
        if (isSuccess){
            System.out.println("成功发送");
            return code;
        }
        return "";
    }

    private boolean send(String code) {
        String msg="验证码为："+code+"，验证码有效期5分钟，请及时验证";
        System.out.println(msg);
        return true;
    }
    //生成六位随机验证码
    private static String getCode() {
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }
}
