package com.lingdaoyi.cloud.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.CurrencyExchangeRecordDTO;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.entity.CurrencyExchangeRecord;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.mapper.CurrencyExchangeRecordMapper;
import com.lingdaoyi.cloud.utils.JsonMapper;

@Service
@Transactional
public class CurrencyExchangeRecordService {

	@Autowired
	private AccountFeignClient accountFeignClient;

	@Autowired
	private CurrencyExchangeRecordMapper currencyExchangeRecordMapper;

	public ResponseDTO getExchangeRecordList(String ticket, Integer clientType, Integer pageNum, Integer pageSize) {

		ResponseDTO responseDTO = new ResponseDTO();
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

		
		PageHelper.startPage(pageNum, pageSize);
		Long accountId = Long.valueOf(accountSysDTO.getAccountId());

		CurrencyExchangeRecordDTO currencyExchangeRecordDTO = null;
		ArrayList<CurrencyExchangeRecordDTO> currencyExchangeRecordDTOList = Lists.newArrayList();
		List<CurrencyExchangeRecord> list = currencyExchangeRecordMapper.getExchangeRecordListByAccountId(accountId);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (list != null && list.size() > 0) {
			for (CurrencyExchangeRecord currencyExchangeRecord : list) {
				currencyExchangeRecordDTO = new CurrencyExchangeRecordDTO();
				currencyExchangeRecordDTO.setRecordId(currencyExchangeRecord.getId());
				currencyExchangeRecordDTO.setAccountId(currencyExchangeRecord.getAccountId());

				currencyExchangeRecordDTO.setAccountCurrencyName(currencyExchangeRecord.getAccountCurrencyName());
				currencyExchangeRecordDTO.setAccountCurrencySort(currencyExchangeRecord.getAccountCurrencySort());
				currencyExchangeRecordDTO.setAccountExchangeSum(currencyExchangeRecord.getAccountExchangeSum().toString());
				currencyExchangeRecordDTO.setAccountCurrencySign(currencyExchangeRecord.getAccountCurrencySign());
				currencyExchangeRecordDTO.setAccountFlagUrl(currencyExchangeRecord.getAccountFlagUrl());

				currencyExchangeRecordDTO.setExchangeCurrencyName(currencyExchangeRecord.getExchangeCurrencyName());
				currencyExchangeRecordDTO.setExchangeCurrencySort(currencyExchangeRecord.getExchangeCurrencySort());
				currencyExchangeRecordDTO.setExchangeCurrencySum(currencyExchangeRecord.getExchangeCurrencySum().toString());
				currencyExchangeRecordDTO.setExchangeCurrencySign(currencyExchangeRecord.getExchangeCurrencySign());
				currencyExchangeRecordDTO.setExchangeFlagUrl(currencyExchangeRecord.getExchangeFlagUrl());

				currencyExchangeRecordDTO.setExchangeRateRatio(currencyExchangeRecord.getExchangeRateRatio());
				String format = df.format(currencyExchangeRecord.getExchangeTime());
				currencyExchangeRecordDTO.setExchangeTime(format);
				currencyExchangeRecordDTOList.add(currencyExchangeRecordDTO);
			}
		}
		Map<String, Object> returnData = Maps.newHashMap();
		
		PageDTO<CurrencyExchangeRecordDTO> pageDTO = new PageDTO<CurrencyExchangeRecordDTO>(currencyExchangeRecordDTOList);
		pageDTO.setList(currencyExchangeRecordDTOList);
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.income.success"));
		return responseDTO;
	}

}
