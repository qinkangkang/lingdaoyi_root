package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountBalanceService;
import com.lingdaoyi.cloud.service.AccountExchangeService;
import com.lingdaoyi.cloud.utils.IpUtil;

@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/exchange")
public class AccountExchangeController {
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	private static final Logger logger = LoggerFactory.getLogger(AccountRecordController.class);

	@Autowired
	private AccountExchangeService exchangeService;

	/**
	 * 汇率转换
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param money
	 * @param receivedAccountId
	 * @param sign
	 * @return
	 */

	@PostMapping("/getExchangeValue")
	public String exchangeMoney(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "money", required = true) String transferAmount,
			@RequestParam(value = "receivedAccountId", required = true) Long receivedAccountId,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = exchangeService.exchangeMoney(ticket, clientType, transferAmount, receivedAccountId,
					IpUtil.getRequestIp(), sign);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.exchange.fail"));
		}

		return mapper.toJson(responseDTO);
	}
	
	
	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = exchangeService.test(IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout.fail"));
		}
		return mapper.toJson(responseDTO);

	}

}
