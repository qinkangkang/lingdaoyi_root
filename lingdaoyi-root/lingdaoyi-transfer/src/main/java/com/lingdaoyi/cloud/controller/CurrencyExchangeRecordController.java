package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.CurrencyExchangeRecordService;

@Configuration
@Controller
@SpringBootApplication
@RequestMapping("/exchange")
public class CurrencyExchangeRecordController {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CurrencyExchangeRecordService currencyExchangeRecordService;

	/**
	 * 兑换记录列表
	 */
	@ResponseBody
	@PostMapping(value = "/getExchangeRecordList")
	public String getExchangeRecordList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = currencyExchangeRecordService.getExchangeRecordList(ticket, clientType, pageNum, pageSize);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.fail"));
		}
		return mapper.toJson(responseDTO);
	}
}
