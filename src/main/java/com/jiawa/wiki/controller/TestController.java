package com.jiawa.wiki.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    /**
     * get,post,put,delete
     */
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
