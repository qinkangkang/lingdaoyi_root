package com.lingdaoyi.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountCountryInfoDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.BankCardDTO;
import com.lingdaoyi.cloud.dto.BankCardDTO.BankCardResult;
import com.lingdaoyi.cloud.dto.CountryFlagDTO;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO2;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO3;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO3.CurrencyResult;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO3.CurrencyResult.CurrencyVlue;
import com.lingdaoyi.cloud.dto.ExchangeRateResponseDTO;
import com.lingdaoyi.cloud.dto.IdCardDTO;
import com.lingdaoyi.cloud.dto.IdCardDTO.IdCardResult;
import com.lingdaoyi.cloud.dto.IdCardDTO.IdCardResult.IdCardInfor;
import com.lingdaoyi.cloud.dto.OcrIdCardDTO;
import com.lingdaoyi.cloud.dto.OcrIdCardDataDTO;
import com.lingdaoyi.cloud.dto.RateInfoDTO;
import com.lingdaoyi.cloud.dto.RateInfoDTO.RateResult;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.feignclient.AccuontClient;
import com.lingdaoyi.cloud.service.AuthenticationRecordService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.authentication.BankCardUtil;
import com.lingdaoyi.cloud.utils.authentication.ExchangeRateUtil;
import com.lingdaoyi.cloud.utils.authentication.ExchangeRateUtil2;
import com.lingdaoyi.cloud.utils.authentication.IDcardUtil;
import com.lingdaoyi.cloud.utils.authentication.OcrIDcardUtil;
import com.lingdaoyi.cloud.utils.authentication.OcrIdCardDTOUtil;
import com.lingdaoyi.cloud.utils.base.IdcardValidator;
import com.lingdaoyi.cloud.utils.base.RegexUtils;

/**
 * 鉴权controller
 * 
 * @author jack
 *
 */
