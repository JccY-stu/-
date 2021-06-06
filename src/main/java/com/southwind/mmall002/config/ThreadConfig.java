package com.southwind.mmall002.config;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration//声明为配置文件并装配到spring
@EnableAsync//开启异步任务支支持
public class ThreadConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(30);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(200);
        //空闲的线程多久时间后被销毁
        taskExecutor.setKeepAliveSeconds(25);
        //执行初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
