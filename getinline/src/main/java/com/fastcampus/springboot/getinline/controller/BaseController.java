package com.fastcampus.springboot.getinline.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController{

    @GetMapping("/")
    public String root() throws Exception{
        throw new Exception("테스트");     // -> BaseExceptionHandler 호출, error view 반환
//        return "index";
    }
}
