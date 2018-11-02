package com.flyxiaozhu.adminclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RefreshScope
public class AdminClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminClientApplication.class, args);
    }

    @Value("${server.port}")
    private String port;

    @Value("${test}")
    private String test;

    @RequestMapping("/hello")
    public String like(@RequestParam(value = "name") String name) {
        return "Hello," + name + ".my port is:" + port;
    }

    @RequestMapping("/test")
    public String test(){
        return test;
    }
}
