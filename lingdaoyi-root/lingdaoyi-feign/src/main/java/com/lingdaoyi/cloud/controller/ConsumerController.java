package com.lingdaoyi.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.lingdaoyi.cloud.feign.ProducerFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ConsumerController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProducerFeignClient producerFeignClient;

	/**
	 * 测试调用feignClient接口
	 * 
	 * @return
	 */

	@RequestMapping("feign")
	@HystrixCommand(fallbackMethod = "findByIdFallback", commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
	public String getPerson1() {
		return producerFeignClient.getPerson();
	}

	/**
	 * 配置commandProperties = @HystrixProperty(name = "execution.isolation.strategy",
	 * value = "SEMAPHORE") 与熔断方法在同一线程工作
	 * 
	 * 1.sercurity and scopes
	 * 
	 * @SessionScope @Scope("")
	 * @RequestScope
	 * 
	 * 				2.Health indicator
	 * 
	 *               3.hystrix.stream 监控 api 线程等。
	 * 
	 *               4.hystrix Dashboard
	 * 
	 * @return
	 */
	@RequestMapping("/hello2")
	@HystrixCommand(fallbackMethod = "findByIdFallback", commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
	public String getPerson() {
		String forObject = restTemplate.getForObject("http://lingdaoyi-producer/hello", String.class);
		return forObject;
	}

	/**
	 * 断路器test方法
	 * 
	 * @return
	 */
	public String findByIdFallback() {
		// 断路器方法，想要短路的方法返回什么类型则这个方法与之一样
		// Person user = new Person();
		// user.setId(0);
		// user.setAge(0);
		// user.setName("小王");
		String xiaowang = "隔壁老王八";
		return xiaowang;
	}

}
