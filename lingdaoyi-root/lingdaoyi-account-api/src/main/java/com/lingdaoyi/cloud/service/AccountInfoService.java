package com.lingdaoyi.cloud.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountInfoDTO;
import com.lingdaoyi.cloud.dto.account.AccountInfoExtDTO;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountInfo;
import com.lingdaoyi.cloud.mapper.AccountInfoMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class AccountInfoService {

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private AccountMapper accountMapper;
	
	@Autowired
	private EmcService emcService;

	public ResponseDTO updateAccountInfo(Long id, String loginName, String headUrl, String nickname, String countryName,
			String gender, String area, String signature, Integer age, String income, String deviceTokens,
			Integer clientType) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (id != null) {
			if (StringUtils.isBlank(id.toString())) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				responseDTO.setMsg("id" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				return responseDTO;
			}
		}

		AccountInfo accountInfo = null;
		AccountInfoDTO accountInfoDTO = null;

		if (StringUtils.isNotBlank(id.toString())) {
			accountInfo = accountInfoMapper.selectByPrimaryKey(id);
			if (StringUtils.isNotBlank(headUrl)) {
				accountInfo.setHeadUrl(headUrl);
			}
			if (StringUtils.isNotBlank(nickname)) {
				accountInfo.setNickname(nickname);
			}
			if (StringUtils.isNotBlank(countryName)) {
				accountInfo.setCountryName(countryName);
			}
			if (StringUtils.isNotBlank(gender)) {
				accountInfo.setGender(gender);
			}
			if (StringUtils.isNotBlank(area)) {
				accountInfo.setArea(area);
			}
			if (StringUtils.isNotBlank(signature)) {
				accountInfo.setSignature(signature);
			}
			if (age != null) {
				if (StringUtils.isNotBlank(age.toString())) {
					accountInfo.setAge(age);
				}
			}
			if (StringUtils.isNotBlank(income)) {
				accountInfo.setIncome(income);
			}
			if (StringUtils.isNotBlank(deviceTokens)) {
				accountInfo.setDeviceTokens(deviceTokens);
			}
			accountInfo.setGmtModified(new Date());

			accountInfoMapper.updateByPrimaryKey(accountInfo);
			accountInfoDTO = new AccountInfoDTO();
			accountInfoDTO.setId(accountInfo.getId());
			accountInfoDTO.setAccountId(accountInfo.getAccountId());
			accountInfoDTO.setLoginName(accountInfo.getLoginName());
			accountInfoDTO.setHeadUrl(accountInfo.getHeadUrl());
			accountInfoDTO.setQrCode(accountInfo.getQrCode());
			accountInfoDTO.setNickname(accountInfo.getNickname());
			accountInfoDTO.setCountryName(accountInfo.getCountryName());
			accountInfoDTO.setGender(accountInfo.getGender());
			accountInfoDTO.setCareer(accountInfo.getCareer());
			accountInfoDTO.setIncome(accountInfo.getIncome());
		}
		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("accountInfo", accountInfoDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.success"));
		return responseDTO;
	}

	public ResponseDTO updateAccountInfo(String nickname, String income, String career, String ticket,
			Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		AccountInfo accountInfo = accountInfoMapper.findByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		AccountInfoExtDTO accountInfoExtDTO = null;
		if (account != null) {
			accountInfoExtDTO = new AccountInfoExtDTO();
			accountInfoExtDTO.setAccountId(account.getId());
			accountInfoExtDTO.setCountryName(account.getCountryName());
			accountInfoExtDTO.setRealName(account.getRealName());
			accountInfoExtDTO.setLoginName(account.getLoginName());
			if (account.getIsAuth() == 0) {
				accountInfoExtDTO.setAuth(false);
			} else {
				accountInfoExtDTO.setAuth(true);
			}
		}
		if (accountInfo != null) {

			if (StringUtils.isNotBlank(career)) {
				accountInfo.setCareer(career);
			}
			if (StringUtils.isNotBlank(income)) {
				accountInfo.setIncome(income);
			}
			if (StringUtils.isNotBlank(nickname)) {
				accountInfo.setNickname(nickname);
			}
			accountInfoMapper.updateByPrimaryKeySelective(accountInfo);
			accountInfoExtDTO.setAge(accountInfo.getAge());
			accountInfoExtDTO.setCareer(accountInfo.getCareer());
			accountInfoExtDTO.setGender(accountInfo.getGender());
			accountInfoExtDTO.setIncome(accountInfo.getIncome());
			accountInfoExtDTO.setQrCode(accountInfo.getQrCode());
			accountInfoExtDTO.setNickname(accountInfo.getNickname());
			accountInfoExtDTO.setHeadUrl(accountInfo.getHeadUrl());
		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountInfo", accountInfoExtDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.success"));
		return responseDTO;
	}

}
