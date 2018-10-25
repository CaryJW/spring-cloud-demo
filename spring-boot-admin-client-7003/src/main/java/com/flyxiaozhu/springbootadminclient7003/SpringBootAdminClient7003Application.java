package com.flyxiaozhu.springbootadminclient7003;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class SpringBootAdminClient7003Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminClient7003Application.class, args);
	}

    @Value("${server.port}")
    private String port;

    @RequestMapping("/like")
    public String like(@RequestParam(value = "name") String name) {
        return "Hello," + name + ".my port is:" + port;
    }
}
