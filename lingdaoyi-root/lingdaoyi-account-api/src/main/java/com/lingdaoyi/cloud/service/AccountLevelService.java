package com.lingdaoyi.cloud.service;

import java.util.List;
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
import com.lingdaoyi.cloud.dto.account.AccountLevelDTO;
import com.lingdaoyi.cloud.dto.account.AccountLevelExtDTO;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountLevel;
import com.lingdaoyi.cloud.mapper.AccountLevelMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class AccountLevelService {

	@Autowired
	private AccountLevelMapper accountLevelMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private EmcService emcService;

	public ResponseDTO getAccountlevel(String ticket, Integer clientType) {

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

		// 根据ticket查询数据库,判断account是否可用
		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		AccountLevelDTO accountLevelDTO = null;
		AccountLevelExtDTO accountLevelExtDTO = null;

		try {
			Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
			accountLevelExtDTO = new AccountLevelExtDTO();
			accountLevelExtDTO.setCurrentLevel(account.getAccountLevel());

			List<AccountLevel> list = accountLevelMapper.selectAll();
			List<AccountLevelDTO> accountLevelList = Lists.newArrayList();
			if (list != null && list.size() > 0) {
				for (AccountLevel accountLevel : list) {
					accountLevelDTO = new AccountLevelDTO();
					accountLevelDTO.setId(accountLevel.getId());
					accountLevelDTO.setLevelCode(accountLevel.getLevelCode());
					accountLevelDTO.setName(accountLevel.getName());
					accountLevelDTO.setTransferQuota(accountLevel.getTransferQuota());
					accountLevelList.add(accountLevelDTO);
				}
			}
			accountLevelExtDTO.setList(accountLevelList);

		} catch (Exception e) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg("系统错误");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("accountLevel", accountLevelExtDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.level.success"));
		return responseDTO;

	}

}
