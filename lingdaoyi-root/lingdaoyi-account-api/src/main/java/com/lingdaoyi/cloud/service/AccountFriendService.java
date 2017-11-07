package com.lingdaoyi.cloud.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountDTO;
import com.lingdaoyi.cloud.dto.account.AccountFriendInfoDTO;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountFriend;
import com.lingdaoyi.cloud.mapper.AccountFriendMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;

@Service
@Transactional
public class AccountFriendService {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountFriendMapper accountFriendMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private EmcService emcService;

	public ResponseDTO getFriendList(Integer clientType, String ticket) {

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

		Account account = null;
		AccountFriendInfoDTO accountFriendInfoDTO = null;

		List<AccountFriendInfoDTO> accountList = Lists.newArrayList();
		List<AccountFriend> list = accountFriendMapper.getFriendList(Long.valueOf(accountSysDTO.getAccountId()));
		for (AccountFriend accountFriend : list) {
			accountFriendInfoDTO = new AccountFriendInfoDTO();
			account = accountMapper.selectByPrimaryKey(accountFriend.getToAccountId());
			if (account != null) {
				Account account2 = accountMapper.selectByPrimaryKey(account.getId().longValue());
				accountFriendInfoDTO.setAccountId(account.getId());
				accountFriendInfoDTO.setLoginName(account.getLoginName());
				accountFriendInfoDTO.setRealName(account.getRealName());
				accountFriendInfoDTO.setFriends(true);
				if (account2 != null) {
					if (account2.getIsAuth() == 0) {
						accountFriendInfoDTO.setAuth(true);
					} else {
						accountFriendInfoDTO.setAuth(false);
					}
				}
			}
			accountList.add(accountFriendInfoDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("friendList", accountList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.person.success"));
		return responseDTO;

	}

	public ResponseDTO addFriends(Integer clientType, Long toAccountId, String ticket) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (toAccountId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("toAccountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

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

		Account account = accountMapper.selectByPrimaryKey(toAccountId);
		if (account == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.110"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.110"));
			return responseDTO;
		}

		AccountFriend accountFriend = new AccountFriend();
		accountFriend.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		accountFriend.setToAccountId(toAccountId);
		accountFriend.setGmtCreate(new Date());
		accountFriend.setIsDeleted(0);
		accountFriendMapper.insert(accountFriend);

		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.add.friend.success"));
		return responseDTO;
	}

	public ResponseDTO getAccountExistence(Integer clientType, String toAccountIdList, String ticket) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(toAccountIdList)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("toAccountIdList" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

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

		Account account = null;
		AccountFriendInfoDTO accountFriendInfoDTO = null;

		// 解析前端传入的json串
		JavaType jt = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> accountIdList = new ArrayList<String>();
		accountIdList = mapper.fromJson(toAccountIdList, jt);

		List<AccountFriendInfoDTO> accountFriendInfoList = Lists.newArrayList();
		List<AccountFriend> friendList = accountFriendMapper.getFriendList(Long.valueOf(accountSysDTO.getAccountId()));

		for (String phone : accountIdList) {
			String replace = null;
			if (phone != null && phone != "") {
				replace = phone.replace("-", "");
			}
			accountFriendInfoDTO = new AccountFriendInfoDTO();
			account = accountMapper.findByLoginName(replace);
			if (account != null) {
				for (AccountFriend accountFriend : friendList) {
					if (accountFriend.getToAccountId() == account.getId()) {
						accountFriendInfoDTO.setFriends(true);
					} else {
						accountFriendInfoDTO.setFriends(false);
					}
				}
				accountFriendInfoDTO.setAccountId(account.getId());
				accountFriendInfoDTO.setHeadUrl(account.getHeadUrl());
				accountFriendInfoDTO.setLoginName(account.getLoginName());
				accountFriendInfoDTO.setRealName(account.getRealName());
				if (account.getIsAuth() == 1) {
					accountFriendInfoDTO.setAuth(true);
				} else {
					accountFriendInfoDTO.setAuth(false);
				}

				accountFriendInfoList.add(accountFriendInfoDTO);
			}

		}

		Map<String, Object> returnData = Maps.newHashMap();
		responseDTO.setData(returnData);
		returnData.put("accountfriendList", accountFriendInfoList);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.existence.success"));
		return responseDTO;
	}

	public ResponseDTO findAccountPhone(Integer clientType, String phone, String ticket) {
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
		AccountFriend accountFriend = null;
		AccountFriendInfoDTO accountFriendInfoDTO = null;

		account = accountMapper.findByLoginName(phone);
		if (account == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.126"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.126"));
			return responseDTO;
		} else {
			accountFriendInfoDTO = new AccountFriendInfoDTO();
			AccountFriend accountFriend2 = accountFriendMapper.findByAccountIdAndFriendId(
					Long.valueOf(accountSysDTO.getAccountId()), account.getId().longValue());
			if (accountFriend2 != null) {
				accountFriendInfoDTO.setFriends(true);
			}
			accountFriendInfoDTO.setAccountId(account.getId());
			accountFriendInfoDTO.setLoginName(account.getLoginName());
			accountFriendInfoDTO.setRealName(account.getRealName());
			if (account.getIsAuth() == 0) {
				accountFriendInfoDTO.setAuth(false);
			} else {
				accountFriendInfoDTO.setAuth(true);
			}
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountFriendInfo", accountFriendInfoDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.findName.success"));
		return responseDTO;
	}

}
