package com.flyxiaozhu.feignserver2.controller;

import com.flyxiaozhu.feignserver2.service.ServerHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    ServerHelloService serverHelloService; // 编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String like(@RequestParam(value = "name", defaultValue = "flyxiaozhu") String name) {
        return serverHelloService.sayHelloClientOne(name);
    }

    @RequestMapping("/test")
    public String like(){
        return serverHelloService.outTest();
    }

    @RequestMapping("/test2")
    public String test2() {
        return "feign-server-2:" + port;
    }
}
