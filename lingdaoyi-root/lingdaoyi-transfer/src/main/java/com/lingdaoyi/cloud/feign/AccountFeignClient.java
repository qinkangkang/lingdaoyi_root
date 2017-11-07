package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-account-api")
public interface AccountFeignClient {
	
	
	@RequestMapping(value="/account/getAccountByTicket",method=RequestMethod.POST)
	public String getAccountByTicket(@RequestParam("ticket")String ticket,@RequestParam("clientType") Integer clientType);
	
	@RequestMapping(value="/account/getAccountById",method=RequestMethod.POST)
	public String getAccountById(@RequestParam("accountId")Long accountId);

	/**
	 * 获取国际信息
	 * @param currencySort
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountCountry/getCountryInfo", consumes = "application/json")
	public String getCountryInfo(@RequestParam(value = "currencySort", required = false)String currencySort);
}
