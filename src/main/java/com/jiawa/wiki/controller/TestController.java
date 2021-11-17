package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



@RestController
public class TestController {
    @Value("${test.hello:TEST}")
    private String testHello;

    @Resource
    private TestService testService;
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

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

}
