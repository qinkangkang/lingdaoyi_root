package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient("lingdaoyi-producer")
public interface ProducerFeignClient {
	
	@RequestMapping("person/testTimer")
	public String testTimer();

}
