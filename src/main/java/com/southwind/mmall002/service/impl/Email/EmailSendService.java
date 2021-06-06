package com.southwind.mmall002.service.impl.Email;

import com.southwind.mmall002.entity.User;
import com.southwind.mmall002.service.EmailSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Transactional(rollbackFor = Exception.class)
@Service
public class EmailSendService implements EmailSend {


    @Autowired
    JavaMailSenderImpl javaMailSender;

    private static Logger logger = LoggerFactory.getLogger(com.southwind.mmall002.service.impl.Email.EmailSendService.class);
    /**
     * @Description:通过@Async注解表明该方法是一个异步方法，
     * 如果注解在类级别上，则表明该类所有的方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor
     */
    @Async
    public void executeAsyncTask1(User user, HttpSession session){
        logger.info(Thread.currentThread().getName());
        User seller = (User) session.getAttribute("user");
        SimpleMailMessage message = new SimpleMailMessage();
        logger.info("发送方：" + seller.getEmail());
        message.setTo(user.getEmail());
        message.setSubject("你好");
        message.setText("Msg");
        message.setFrom(seller.getEmail());
        javaMailSender.send(message);
        logger.info(Thread.currentThread().getName() +"EmailSendService ==> executeAsyncTask1 method: 执行异步任务{} " +  "发送邮件给" + user.getLoginName());
    }

    /**
     * @Description:通过@Async注解表明该方法是一个异步方法，
     * 如果注解在类级别上，则表明该类所有的方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor
     */
    @Async
    public void executeAsyncTask2(User user,HttpSession session){
        logger.info(Thread.currentThread().getName());
        SimpleMailMessage message = new SimpleMailMessage();
        User seller = (User) session.getAttribute("user");
        logger.info("发送方：" + seller.getEmail());
        message.setTo(user.getEmail());
        message.setSubject("你好");
        message.setText("Msg");
        message.setFrom(seller.getEmail());
        javaMailSender.send(message);
        logger.info(Thread.currentThread().getName() + "EmailSendService ==> executeAsyncTask2 method: 执行异步任务{} "+ "发送邮件给" + user.getLoginName());
    }
}