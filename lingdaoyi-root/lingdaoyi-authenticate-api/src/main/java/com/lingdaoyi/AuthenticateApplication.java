package com.lingdaoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.lingdaoyi.cloud.feignclient")
@SpringBootApplication
@EnableAutoConfiguration
@MapperScan(basePackages="com.lingdaoyi.cloud.mapper")
public class AuthenticateApplication  extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthenticateApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthenticateApplication.class, args);
    }

}
