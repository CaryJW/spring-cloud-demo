package com.flyxiaozhu.traceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class TraceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraceServerApplication.class, args);
	}
}
