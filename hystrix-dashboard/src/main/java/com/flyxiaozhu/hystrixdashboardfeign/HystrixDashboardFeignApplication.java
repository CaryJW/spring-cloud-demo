package com.flyxiaozhu.hystrixdashboardfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrixDashboard // 启用 Hystrix Dashboard 功能
public class HystrixDashboardFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardFeignApplication.class, args);
	}
}
