package com.lingdaoyi.cloud.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountBalanceDTO;
import com.lingdaoyi.cloud.dto.account.AccountCareerDTO;
import com.lingdaoyi.cloud.dto.account.AccountDTO;
import com.lingdaoyi.cloud.dto.account.AccountFriendInfoDTO;
import com.lingdaoyi.cloud.dto.account.AccountIncomeDTO;
import com.lingdaoyi.cloud.dto.account.AccountInfoExtDTO;
import com.lingdaoyi.cloud.dto.account.AccountViewDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.encrypt.MD5Util2;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountCountry;
import com.lingdaoyi.cloud.entity.account.AccountInfo;
import com.lingdaoyi.cloud.feign.SystemFeignClient;
import com.lingdaoyi.cloud.feign.TransferFeignClient;
import com.lingdaoyi.cloud.mapper.AccountCountryMapper;
import com.lingdaoyi.cloud.mapper.AccountFriendMapper;
import com.lingdaoyi.cloud.mapper.AccountInfoMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;

@Service
@Transactional
public class AccountService {

	public static final long Control = 60L;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private EmcService emcService;

	@Autowired
	private TransferFeignClient transferFeignClient;

	@Autowired
	private AccountCountryMapper accountCountryMapper;

	@Autowired
	private SystemFeignClient systemFeignClient;

	@Autowired
	private AccountFriendMapper accountFriendMapper;

