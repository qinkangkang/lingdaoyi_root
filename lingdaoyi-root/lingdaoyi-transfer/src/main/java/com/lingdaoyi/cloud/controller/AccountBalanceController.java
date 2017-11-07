package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountBalanceDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountBalanceService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;

@RestController
@RequestMapping("/balance")
public class AccountBalanceController {

	private static final Logger logger = LoggerFactory.getLogger(AccountRecordController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountBalanceService accountBalanceService;

	/**
	 * 获取当前账户的余额
	 * 
	 * @param ticket
	 * @param clientType
	 * @param account_id
	 * @return
	 */
	@RequestMapping("/getAccountBalance")
	public String getAccountBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "sign", required = true) String sign) {

		ResponseDTO responseDTO = null;

		try {
			responseDTO = accountBalanceService.getAccountBalance(ticket, clientType, sign, IpUtil.getRequestIp());
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.balance.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	@RequestMapping("/getAccountBalanceById")
	public String getAccountBalanceById(Long accountId) {
		AccountBalanceDTO balanceDTO = accountBalanceService.getAccountBalanceById(accountId);
		return mapper.toJson(balanceDTO);
	}

	/**
	 * 为用户创建余额账户
	 */
	@RequestMapping("/createAccountBalance")
	public void createAccountBalance(@RequestParam(value = "acocuntId", required = true) Long acocuntId,
			@RequestParam(value = "currencySign", required = true) String currencySign) {
		accountBalanceService.createAccountBalance(acocuntId,currencySign);

	}

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountBalanceService.test(IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 余额提现
	 * 
	 * @param request
	 * @param response
	 * @param acocuntId
	 * @return
	 */
	@RequestMapping("/withdrawBalance")
	public String withdrawBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "payPassword", required = true) String payPassword,
			@RequestParam(value = "money", required = true) String money,
			@RequestParam(value = "bankcardId",required = false) Long bankcardId,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "sign", required = true) String sign,
			@RequestParam(value = "type", required = true)Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = accountBalanceService.withdrawBalance(payPassword, money, bankcardId, ticket, clientType,
					sign, IpUtil.getIpAddr(request),type);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.withdrawbalance.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@RequestMapping("/getBalance")
	public String getBalance(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = true) Long accountId) {
		AccountBalanceDTO accountBalanceDTO = accountBalanceService.getBalance(accountId);
		return mapper.toJson(accountBalanceDTO);
	}

}
