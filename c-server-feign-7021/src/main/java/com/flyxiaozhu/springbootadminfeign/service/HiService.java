package com.flyxiaozhu.springbootadminfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "spring-boot-admin-client",fallback = HiServiceImpl.class)
public interface HiService {
    @RequestMapping(value = "/like")
    String sayLikeClientOne(@RequestParam(value = "name") String name);

}
