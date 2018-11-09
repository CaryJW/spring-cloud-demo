package com.flyxiaozhu.feignserver.controller;

import com.flyxiaozhu.feignserver.service.ServerHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    ServerHelloService serverHelloService; // 编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错

    @RequestMapping("/hello")
    public String like(@RequestParam(value = "name", defaultValue = "flyxiaozhu") String name) {
        return serverHelloService.sayHelloClientOne(name);
    }

    @RequestMapping("/test")
    public String like(){
        return serverHelloService.outTest();
    }
}
