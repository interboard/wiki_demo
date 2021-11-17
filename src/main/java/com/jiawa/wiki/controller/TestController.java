package com.jiawa.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
public class TestController {
    @Value("${test.hello:TEST}")
    private String testHello;
    /**
     * get,post,put,delete
     */
    @GetMapping("/hello")
    public String hello(){
        return testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(Map<String,String> map){
        return "Hello World! Post," + map.get("name");
    }

}
