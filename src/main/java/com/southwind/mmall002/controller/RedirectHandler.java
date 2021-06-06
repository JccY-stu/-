package com.southwind.mmall002.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Chengzhi
 * @Date 2021/5/7 11:25
 * @Version 1.0
 *
 * 页面映射
 */

@Controller
public class RedirectHandler {

    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url){
        return url;
    }

    @GetMapping("/")
    public String main(){//转发 一次请求
        return "redirect:/productCategory/list";
    }
}
