package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-transfer-api")
public interface CurrencyExchangeOrderClient {

	@RequestMapping(method = RequestMethod.POST, value = "/currencyexchangeorder/updateordertype", consumes = "application/json")
	public String updateOrderType(@RequestParam(value = "sign", required = false) String sign);
}
