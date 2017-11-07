package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.PUBLIC_MEMBER;
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
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountBankCardService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;

@RestController
@RequestMapping("/bankCard")
public class AccountBankCardController {

	private static final Logger logger = LoggerFactory.getLogger(AccountRecordController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountBankCardService accountBankCardService;

	/**
	 * 绑定银行卡
	 * 
	 * @param ticket:用户唯一标识
	 * @param backCardNO:银行卡号码
	 * @param securityCode:验证码
	 * @param sign:加密规则
	 * 
	 * @return
	 */
	@RequestMapping("/bindingBankCard")
	public String bindingBankCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "backCardNO", required = true) String backCardNO,
			@RequestParam(value = "bankName", required = true) String bankName,
			@RequestParam(value = "bankImage", required = true) String bankImage,
			@RequestParam(value = "bankCode", required = true) String bankCode,
			@RequestParam(value = "cardType", required = true) String cardType,
			@RequestParam(value = "telephone", required = true) String telephone,
			@RequestParam(value = "securityCode", required = true) String securityCode,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountBankCardService.bindingBankCard(clientType, ticket, backCardNO, securityCode, sign,
					IpUtil.getRequestIp(), bankName, bankCode, cardType, bankImage, telephone);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCard.fail"));
		}

		return mapper.toJson(responseDTO);
	}

	/**
	 * 根据ticket查询当前账户绑定的银行卡
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/getBankCardList")
	public String getBankCardList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountBankCardService.getBankCardList(ticket, clientType, sign, IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg("获取绑定银行卡信息出错！");
		}

		return mapper.toJson(responseDTO);
	}

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountBankCardService.test(IpUtil.getRequestIp());
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
	 * 银行卡解绑
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param sign
	 * @return
	 */

	@PostMapping(value = "/deleteBankCard")
	public String deleteBankCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "bankCardId", required = true) Long bankCardId,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountBankCardService.deleteBankCard(ticket, bankCardId, clientType, sign,
					IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.deleteBackCard.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
