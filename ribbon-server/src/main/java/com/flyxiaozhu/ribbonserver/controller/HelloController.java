package com.flyxiaozhu.ribbonserver.controller;

import com.flyxiaozhu.ribbonserver.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @RequestMapping("/hello")
    public String hi(@RequestParam(value = "name", defaultValue = "flyxiaozhu") String name) {
        return helloService.hiService(name);
    }
}
