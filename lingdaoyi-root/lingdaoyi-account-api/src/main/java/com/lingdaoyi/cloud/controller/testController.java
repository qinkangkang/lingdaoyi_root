package com.lingdaoyi.cloud.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/test")
public class testController {

	@ResponseBody
	@PostMapping(value = "/phone")
	public void test(String phone) {
		System.out.println(phone);

	}

}
