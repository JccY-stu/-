package com.southwind.mmall002.config;

import com.southwind.mmall002.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new UserFilter());
        //设置拦截路径
        filterRegistrationBean.addUrlPatterns("/cart/*","/orders/*","/user/userInfo","/userAddress/*");
        return filterRegistrationBean;
    }
}
