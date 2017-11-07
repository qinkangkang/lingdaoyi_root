package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountDTO;
import com.lingdaoyi.cloud.dto.AccountExchangerateDTO;
import com.lingdaoyi.cloud.dto.AccountRecordDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.AccountExchangerate;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.feign.AuthenticateFeignClient;
import com.lingdaoyi.cloud.mapper.AccountExchangerateMapper;
import com.lingdaoyi.cloud.utils.JsonMapper;
import com.lingdaoyi.cloud.utils.date.DateUtils;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Transactional
@Service
public class AccountExchangeService {
	Map<String, Object> returnData = Maps.newHashMap();

	@Autowired
	private AccountExchangerateMapper exchangeMapper;

	@Autowired
	private AccountFeignClient accountFeignClient;

	@Autowired
	private AuthenticateFeignClient authenticateFeignClient;

	public ResponseDTO exchangeMoney(String ticket, Integer clientType, String transferAmount, Long receivedAccountId,
			String addressIP, String sign) {

		AccountExchangerateDTO exchageDTO = new AccountExchangerateDTO();
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(transferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("transferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String test = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(test, AccountSysDTO.class);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		// transferAmount解密
		String des3TransferAmount = null;
		try {
			des3TransferAmount = DesUtil3.decryptThreeDESECB(transferAmount, DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(des3TransferAmount).append(receivedAccountId);
		try {
			signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}
		// 根据ticket获取当前账户的币种
		String accountJson = accountFeignClient.getAccountById(receivedAccountId);
		AccountDTO currentAccountDTO = JsonMapper.fromJsonString(accountJson, AccountDTO.class);
		String orgCurrencyName = currentAccountDTO.getCurrencyName();

		// 根据ID查询收钱人的币种的缩写
		String receviceAccountJson = accountFeignClient.getAccountById(receivedAccountId);
		AccountDTO accountDTO = JsonMapper.fromJsonString(receviceAccountJson, AccountDTO.class);
		if (accountDTO == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.113"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.113"));
			return responseDTO;
		}
		String transCurrencyName = accountDTO.getCurrencyName();// 获得货币名称

		BigDecimal exchangeRateValue = null;

		exchageDTO.setOrgCurrency(orgCurrencyName);
		exchageDTO.setTransCurrency(transCurrencyName);

		// 判断下要币种是否一致,一致可以进行转账,不一致,判断是否是CAD转成CNY
		// 目前设定只支持CAD装成CNY,以及相同币种之间的转账;

		if (orgCurrencyName.equals(transCurrencyName)) {
			exchangeRateValue = new BigDecimal("1");
			exchageDTO.setGmtCreate(DateUtils.getCurrentTime());
		} else {

			if (!orgCurrencyName.equals("加元") || !transCurrencyName.equals("人民币")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.113"));
				responseDTO.setMsg("目前只支持“加元->人民币”间的转换");
				return responseDTO;
			}
			// 当前实时汇率---调用强哥的接口

			// xxxxxxx-假设
			exchangeRateValue = new BigDecimal("0.149");
			// 获取汇率接口的时间
			// xxxxxxxxx-目前假设
			exchageDTO.setGmtCreate(DateUtils.getCurrentTime());
		}
		exchageDTO.setOrgMoney(new BigDecimal(des3TransferAmount));
		exchageDTO.setTransferMoney(new BigDecimal(des3TransferAmount).subtract(exchangeRateValue));
		returnData.put("exchageDTO", exchageDTO);

		responseDTO.setSuccess(true);
		responseDTO.setData(returnData);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc_code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.exchange.success"));
		return responseDTO;
	}

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

}
