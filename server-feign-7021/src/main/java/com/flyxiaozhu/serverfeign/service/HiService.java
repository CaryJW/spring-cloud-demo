package com.flyxiaozhu.serverfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "admin-client",fallback = HiServiceImpl.class)
public interface HiService {
    @RequestMapping(value = "/hello")
    String sayLikeClientOne(@RequestParam(value = "name") String name);
}
