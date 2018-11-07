package com.flyxiaozhu.serverfeign.service;

import org.springframework.stereotype.Component;

@Component
public class HiServiceImpl implements HiService {
    @Override
    public String sayLikeClientOne(String name) {
        return "sorry," + name;
    }
}
