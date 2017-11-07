package com.lingdaoyi.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lingdaoyi.cloud.dto.AccountSysDTO;

@FeignClient("lingdaoyi-transfer-api")
public interface TransferFeignClient {
	
	@RequestMapping(value = "record/accountTransfer", method = RequestMethod.POST)
	public  String orderTransfer(
			@RequestParam("type")String type,
			@RequestParam("ticket")String ticket,
			@RequestParam("clientType")Integer clientType,
			@RequestParam("transferAmount")String transferAmount,
			@RequestParam("receiveAccountId")String receiveAccountId,
			@RequestParam("payPassword")String payPassword,
			@RequestParam("orgCurrency")String orgCurrency,
			@RequestParam("transCurrency")String transCurrency,
			@RequestParam("exchageTime")String exchageTime,
			@RequestParam("exchangeValue")String exchangeValue,
			@RequestParam("remarks")String remarks,
			@RequestParam("afterTransferAmount")String afterTransferAmount,
			@RequestParam("fingerprintSuccess")String fingerprintSuccess,
			@RequestParam("sign")String sign);

}
