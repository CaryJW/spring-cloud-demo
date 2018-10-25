package com.flyxiaozhu.springbootadminconsumer.controller;

import com.flyxiaozhu.springbootadminconsumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @RequestMapping("/like")
    public String hi(@RequestParam(value = "name",defaultValue = "慧慧") String name){
        return helloService.hiService(name);
    }
}
