package com.flyxiaozhu.feignserver.service;

import org.springframework.stereotype.Component;

@Component
public class ServerHelloServiceImpl implements ServerHelloService {
    @Override
    public String sayHelloClientOne(String name) {
        return "sorry," + name + ",error!!!";
    }

    @Override
    public String outTest() {
        return "sorry,error!!!";
    }
}
