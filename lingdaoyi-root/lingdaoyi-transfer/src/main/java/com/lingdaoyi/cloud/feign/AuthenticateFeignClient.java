package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-authenticate-api")
public interface AuthenticateFeignClient {

	/**
	 * 
	 * @param bankcard
	 * @param Mobile
	 * @param cardNo
	 * @param realName
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@RequestMapping(value = "authenticate/exchangeRateChina", method = RequestMethod.POST)
	public String exchangeRateChina(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType);

}
