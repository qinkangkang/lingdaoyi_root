package com.lingdaoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableEurekaClient //eureka的客户端,提供服务
@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@EnableFeignClients
@MapperScan(basePackages = "com.lingdaoyi.cloud.mapper")
public class LoginSysApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LoginSysApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LoginSysApplication.class, args);
	}
}
