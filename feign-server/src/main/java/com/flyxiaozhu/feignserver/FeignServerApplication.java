package com.flyxiaozhu.feignserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix  // 或者 @EnableCircuitBreaker 开启断路器功能
@RefreshScope
public class FeignServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignServerApplication.class, args);
	}
}
