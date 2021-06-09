package com.southwind.mmall002.logAOP;

import com.southwind.mmall002.entity.AdminOption;
import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.exception.BizException;
import com.southwind.mmall002.service.AdminOptionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * @Author Chengzhi
 * @Date 2021/6/9 9:08
 * @Version 1.0
 */

@Aspect
@Component
public class WebLogAcpect {

    private Logger logger = LoggerFactory.getLogger(WebLogAcpect.class);

    @Autowired
    AdminOptionService adminOptionService;

    static  User user;
    /**
     * 定义切入点，切入点为com.example.aop下的所有函数
     */
    @Pointcut("execution(public * com.southwind.mmall002.controller.ProductController.up*(..)) || execution(public * com.southwind.mmall002.controller.ProductController.re*(..))")
    public void webLog(){}

    /**
     * 前置通知：在连接点之前执行的通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("user");

        // 记录下请求内容
        logger.info("请求路径: " + request.getRequestURL().toString());
        logger.info("请求方式 : " + request.getMethod());
        logger.info("方法名称 " + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        //装载日志信息
        AdminOption adminOption = new AdminOption();
        adminOption.setStoreId(user.getStoreId())
                .setMethodName( joinPoint.getSignature().getName())
                .setType(request.getMethod())
                .setMsg(Arrays.toString(joinPoint.getArgs()));
       try{
           //插入数据库
           adminOptionService.save(adminOption);
       }catch (BizException e){
           e.printStackTrace();
       }
    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }


}
