package com.flyxiaozhu.springbootadminconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix // 开启短路器
public class SpringBootAdminConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminConsumerApplication.class, args);
	}

	@Bean
    @LoadBalanced // 开启负载均衡功能 轮询
    RestTemplate restTemplate(){
	    return new RestTemplate();
    }
}
