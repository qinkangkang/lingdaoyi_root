package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountLoginService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/accountLogin")
public class AccountLoginController {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static final Logger logger = LoggerFactory.getLogger(AccountLoginController.class);

	@Autowired
	private AccountLoginService accountLoginService;

	@ResponseBody
	@PostMapping(value = "/sendAccountSms")
	public String sendAccountSms(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "accountPhone", required = false) String accountPhone,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.sendAccountLoginSms(clientType, accountPhone, IpUtil.getRequestIp(), type,
					sign);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.fail"));
		}

		return mapper.toJson(responseDTO);

	}

	@PostMapping(value = "/quickLogin")
	public String quickLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "accountPhone", required = false) String accountPhone,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.quickLogin(clientType, checkCode, accountPhone, sign,
					IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.fail"));
		}

		return mapper.toJson(responseDTO);

	}

	@PostMapping(value = "/accountPasswordLogin")
	public String accountPasswordLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "loginPassword", required = false) String loginPassword,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.accountPasswordLogin(clientType, loginName, loginPassword, sign,
					IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.password.fail"));
		}

		return mapper.toJson(responseDTO);

	}

	@ResponseBody
	@PostMapping(value = "/cheakCodePhone")
	public String cheakCodePhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "accountPhone", required = false) String accountPhone,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "type", required = false) Integer type) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.cheakCode(clientType, accountPhone, checkCode, IpUtil.getRequestIp(),
					type);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.fail"));
		}

		return mapper.toJson(responseDTO);

	}

	@PostMapping(value = "/registerAccount")
	public String registerAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "countryId", required = false) Long countryId,
			@RequestParam(value = "loginPassword", required = false) String loginPassword,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.registerAccount(clientType, loginName, countryId,
					loginPassword, deviceId, deviceTokens, sign, IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.register.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	@PostMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.logout(clientType, ticket, IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLoginService.test(IpUtil.getRequestIp());
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
