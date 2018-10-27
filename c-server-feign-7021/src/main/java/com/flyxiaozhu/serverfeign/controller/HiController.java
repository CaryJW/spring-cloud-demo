package com.flyxiaozhu.serverfeign.controller;

import com.flyxiaozhu.serverfeign.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    HiService hiService; // 编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错

    @RequestMapping("/like")
    public String like(@RequestParam(value = "name", defaultValue = "慧慧") String name) {
        return hiService.sayLikeClientOne(name);
    }
}
