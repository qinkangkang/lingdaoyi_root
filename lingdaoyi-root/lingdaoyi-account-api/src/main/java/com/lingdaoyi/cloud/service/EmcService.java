package com.lingdaoyi.cloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountTicket;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.mapper.AccountTicketMapper;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Component
@Transactional
public class EmcService {

	private static final Logger logger = LoggerFactory.getLogger(EmcService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountTicketMapper accountTicketMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public AccountSysDTO getAccountByTicket(String ticket, Integer clientType) {
		AccountSysDTO accountDTO = new AccountSysDTO();

		String ticketElement = null;
		try {
			ticketElement = RedisUtils.getValue(ticket, RedisMoudel.TicketToId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String accountId = null;
		if (ticketElement == null) {
			// 获取用户TICKET记录
			AccountTicket accountTicket = accountTicketMapper.findTicketAndType(ticket, clientType);
			if (accountTicket == null) {
				accountDTO.setEnable(false);
				accountDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.100"));
				accountDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.100"));
				return accountDTO;
			}
			RedisUtils.putCache(RedisMoudel.TicketToId, ticket, String.valueOf(accountTicket.getAccountId()));
			accountId = String.valueOf(accountTicket.getAccountId());
		} else {
			accountId = ticketElement.toString();
		}
		// 获取用户DTO缓存对象
		String AccountEentity = null;
		try {
			AccountEentity = RedisUtils.getValue(accountId, RedisMoudel.AccountEentity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (AccountEentity == null) {
			Account account = accountMapper.selectByPrimaryKey(Long.parseLong(accountId));
			if (account == null) {
				accountDTO.setEnable(false);
				accountDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.102"));
				accountDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.102"));
				return accountDTO;
			} else if (account.getStatus().equals(10)) {
				accountDTO.setEnable(false);
				accountDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.103"));
				accountDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.103"));
				return accountDTO;
			} else if (account.getStatus().equals(999)) {
				accountDTO.setEnable(false);
				accountDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.104"));
				accountDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.104"));
				return accountDTO;
			} else {
				// TODO用户基本信息存进缓存中
				accountDTO.setAccountId(accountId);
				accountDTO.setAccountName(account.getLoginName());
				accountDTO.setAccountRealName(account.getRealName());
				accountDTO.setAccountHeadUrl(account.getHeadUrl());
				accountDTO.setEnable(true);
				RedisUtils.putCache(RedisMoudel.AccountEentity, accountId, mapper.toJson(accountDTO));
			}
		} else {
			accountDTO = mapper.fromJson(AccountEentity, AccountSysDTO.class);
		}
		return accountDTO;
	}
}