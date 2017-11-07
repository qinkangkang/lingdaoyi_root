package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-transfer-api")
public interface TransferFeignClient {

	/**
	 * 获取当前账户余额
	 * 
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/balance/getBalance")
	public String getBalance(@RequestParam(value = "accountId", required = true) Long accountId);

	/**
	 * 创建账户余额
	 * 
	 * @param acocuntId
	 */
	@RequestMapping("/balance/createAccountBalance")
	public void createAccountBalance(@RequestParam(value = "acocuntId", required = true) Long acocuntId,
			@RequestParam(value = "currencySign", required = true) String currencySign);

}
