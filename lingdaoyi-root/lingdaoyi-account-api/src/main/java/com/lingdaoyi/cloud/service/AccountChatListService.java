package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountChatListDTO;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountChatList;
import com.lingdaoyi.cloud.mapper.AccountChatListMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;

@Service
@Transactional
public class AccountChatListService {

	@Autowired
	private AccountChatListMapper accountChatListMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private EmcService emcService;

	public ResponseDTO getLatelyFriendsList(Integer clientType, String ticket, Integer pageNum, Integer pageSize) {

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

		PageHelper.startPage(pageNum, pageSize);
		Account account = null;
		AccountChatListDTO accountChatListDTO = null;

		List<AccountChatListDTO> accountChatListList = Lists.newArrayList();
		List<AccountChatList> list = accountChatListMapper
				.getLatelyFriendsList(Long.valueOf(accountSysDTO.getAccountId()));
		for (AccountChatList accountChat : list) {
			accountChatListDTO = new AccountChatListDTO();
			account = accountMapper.selectByPrimaryKey(accountChat.getToAccountId());
			if (account != null) {
				accountChatListDTO.setName(account.getRealName());
				accountChatListDTO.setHeadUrl(account.getHeadUrl());
				accountChatListDTO.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
				accountChatListDTO.setToAccountId(accountChat.getToAccountId());
				accountChatListDTO.setTransferAmount(accountChat.getTransferAmount());
				accountChatListDTO.setIsDeleted(accountChat.getIsDeleted());
				accountChatListDTO.setIsTransferAssistant(accountChat.getIsTransferAssistant());
				accountChatListDTO.setAssistantDesc(accountChat.getAssistantDesc());
				accountChatListDTO.setGmtCreate(DateFormatUtils.format(account.getGmtCreate(), "yyyy-MM-dd HH:mm"));
				accountChatListList.add(accountChatListDTO);
			}

		}

		Map<String, Object> returnData = Maps.newHashMap();
		PageDTO<AccountChatListDTO> pageDTO = new PageDTO<AccountChatListDTO>(accountChatListList);
		pageDTO.setList(accountChatListList);
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.chat.success"));
		return responseDTO;

	}

	public void addChatRecord(Long accountId, Long toAccountId, BigDecimal amounts, Integer type, Date transferDate) {
		AccountChatList accountChatList = new AccountChatList();
		try {
			accountChatList.setAccountId(accountId);
			accountChatList.setToAccountId(toAccountId);
			accountChatList.setIsDeleted(0);
			accountChatList.setTransferDate(transferDate);
			accountChatList.setType(type);
			accountChatList.setTransferAmount(amounts);
			accountChatList.setGmtCreate(new Date());
			accountChatList.setGmtModified(new Date());

			accountChatListMapper.insert(accountChatList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
