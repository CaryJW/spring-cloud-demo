package com.flyxiaozhu.feignserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "server-hello",fallback = ServerHelloServiceImpl.class)
public interface ServerHelloService {
    @RequestMapping(value = "/hello")
    String sayHelloClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    String outTest();
}
