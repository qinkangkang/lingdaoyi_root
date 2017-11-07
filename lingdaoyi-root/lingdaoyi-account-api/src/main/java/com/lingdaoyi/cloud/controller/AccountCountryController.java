package com.lingdaoyi.cloud.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.lingdaoyi.cloud.dto.AccountCountryInfoDTO;
import com.lingdaoyi.cloud.dto.CountryFlagDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountCountryService;
import com.lingdaoyi.cloud.utils.IpUtil;

@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/accountCountry")
public class AccountCountryController {
	private static final Logger logger = LoggerFactory.getLogger(AccountCountryController.class);
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountCountryService accountCountryService;

	@PostMapping(value = "/getAccountCountryList")
	public String getAccountCountryList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountCountryService.getAccountCountryList(clientType, sign, IpUtil.getRequestIp());
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.country.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@PostMapping("/getCountryInfo")
	public String getCountryInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "currencySort", required = false)String currencySort){
		AccountCountryInfoDTO responseDTO = null;
		try {
			responseDTO = accountCountryService.getAccountCountryInfo(currencySort);
		} catch (Exception e) {
			responseDTO = new AccountCountryInfoDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.country.fail"));
		}
		return mapper.toJson(responseDTO);
		
	}
	
	@PostMapping(value = "/addCountryAndCurrencyRedis")
	public String addCountryAndCurrencyRedis(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id) {

		try {
			accountCountryService.addCountryAndCurrencyRedis();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return mapper.toJson("国籍和货币缓存添加成功");
	}
	
	@PostMapping(value = "/getCountryFlagList")
	public String getCountryFlagList(HttpServletRequest request, HttpServletResponse response){
//		ResponseDTO dto  = new ResponseDTO();
//		Map<String,Object> map = new HashMap<>();
		List<CountryFlagDTO> countryFlagList = null;
		try {
			countryFlagList = accountCountryService.getCountryFlagList();
		} catch (Exception e) {
			countryFlagList = new ArrayList<>();
		}
//		map.put("data", countryFlagList);
//		dto.setData(map);
		return mapper.toJson(countryFlagList);
	}
	
}