	public ResponseDTO updateAccount(String loginName, String headUrl, Integer isAuth, String email,
			Integer accountLevel, Integer fingerprintPay, Integer status, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(loginName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginName" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		AccountDTO accountDTO = null;

		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		if (StringUtils.isNotBlank(headUrl)) {
			account.setHeadUrl(headUrl);
		}
		if (isAuth != null) {
			if (StringUtils.isNotBlank(isAuth.toString())) {
				account.setIsAuth(isAuth);
			}
		}
		if (StringUtils.isNotBlank(headUrl)) {
			account.setHeadUrl(headUrl);
		}
		if (StringUtils.isNotBlank(email)) {
			account.setEmail(email);
		}
		if (accountLevel != null) {
			if (StringUtils.isNotBlank(accountLevel.toString())) {
				account.setAccountLevel(accountLevel);
			}
		}
		if (fingerprintPay != null) {
			if (StringUtils.isNotBlank(fingerprintPay.toString())) {
				account.setFingerprintPay(fingerprintPay);
			}
		}
		if (status != null) {
			if (StringUtils.isNotBlank(status.toString())) {
				account.setStatus(status);
			}
		}
		account.setGmtModified(new Date());

		accountMapper.updateByPrimaryKey(account);
		accountDTO = new AccountDTO();
		accountDTO.setAccountId(account.getId());
		accountDTO.setAccountLevel(account.getAccountLevel());
		accountDTO.setCountryName(account.getCountryName());
		accountDTO.setEmail(account.getEmail());
		if (account.getIsAuth() == 0) {
			accountDTO.setAuth(false);
		} else {
			accountDTO.setAuth(true);
		}
		accountDTO.setLoginName(account.getLoginName());
		accountDTO.setRealName(account.getRealName());

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("account", accountDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.success"));
		return responseDTO;

	}

	public AccountDTO getAccountById(Long id) {
		Account account = null;
		AccountDTO accountDTO = null;
		try {
			if (id != null) {
				account = accountMapper.selectByPrimaryKey(id);
				AccountCountry accountCountry = accountCountryMapper.findByAccountCountryId(account.getCountryId());
				accountDTO = new AccountDTO();
				accountDTO.setAccountId(account.getId());
				accountDTO.setAccountLevel(account.getAccountLevel());
				accountDTO.setCountryName(account.getCountryName());
				accountDTO.setCurrencyName(account.getCurrencyName());
				accountDTO.setHeadUrl(account.getHeadUrl());
				accountDTO.setEmail(account.getEmail());
				accountDTO.setPayPassword(account.getPayPassword());
				accountDTO.setCurrencySort(accountCountry.getCurrencysort());
				accountDTO.setCountryName(accountCountry.getCountryname());
				accountDTO.setCurrencyName(accountCountry.getCurrencyname());
				if (account.getIsAuth() == 0) {
					accountDTO.setAuth(false);
				} else {
					accountDTO.setAuth(true);
				}
				accountDTO.setLoginName(account.getLoginName());
				accountDTO.setRealName(account.getRealName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountDTO;
	}

	public ResponseDTO findAccountName(Integer clientType, String ticket) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		Account account = null;
		AccountDTO accountDTO = null;

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);

		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		if (accountSysDTO.getAccountId() != null) {
			account = accountMapper.selectByPrimaryKey(accountSysDTO.getAccountId());
			accountDTO = new AccountDTO();
			accountDTO.setAccountId(account.getId());
			accountDTO.setAccountLevel(account.getAccountLevel());
			accountDTO.setCountryName(account.getCountryName());
			accountDTO.setEmail(account.getEmail());
			if (account.getIsAuth() == 0) {
				accountDTO.setAuth(false);
			} else {
				accountDTO.setAuth(true);
			}
			accountDTO.setLoginName(account.getLoginName());
			accountDTO.setRealName(account.getRealName());
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountInfo", accountDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.findName.success"));
		return responseDTO;
	}

	public ResponseDTO updateAccountLoginPwd(Integer clientType, String loginPassword, String ticket, String addressIP,
			String sign) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(loginPassword)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(loginPassword).append(ticket);
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

		// 加密密码
		String enPassword = null;
		try {
			enPassword = MD5Util2.md5Encode(loginPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		if (account != null) {
			if (account.getLoginPassword() != null) {
				if (account.getLoginPassword().equals(loginPassword)) {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.123"));
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.123"));
					return responseDTO;
				}
			}
			account.setLoginPassword(enPassword);
			account.setGmtModified(new Date());
			accountMapper.updateByPrimaryKey(account);

		}

		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg("修改登录密码成功！");
		return responseDTO;
	}

	public ResponseDTO updateAccountPayPwd(Integer clientType, String payPassword, String ticket, String addressIP,
			String sign) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(payPassword)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("payPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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
		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(payPassword).append(ticket);
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

		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		if (account != null) {

			if (account.getPayPassword() != null) {
				if (account.getPayPassword().equals(payPassword)) {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.123"));
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.123"));
					return responseDTO;
				}
			}
			account.setPayPassword(payPassword);
			account.setGmtModified(new Date());
			accountMapper.updateByPrimaryKeySelective(account);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg("修改支付密码成功");
		return responseDTO;
	}

	public void updateAccountAuth(Long id, Integer isAuth) {
		Account account = accountMapper.selectByPrimaryKey(id);
		account.setIsAuth(isAuth);
		accountMapper.updateByPrimaryKey(account);
	}

	public ResponseDTO getAccountByPhone(String loginName, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(loginName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginName" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		Account account = accountMapper.findByLoginName(loginName);
		AccountDTO accountDTO = null;
		if (account != null) {
			Long countryId = account.getCountryId();
			AccountCountry accountCountry = accountCountryMapper.findByAccountCountryId(countryId);
			accountDTO = new AccountDTO();
			accountDTO.setAccountId(account.getId());
			accountDTO.setHeadUrl(account.getHeadUrl());
			accountDTO.setCountryId(countryId);
			accountDTO.setLoginName(account.getLoginName());
			accountDTO.setRealName(account.getRealName());
			if (accountCountry != null) {
				accountDTO.setCurrencySort(accountCountry.getCurrencysort());
			}
		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("accountInfo", accountDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.success"));
		return responseDTO;
	}

	public ResponseDTO viewAccount(String ticket, Integer clientType) {
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

		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		AccountInfo accountInfo = accountInfoMapper.findByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		AccountViewDTO accountViewDTO = new AccountViewDTO();
		if (account != null) {
			AccountCountry accountCountry = accountCountryMapper.findByAccountCountryId(account.getCountryId());
			accountViewDTO.setAccountId(account.getId());
			accountViewDTO.setLoginName(account.getLoginName());
			accountViewDTO.setHeadUrl(account.getHeadUrl());
			accountViewDTO.setCountryId(account.getCountryId());
			accountViewDTO.setCountryName(account.getCountryName());
			accountViewDTO.setCurrencyName(account.getCurrencyName());
			accountViewDTO.setTicket(ticket);
			if (accountCountry != null) {
				accountViewDTO.setCurrencySort(accountCountry.getCurrencysort());
				accountViewDTO.setCurrencysign(accountCountry.getCurrencysign());
				accountViewDTO.setCountryFlag(accountCountry.getCountryFlag());
			}
			if (account.getIsAuth() == 0) {
				accountViewDTO.setAuth(false);
			} else {
				accountViewDTO.setAuth(true);
			}
			if (account.getPayPassword() == null) {
				accountViewDTO.setisSetPayPwd(false);
			} else {
				accountViewDTO.setisSetPayPwd(true);
			}

			try {
				String balance = transferFeignClient.getBalance(Long.valueOf(accountSysDTO.getAccountId()));
				AccountBalanceDTO accountBalanceDTO = JsonMapper.fromJsonString(balance, AccountBalanceDTO.class);
				accountViewDTO.setBalance(accountBalanceDTO.getBalance());
			} catch (NumberFormatException e) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.203"));
				responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.203"));
				return responseDTO;
			}
		}

		if (accountInfo != null) {

			accountViewDTO.setNikeName(accountInfo.getNickname());
			accountViewDTO.setSex(accountInfo.getGender());
			accountViewDTO.setAge(accountInfo.getAge());
			accountViewDTO.setCareer(accountInfo.getCareer());
			accountViewDTO.setIncome(accountInfo.getIncome());

		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("account", accountViewDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.success"));
		return responseDTO;
	}

	public String getAuthStatus(Long accountId) {
		Account account = accountMapper.selectByPrimaryKey(accountId);
		Integer isAuth = account.getIsAuth();
		return isAuth.toString();
	}

	public ResponseDTO getIncomeList(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		Map<Integer, String> all = DictionaryUtil.getStatueMap(DictionaryUtil.Income);
		ArrayList<AccountIncomeDTO> AccountIncomeList = Lists.newArrayList();
		AccountIncomeDTO accountIncomeDTO = null;
		for (Map.Entry<Integer, String> entry : all.entrySet()) {
			accountIncomeDTO = new AccountIncomeDTO();
			accountIncomeDTO.setValue(entry.getKey());
			accountIncomeDTO.setIncome(entry.getValue());
			AccountIncomeList.add(accountIncomeDTO);
		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("incomeList", AccountIncomeList);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.income.success"));
		return responseDTO;
	}

	public ResponseDTO viewAccountInfo(String ticket, Integer clientType) {

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

		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		AccountInfo accountInfo = accountInfoMapper.findByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
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
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.success"));
		return responseDTO;

	}

	public ResponseDTO updateAccountHead(String type, String file, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);

		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		Account account = null;
		AccountInfo accountInfo = null;
		String headUrl = null;
		try {
			String imageResponseDTO = systemFeignClient.imageUploadDG(type, file);
			ResponseDTO iResponseDTO = JsonMapper.fromJsonString(imageResponseDTO, ResponseDTO.class);
			Map<String, Object> data = iResponseDTO.getData();
			if (data != null) {
				headUrl = (String) data.get("relativePath");
			}
		} catch (Exception e) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.203"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.203"));
			return responseDTO;
		}

		if (headUrl != null) {
			account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
			accountInfo = accountInfoMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
			account.setHeadUrl(headUrl);
			accountInfo.setHeadUrl(headUrl);

			accountMapper.updateByPrimaryKeySelective(account);
			accountInfoMapper.updateByPrimaryKey(accountInfo);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("relativePath", headUrl);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.account.head.success"));
		return responseDTO;
	}

}
