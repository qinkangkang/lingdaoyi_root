package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountCountryInfoDTO;
import com.lingdaoyi.cloud.dto.AccountDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.SponsorDTO2;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.AccountBalance;
import com.lingdaoyi.cloud.entity.CurrencyExchangeOrder;
import com.lingdaoyi.cloud.entity.CurrencyExchangeRecord;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.feign.SystemFeignClient;
import com.lingdaoyi.cloud.mapper.AccountBalanceMapper;
import com.lingdaoyi.cloud.mapper.CurrencyExchangeOrderMapper;
import com.lingdaoyi.cloud.mapper.CurrencyExchangeRecordMapper;
import com.lingdaoyi.cloud.utils.JsonMapper;
import com.lingdaoyi.cloud.utils.OddNumbers;

@Service
@Transactional
public class CurrencyExchangeOrderService {

	public static final Long timer = 259200000L;
	public static final String signStr = "ainiyiwannian";
	@Autowired
	private AccountFeignClient accountFeignClient;

	@Autowired
	private AccountBalanceMapper accountBalanceMapper;

	@Autowired
	private CurrencyExchangeOrderMapper currencyExchangeOrderMapper;

	@Autowired
	private SystemFeignClient systemFeignClient;

	@Autowired
	private CurrencyExchangeRecordMapper currencyExchangeRecordMapper;

	public ResponseDTO test(String addressIp) {
		ResponseDTO responseDTO = new ResponseDTO();

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("addressIp", "您的ip为" + addressIp + "服务当前运转正常,谢谢！");
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout"));

		return responseDTO;
	}

	public ResponseDTO createOrder(String ticket, Integer clientType, String fromCurrency, String fromAmount,
			String toCurrency, String toAmount, String rate, String exchangeTime, Long sponsorId, Long accountId,
			String fee) {
		ResponseDTO dto = new ResponseDTO();
		AccountSysDTO accountSysDTO = null;
		String frommoneyStr = null;
		String tomoneyStr = null;
		String rateStr = null;
		String feeStr = null;
		if (StringUtils.isBlank(ticket)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			// 根据ticket查询数据库,判断account是否可用
			String test = accountFeignClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = JsonMapper.fromJsonString(test, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return dto;
			}
		}
		if (clientType < 0 || clientType > 3 || sponsorId < 1 || accountId < 1) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType/sponsorId/accountId"
					+ PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			String accountById = accountFeignClient.getAccountById(accountId);
			Gson gson = new Gson();
			AccountDTO fromJson = gson.fromJson(accountById, AccountDTO.class);
			if (!fromJson.getCurrencySort().equals(fromCurrency)) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.currencysort.fail"));
				return dto;
			}