@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/authenticate")
public class AuthenticateController {
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationRecordService.class);
	@Autowired
	private AccuontClient accountClient;
	// 鉴权流水service
	@Autowired
	private AuthenticationRecordService authenticationRecordService;

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = authenticationRecordService.test(IpUtil.getRequestIp());
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
	 * @param idcard
	 *            身份证号
	 * @param realname
	 *            真实姓名
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/certificationIDcard")
	public String certificationIDcard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "idcard", required = false) String idcard,
			@RequestParam(value = "realname", required = false) String realname,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		if (StringUtils.isNotBlank(ticket)) {
			String accountSysDTOString = accountClient.getAccountByTicket(ticket, clientType);
			Gson gson = new Gson();
			accountSysDTO = gson.fromJson(accountSysDTOString, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
			String accountId = accountSysDTO.getAccountId();
			long accountIdL = Long.parseLong(accountId);
			String authStatus = accountClient.getAuthStatus(accountIdL);
			if (authStatus.equals("1")) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.error"));
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		String accountId = accountSysDTO.getAccountId();
		long accountIdL = Long.parseLong(accountId);
		String decryptIdcard = "";
		String decryptrealname = "";
		try {
			System.out.println("加密身份证：" + idcard);
			System.out.println("加密名字：" + realname);
			decryptIdcard = DesUtil3.decryptThreeDESECB(idcard, DesUtil3.KEY);
			decryptrealname = DesUtil3.decryptThreeDESECB(realname, DesUtil3.KEY);
			System.out.println("解密身份证：" + decryptIdcard);
			System.out.println("解密名字：" + decryptrealname);
			if (!RegexUtils.isChinese(decryptrealname)) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setMsg("realname" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				return mapper.toJson(dto);
			}

			if (!IdcardValidator.isValidate18Idcard(decryptIdcard)) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setMsg("idcard" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				return mapper.toJson(dto);
			}

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			dto = new ResponseDTO();
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			return mapper.toJson(dto);
		}
		IdCardDTO checkIDcard = IDcardUtil.checkIDcard(decryptIdcard, decryptrealname);
		IdCardResult result = checkIDcard.getResult();
		String error_code = checkIDcard.getError_code();
		if (error_code.equals("0")) {
			dto = new ResponseDTO();
			if (result.isIsok()) {
				IdCardInfor idCardInfor = result.getIdCardInfor();
				accountClient.saveIdentityInfo(accountIdL, idcard, realname, idCardInfor.getArea(),
						idCardInfor.getSex(), DesUtil3.KEY, ticket, clientType);
				authenticationRecordService.insertAuthenticationRecord(accountIdL, realname, idcard, null, null, null,
						null, null, null, null, DesUtil3.KEY, 1, null);
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.ok"));
			} else {
				authenticationRecordService.insertAuthenticationRecord(accountIdL, realname, idcard, null, null, null,
						null, null, null, null, DesUtil3.KEY, 0, null);
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.no"));
			}

			dto.setSuccess(result.isIsok());
			dto.setStatusCode("200");

			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("result", "保密信息");
			dto.setData(null);
			return mapper.toJson(dto);
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(checkIDcard.getError_code());
			dto.setMsg(checkIDcard.getReason());
			dto.setData(null);
			return mapper.toJson(dto);
		}
	}

	/**
	 * 身份证图片扫描接口
	 * 
	 * @param image
	 *            图片二进制Base64码
	 * @param configure
	 *            正反面值只可为（face/back）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/ocrIDcard")
	public String ocrIDcard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "image", required = false) String image,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		String accountId = accountSysDTO.getAccountId();
		long accountIdL = Long.parseLong(accountId);
		ArrayList<String> jsonList = null;
		try {
			jsonList = gson.fromJson(image, ArrayList.class);
		} catch (JsonSyntaxException e1) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setMsg("image" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
		}
		OcrIdCardDTO ocrIDcard = null;
		OcrIdCardDTO ocrIDcard2 = null;
		if (jsonList != null && jsonList.size() == 2) {
			ocrIDcard = OcrIDcardUtil.ocrIDcard(jsonList.get(0), "face");
			ocrIDcard2 = OcrIDcardUtil.ocrIDcard(jsonList.get(1), "back");
		}
		OcrIdCardDataDTO ocrIdCardDataDTO = null;
		OcrIdCardDataDTO ocrIdCardDataDTO2 = null;
		try {
			String dataValue = OcrIdCardDTOUtil.getDataValue(ocrIDcard);
			String dataValue2 = OcrIdCardDTOUtil.getDataValue(ocrIDcard2);
			ocrIdCardDataDTO = gson.fromJson(dataValue, OcrIdCardDataDTO.class);
			ocrIdCardDataDTO2 = gson.fromJson(dataValue2, OcrIdCardDataDTO.class);
			return accountClient.perfectIdentityInfo(accountIdL, ocrIdCardDataDTO.getName(), ocrIdCardDataDTO.getNum(),
					ocrIdCardDataDTO.getSex(), ocrIdCardDataDTO.getAddress(), ocrIdCardDataDTO.getBirth(),
					ocrIdCardDataDTO.getNationality(), ocrIdCardDataDTO2.getStart_date(),
					ocrIdCardDataDTO.getEnd_date(), ocrIdCardDataDTO.getIssue(), null, null, ticket, clientType);
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.oceidcard.500"));
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setSuccess(false);
			dto.setData(null);
			return mapper.toJson(dto);
		}
	}

	/**
	 * 银行卡二、三、四元素验证
	 * 
	 * @param request
	 * @param response
	 * @param bankcard
	 * @param Mobile
	 * @param cardNo
	 * @param realName
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/certificationBankCard")
	public String certificationBankCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "bankcard", required = false) String bankcard,
			@RequestParam(value = "Mobile", required = false) String Mobile,
			@RequestParam(value = "cardNo", required = false) String cardNo,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		String accountId = accountSysDTO.getAccountId();
		long accountIdL = Long.parseLong(accountId);
		String decryptbankcard = "";
		String decryptcardNo = "";
		String decryptMobile = "";
		String decryptrealName = "";
		try {
			if (StringUtils.isNotBlank(bankcard)) {
				decryptbankcard = DesUtil3.decryptThreeDESECB(bankcard, DesUtil3.KEY);
			}
			if (StringUtils.isNotBlank(cardNo)) {
				decryptcardNo = DesUtil3.decryptThreeDESECB(cardNo, DesUtil3.KEY);
			}
			if (StringUtils.isNotBlank(Mobile)) {
				decryptMobile = DesUtil3.decryptThreeDESECB(Mobile, DesUtil3.KEY);
			}
			if (StringUtils.isNotBlank(realName)) {
				decryptrealName = DesUtil3.decryptThreeDESECB(realName, DesUtil3.KEY);
			}
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			return mapper.toJson(dto);
		}
		BankCardDTO checkBankcard = BankCardUtil.checkBankcard(decryptbankcard, decryptMobile, decryptcardNo,
				decryptrealName);
		String error_code = checkBankcard.getError_code();
		BankCardResult result = checkBankcard.getResult();
		String ordersign = checkBankcard.getOrdersign();
		Integer isOk = 0;
		String bankName = null;
		if (error_code.equals("0")) {
			result.setBankcard(bankcard);
			dto = new ResponseDTO();
			dto.setMsg(checkBankcard.getReason());
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setSuccess(true);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("result", result);
			dto.setData(map);
			isOk = 1;
			bankName = result.getInformation().getBankname();
		} else {
			dto = new ResponseDTO();
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.code.124"));
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.124"));
			dto.setSuccess(false);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("result", result);
			dto.setData(map);
		}
		authenticationRecordService.insertAuthenticationRecord(accountIdL, null, null, null, bankcard, bankName, null,
				cardNo, realName, Mobile, DesUtil3.KEY, isOk, ordersign);
		return mapper.toJson(dto);
	}

	/**
	 * 中国银行实时汇率
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/exchangeRateChina")
	public String exchangeRateChina(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		ExchangeRateDTO exchangeRateChina = ExchangeRateUtil.exchangeRateChina(code);
		String showapi_res_code = exchangeRateChina.getShowapi_res_code();
		Map<String, Object> showapi_res_body = exchangeRateChina.getShowapi_res_body();
		if (showapi_res_code.equals("0")) {
			dto = new ResponseDTO();
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setSuccess(true);
			dto.setData(showapi_res_body);
		} else {
			dto = new ResponseDTO();
			dto.setStatusCode(exchangeRateChina.getShowapi_res_code());
			dto.setMsg(exchangeRateChina.getShowapi_res_error());
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}

	/**
	 * 汇率支持币种列表
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/currencyList2")
	public String currencyList2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		ExchangeRateDTO2 currencyList2 = ExchangeRateUtil2.currencyList2();
		String status = currencyList2.getStatus();
		String msg = currencyList2.getMsg();
		if (status.equals("0")) {
			dto = new ResponseDTO();
			dto.setSuccess(true);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
			Map<String, Object> map = new HashMap<>();
			map.put("result", currencyList2.getResult());
			dto.setData(map);
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(status);
			dto.setMsg(msg);
		}
		return mapper.toJson(dto);
	}

	/**
	 * 汇率转换
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param from
	 *            源币种
	 * @param amount
	 *            数量（金额）
	 * @param to
	 *            目标币种
	 * @return
	 */
	@PostMapping("/exchangeRate")
	public String exchangeRate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "amount", required = false) String amount,
			@RequestParam(value = "lowerlimit", required = false) String lowerlimit,
			@RequestParam(value = "to", required = false) String to) {
		AccountSysDTO accountSysDTO = null;
		ExchangeRateResponseDTO ratedto = new ExchangeRateResponseDTO();
		ResponseDTO dto = null;
		AccountCountryInfoDTO fromcountryDTO = null;
		AccountCountryInfoDTO tocountryDTO = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (StringUtils.isBlank(from)) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("from" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		} else {
			String fromcountryInfo = accountClient.getCountryInfo(from);
			fromcountryDTO = gson.fromJson(fromcountryInfo, AccountCountryInfoDTO.class);
			if(!fromcountryDTO.isSuccess()){
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("from" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			}
		}
		if (StringUtils.isBlank(amount)) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("amount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		} else {
			try {
				amount = DesUtil3.decryptThreeDESECB(amount, DesUtil3.KEY);
			} catch (Exception e) {
				e.printStackTrace();
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("amount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return mapper.toJson(dto);
			}
		}
		if(StringUtils.isNotBlank(lowerlimit)){
			try {
				lowerlimit = DesUtil3.decryptThreeDESECB(lowerlimit, DesUtil3.KEY);
				if(!(Double.parseDouble(amount) > Double.parseDouble(lowerlimit))){
					dto = new ResponseDTO();
					dto.setSuccess(false);
					dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.127"));
					dto.setMsg("amount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.127"));
					return mapper.toJson(dto);
				}
			} catch (Exception e) {
				e.printStackTrace();
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("lowerlimit" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return mapper.toJson(dto);
			}
		}
		
		
		if (StringUtils.isBlank(to)) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("to" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		} else {
			String tocountryInfo = accountClient.getCountryInfo(to);
			tocountryDTO = gson.fromJson(tocountryInfo, AccountCountryInfoDTO.class);
			if(!fromcountryDTO.isSuccess()){
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("to" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			}
		}
		RateInfoDTO exchangeRate = ExchangeRateUtil2.exchangeRate(from, amount, to);
		String status = exchangeRate.getStatus();
		String msg = exchangeRate.getMsg();
		if (status.equals("0")) {
			dto = new ResponseDTO();
			dto.setSuccess(true);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
			RateResult rateResult = exchangeRate.getResult();

			ratedto.setFromCountryId(fromcountryDTO.getCountryId());
			ratedto.setFromCurrencySign(fromcountryDTO.getCurrencySign());
			ratedto.setFromCountryFlag(fromcountryDTO.getCountryFlag());
			ratedto.setFromCountryName(fromcountryDTO.getCountryName());
			ratedto.setFromCurrencyName(fromcountryDTO.getCurrencyName());

			ratedto.setToCountryFlag(tocountryDTO.getCountryFlag());
			ratedto.setToCountryId(tocountryDTO.getCountryId());
			ratedto.setToCountryName(tocountryDTO.getCountryName());
			ratedto.setToCurrencyName(tocountryDTO.getCurrencyName());
			ratedto.setToCurrencySign(tocountryDTO.getCurrencySign());
			ratedto.setToCurrencySort(to);
			
			ratedto.setUpdateTime(rateResult.getUpdatetime());
			ratedto.setFromAmount(amount);
			ratedto.setFromCurrencySort(from);
			ratedto.setToAmount(rateResult.getCamount());
			ratedto.setRate(rateResult.getRate());
			Map<String,Object> map = new HashMap<>();
			map.put("result", ratedto);
			dto.setData(map);
			
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(status);
			dto.setMsg(msg);
		}
		return mapper.toJson(dto);
	}

	/**
	 * 单个币种汇率查询
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param currency
	 *            币种 如（CNY）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/currencysingle")
	public String currencysingle(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "currency", required = false) String currency) {

		AccountSysDTO accountSysDTO = null;
		ResponseDTO dto = null;
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(ticket)) {
			String accountByTicket = accountClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = gson.fromJson(accountByTicket, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return mapper.toJson(dto);
			}
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (clientType < 1 || clientType > 3) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		if (StringUtils.isBlank(currency)) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("currency" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return mapper.toJson(dto);
		}
		ExchangeRateDTO3 currencysingle = ExchangeRateUtil2.currencysingle(currency);
		String status = currencysingle.getStatus();
		String msg = currencysingle.getMsg();
		if (status.equals("0")) {
			dto = new ResponseDTO();
			dto.setSuccess(true);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
			Map<String, Object> map = new HashMap<>();
			CurrencyResult result = currencysingle.getResult();
			Map<String, CurrencyVlue> list = result.getList();
			String jsonStr = accountClient.getCountryFlagList();
			List<Map<String,Object>> jsonList = new ArrayList<>();
			List<CountryFlagDTO> flagList = new ArrayList<>();
			if(jsonStr == null){
				dto = new ResponseDTO();
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.203"));
				dto.setMsg("getCountryFlagList"+PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.203"));
				return mapper.toJson(dto);
			}else{
				jsonList = mapper.fromJson(jsonStr, List.class);
				for(Map<String,Object> map2 : jsonList){
					String json = mapper.toJson(map2);
					CountryFlagDTO flag = gson.fromJson(json, CountryFlagDTO.class);
					flagList.add(flag);
				}
			}
			List<CurrencyVlue> resultList = new ArrayList<>();
			for(Map.Entry<String,CurrencyVlue> entry : list.entrySet()){
				CurrencyVlue value = entry.getValue();
				String key = entry.getKey();
				for(CountryFlagDTO flag : flagList){
					if(key.equals(flag.getCurrencySort())){
						value.setCountryFlag(flag.getCountryFlag());
						value.setCurrencySort(key);
						value.setCurrencySign(flag.getCurrencySign());
						value.setLowerLimit("500.00");
						resultList.add(value);
					}
				}
//				resultList.add(value);
			}
			map.put("list", resultList);
			dto.setData(map);
		} else {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(status);
			dto.setMsg(msg);
		}
		return mapper.toJson(dto);
	}
}
