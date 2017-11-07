package com.lingdaoyi.timingTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Exceptions;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.feign.CurrencyExchangeOrderClient;

@Component
public class Scheduler {
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
	private static final String signStr = "ainiyiwannian";
	@Autowired
	private CurrencyExchangeOrderClient currencyExchangeOrderClient;
  
	@Scheduled(cron = "0 0/30 * * * ?")
//	@Scheduled(cron = "0/3 * * * * ?")
    public void updateOrderType() {
		Gson gson = new Gson();
		String sign = "";
		try {
			sign = DesUtil3.encryptThreeDESECB(signStr, DesUtil3.KEY);
			String updateOrderType = currencyExchangeOrderClient.updateOrderType(sign);
			ResponseDTO fromJson = gson.fromJson(updateOrderType, ResponseDTO.class);
			if(!fromJson.isSuccess()){
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("外币订单状态修改请求失败！！！");
		}
		
    }    
}