			String sponsorById = systemFeignClient.getSponsorById(sponsorId);
			SponsorDTO2 sponsor = gson.fromJson(sponsorById, SponsorDTO2.class);
			if (sponsor.getSponsorType() == null || sponsor.getSponsorType() <= 1) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.sponsor.fail"));
				return dto;
			}

		}
		if (StringUtils.isBlank(fromCurrency) || StringUtils.isBlank(toCurrency)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg(
					"fromCurrency/toCurrency" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}

		if (StringUtils.isBlank(fromAmount)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("fromAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			try {
				frommoneyStr = DesUtil3.decryptThreeDESECB(fromAmount, DesUtil3.KEY);
			} catch (Exception e) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("fromAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return dto;
			}
		}
		if (StringUtils.isBlank(toAmount)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("toAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			try {
				tomoneyStr = DesUtil3.decryptThreeDESECB(toAmount, DesUtil3.KEY);
			} catch (Exception e) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("toAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return dto;
			}
		}
		if (StringUtils.isBlank(rate)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("rate" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			try {
				rateStr = DesUtil3.decryptThreeDESECB(rate, DesUtil3.KEY);
			} catch (Exception e) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("rate" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return dto;
			}
		}
		if (StringUtils.isBlank(fee)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("fee" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			try {
				feeStr = DesUtil3.decryptThreeDESECB(fee, DesUtil3.KEY);
			} catch (Exception e) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setMsg("fee" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return dto;
			}
		}

		BigDecimal feeBig = new BigDecimal(feeStr);
		BigDecimal rateBig = new BigDecimal(rateStr);
		BigDecimal frommoneyBig = new BigDecimal(frommoneyStr);
		BigDecimal tomoneyBig = new BigDecimal(tomoneyStr);

		if (StringUtils.isBlank(exchangeTime)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("exchangeTime" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}

		AccountBalance accountBalanceById = accountBalanceMapper.selectByAccountId(accountId);
		if (accountBalanceById == null) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.balance.fail"));
			return dto;
		}

		BigDecimal oldbalance = accountBalanceById.getBalance();

		if (oldbalance.doubleValue() < frommoneyBig.doubleValue()) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.115"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.115"));
			return dto;
		}
		CurrencyExchangeOrder currencyExchangeOrder = new CurrencyExchangeOrder();

		currencyExchangeOrder.setAccountId(accountId);
		currencyExchangeOrder.setSponsorId(sponsorId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = new Date();
			System.out.println(date);
			Date parse = sdf.parse("2017-10-13 10:55:25");

			currencyExchangeOrder.setExchangeTime(parse);
		} catch (ParseException e) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setMsg("exchangeTime" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			return dto;
		}
		BigDecimal newbalance = oldbalance.subtract(frommoneyBig);
		accountBalanceById.setBalance(newbalance);
		accountBalanceMapper.updateByPrimaryKeySelective(accountBalanceById);
		currencyExchangeOrder.setFee(feeBig);
		currencyExchangeOrder.setRate(rateBig);
		currencyExchangeOrder.setFromCurrency(fromCurrency);
		currencyExchangeOrder.setFromAmount(frommoneyBig);
		currencyExchangeOrder.setToCurrency(toCurrency);
		currencyExchangeOrder.setToAmount(tomoneyBig);
		currencyExchangeOrder.setOrdernum(OddNumbers.getOrderNo());
		currencyExchangeOrder.setOrderType(1);
		currencyExchangeOrder.setOldBalance(oldbalance);
		currencyExchangeOrder.setNewBalance(newbalance);
		Date date = new Date();
		currencyExchangeOrder.setGmtCreate(date);
		currencyExchangeOrder.setGmtModified(date);
		currencyExchangeOrderMapper.insertSelective(currencyExchangeOrder);

		dto.setSuccess(true);
		dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
		return dto;
	}

	/**
	 * 每半小时查询一次，订单状态为1 的超过259200000L 毫秒（3天）设置为作废状态
	 * 
	 * @return
	 */
	public ResponseDTO updateOrderType(String sign) {
		ResponseDTO dto = new ResponseDTO();
		if (StringUtils.isBlank(sign)) {
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			dto.setSuccess(false);
			return dto;
		}
		try {
			sign = DesUtil3.decryptThreeDESECB(sign, DesUtil3.KEY);
			if (!sign.equals(signStr)) {
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				dto.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				dto.setSuccess(false);
				return dto;
			}
		} catch (Exception e) {
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			dto.setSuccess(false);
			return dto;
		}
		List<CurrencyExchangeOrder> selectAll = currencyExchangeOrderMapper.selectAll();
		for (CurrencyExchangeOrder ceo : selectAll) {
			if (ceo.getOrderType() == 1) {
				Date exchangeTime = ceo.getExchangeTime();
				Date nowDate = new Date();
				long nowTime = nowDate.getTime();
				BigDecimal nowBig = new BigDecimal(nowTime);
				long time = exchangeTime.getTime();
				BigDecimal timeBig = new BigDecimal(time);
				BigDecimal subtract = nowBig.subtract(timeBig);
				if (subtract.longValue() >= timer) {
					ceo.setOrderType(3);
					currencyExchangeOrderMapper.updateByPrimaryKeySelective(ceo);
				}
			}
		}
		return dto;
	}

	public ResponseDTO insertRecord(String sign, Long currencyExchangeOrderId, Integer clientType, String addressIP) {
		ResponseDTO dto = new ResponseDTO();
		Gson gson = new Gson();
		if (StringUtils.isBlank(sign)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		} else {
			// 加密参数验证
			String signStr = null;
			StringBuffer sb = new StringBuffer();
			sb.append(clientType).append(addressIP);
			try {
				signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!signStr.equals(sign)) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
				return dto;
			}
		}

		if (!(currencyExchangeOrderId > 0)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.select.currencyexchangeorder.fail"));
		}
		CurrencyExchangeOrder currencyExchangeOrder = currencyExchangeOrderMapper
				.selectByPrimaryKey(currencyExchangeOrderId);
		Integer orderType = currencyExchangeOrder.getOrderType();
		if (orderType != 0 && orderType > 1) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.select.currencyexchangeorder.del"));
		}
		currencyExchangeOrder.setOrderType(2);
		currencyExchangeOrderMapper.updateByPrimaryKeySelective(currencyExchangeOrder);
		String fromCurrency = currencyExchangeOrder.getFromCurrency();
		String toCurrency = currencyExchangeOrder.getToCurrency();
		CurrencyExchangeRecord record = new CurrencyExchangeRecord();
		
		String countryInfo = accountFeignClient.getCountryInfo(fromCurrency);
		AccountCountryInfoDTO countryInfoDTO = gson.fromJson(countryInfo, AccountCountryInfoDTO.class);
		record.setAccountCurrencyName(countryInfoDTO.getCurrencyName());
		record.setAccountCurrencySign(countryInfoDTO.getCurrencySign());
		record.setAccountCurrencySort(countryInfoDTO.getCurrencySort());
		record.setAccountFlagUrl(countryInfoDTO.getCountryFlag());
		record.setAccountExchangeSum(currencyExchangeOrder.getFromAmount());
		record.setAccountId(currencyExchangeOrder.getAccountId());
		
		String countryInfo2 = accountFeignClient.getCountryInfo(toCurrency);
		AccountCountryInfoDTO countryInfoDTO2 = gson.fromJson(countryInfo2, AccountCountryInfoDTO.class);
		record.setExchangeCurrencyName(countryInfoDTO2.getCurrencyName());
		record.setExchangeCurrencySign(countryInfoDTO2.getCurrencySign());
		record.setExchangeCurrencySort(countryInfoDTO2.getCurrencySort());
		record.setExchangeFlagUrl(countryInfoDTO2.getCountryFlag());
		record.setExchangeCurrencySum(currencyExchangeOrder.getToAmount());
		record.setExchangeTime(currencyExchangeOrder.getExchangeTime());
		record.setExchangeRateRatio("1:" + currencyExchangeOrder.getRate().toString());
		record.setIsDeleted(0);
		
		Date date = new Date();
		record.setGmtCreate(date);
		record.setGmtModified(date);
		currencyExchangeRecordMapper.insertSelective(record);

		dto.setSuccess(true);
		dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
		return dto;
	}

	public ResponseDTO getCurrencyExchangeOrderById(String ticket, Integer clientType, Long currencyExchangeOrderId) {
		ResponseDTO dto = new ResponseDTO();
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isBlank(ticket)) {
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			dto.setSuccess(false);
			return dto;
		} else {
			// 根据ticket查询数据库,判断account是否可用
			String test = accountFeignClient.getAccountByTicket(ticket, clientType);
			accountSysDTO = JsonMapper.fromJsonString(test, AccountSysDTO.class);
			if (!accountSysDTO.isEnable()) {
				dto.setSuccess(false);
				dto.setStatusCode(accountSysDTO.getStatusCode());
				dto.setMsg(accountSysDTO.getMsg());
				return dto;
			}

		}
		if (clientType < 1 || clientType > 3) {
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			dto.setSuccess(false);
			return dto;
		}
		CurrencyExchangeOrder selectByPrimaryKey = currencyExchangeOrderMapper
				.selectByPrimaryKey(currencyExchangeOrderId);
		if (selectByPrimaryKey != null
				&& selectByPrimaryKey.getAccountId() == Long.parseLong(accountSysDTO.getAccountId())) {
			dto.setSuccess(true);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
			Map<String, Object> map = new HashMap<>();
			map.put("result", selectByPrimaryKey);
			dto.setData(map);
		} else {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.select.currencyexchangeorder.fail"));
		}
		return dto;
	}
}
