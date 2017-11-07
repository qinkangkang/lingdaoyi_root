package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-system-api")
public interface SystemFeignClient {
	@RequestMapping(value="/sponsor/getSponsorById",method=RequestMethod.POST)
	public String getSponsorById(@RequestParam("sponsorId")Long sponsorId);
}
