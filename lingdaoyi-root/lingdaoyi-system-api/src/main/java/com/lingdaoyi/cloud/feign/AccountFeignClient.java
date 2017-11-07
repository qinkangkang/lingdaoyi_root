package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lingdaoyi.cloud.dto.AccountSysDTO;

@FeignClient("lingdaoyi-account-api")
public interface AccountFeignClient {
	
	@RequestMapping(value = "account/getAccountByTicket", method = RequestMethod.POST)
	public  String getAccountByTicket(@RequestParam("ticket")String ticket,@RequestParam("clientType")Integer clientType);

}
