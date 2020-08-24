package com.seven.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seven")
public class WebPage {

    @RequestMapping(value = "/hello")
    public String sayHello(){
        return "Hello springboot!";
    }
}
