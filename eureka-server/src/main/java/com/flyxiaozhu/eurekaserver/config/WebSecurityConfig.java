package com.flyxiaozhu.eurekaserver.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception { // spring cloud security 默认启用了csrf，需要关闭（新建一个类实现）
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
        super.configure(http);
    }
}
