package com.lingdaoyi.cloud.service;

import java.util.ArrayList;
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
import com.lingdaoyi.cloud.dto.account.AccountCareerDTO;
import com.lingdaoyi.cloud.dto.account.AccountCareerExtDTO;
import com.lingdaoyi.cloud.entity.account.AccountCareer;
import com.lingdaoyi.cloud.mapper.AccountCareerMapper;

@Service
@Transactional
public class AccountCareerService {

	@Autowired
	private AccountCareerMapper accountCareerMapper;

	@Autowired
	private EmcService emcService;

	public ResponseDTO getAccountCareerList(String ticket, Integer clientType) {

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

		AccountCareerDTO accountCareerDTO = null;
		AccountCareerExtDTO accountCareerExtDTO = null;

		ArrayList<AccountCareerExtDTO> AccountCareerExtList = Lists.newArrayList();
		List<AccountCareer> list = accountCareerMapper.selectByparentId(0L);// 父级ID为0表示职业所属行业

		if (list != null && list.size() > 0) {
			for (AccountCareer accountCareer : list) {
				accountCareerExtDTO = new AccountCareerExtDTO();
				accountCareerExtDTO.setIndustry(accountCareer.getOccupationName());
				List<AccountCareer> careerList = accountCareerMapper.selectByparentId(accountCareer.getId());
				ArrayList<AccountCareerDTO> AccountCareerList = Lists.newArrayList();
				if (careerList != null && careerList.size() > 0) {
					for (AccountCareer accountCareer2 : careerList) {
						accountCareerDTO = new AccountCareerDTO();
						accountCareerDTO.setCareerId(accountCareer2.getId());
						accountCareerDTO.setOccupationName(accountCareer2.getOccupationName());
						AccountCareerList.add(accountCareerDTO);
					}
					accountCareerExtDTO.setList(AccountCareerList);
				}
				AccountCareerExtList.add(accountCareerExtDTO);
			}
		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("AccountCareerExt", AccountCareerExtList);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.findName.success"));
		return responseDTO;
	}

}
