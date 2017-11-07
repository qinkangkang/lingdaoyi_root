package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.IndexService;
import com.lingdaoyi.cloud.service.SystemService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.init.Constant;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/index")
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private IndexService indexService;
	
	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/getIndexInfo")
	@ResponseBody
	public String getNewIndexPageInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "countryId", required = false, defaultValue = "1") Long countryId,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = indexService.getIndexPageInfo(countryId, clientType, sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.getIndexInfo.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@RequestMapping(value = "/sysColumns")
	@ResponseBody
	public String sysColumns(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "countryId", required = false, defaultValue = "1") Long countryId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = indexService.sysColumns(type, countryId, sign, IpUtil.getIpAddr(request), clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.sysColumns.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = indexService.test(IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@PostMapping(value = "/getAppVersion")
	public String getAppVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.getAppVersion(clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.getAppVersion.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
