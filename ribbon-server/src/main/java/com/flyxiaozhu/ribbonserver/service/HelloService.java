package com.flyxiaozhu.ribbonserver.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError") // 熔断器
    public String hiService(String name) {
        return restTemplate.getForObject("http://server-hello/hello?name=" + name, String.class);
    }

    public String hiError(String name) {
        return "Hi," + name + ".sorry!!!error...";
    }
}
