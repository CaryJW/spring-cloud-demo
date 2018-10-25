package com.flyxiaozhu.springbootadminzuul7012;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class SpringBootAdminZuul7012Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminZuul7012Application.class, args);
	}
}
