package com.lingdaoyi.cloud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountCountryInfoDTO;
import com.lingdaoyi.cloud.dto.CountryFlagDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountCountryDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.account.AccountCountry;
import com.lingdaoyi.cloud.mapper.AccountCountryMapper;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class AccountCountryService {
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountCountryMapper accountCountryMapper;

	public ResponseDTO getAccountCountryList(Integer clientType, String sign, String addressIP) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

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
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}

		AccountCountryDTO accountCountryDTO = null;

		List<AccountCountryDTO> accountCountryList = Lists.newArrayList();
		List<AccountCountry> list = accountCountryMapper.getAccountCountryList();
		for (AccountCountry accountCountry : list) {
			accountCountryDTO = new AccountCountryDTO();
			accountCountryDTO.setCountryId(accountCountry.getId());
			accountCountryDTO.setCountryName(accountCountry.getCountryname());
			accountCountryDTO.setCityName(accountCountry.getCityname());
			accountCountryDTO.setCode(accountCountry.getCode());
			accountCountryDTO.setCurrencyName(accountCountry.getCurrencyname());
			accountCountryDTO.setCurrencySign(accountCountry.getCurrencysign());
			accountCountryDTO.setCurrencySort(accountCountry.getCurrencysort());
			accountCountryDTO.setSort(accountCountry.getSort());
			accountCountryList.add(accountCountryDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("accountCountryList", accountCountryList);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.country.success"));
		return responseDTO;

	}

	public AccountCountryInfoDTO getAccountCountryInfo(String currencySort) {	
		AccountCountryInfoDTO dto = null;
		AccountCountry accountCountry = accountCountryMapper.selectCountryByCurrencySort(currencySort);
		if(accountCountry!=null){
			dto = new AccountCountryInfoDTO();
			dto.setSuccess(true);
			dto.setCountryId(accountCountry.getId());
			dto.setCountryName(accountCountry.getCountryname());
			dto.setCurrencyName(accountCountry.getCurrencyname());
			dto.setCurrencySign(accountCountry.getCurrencysign());
			dto.setCurrencySort(accountCountry.getCurrencysort());
			dto.setCountryFlag(accountCountry.getCountryFlag());
		}else{
			dto = new AccountCountryInfoDTO();
			dto.setSuccess(false);
		}
		return dto;
	}

	public void addCountryAndCurrencyRedis() {
		AccountCountry accountCountry=new AccountCountry();
		accountCountry.setIsDeleted(0);
		List<AccountCountry> countryList=accountCountryMapper.select(accountCountry);
		AccountCountryDTO accountCountryDTO=null;
		for (AccountCountry countryAndCurrency : countryList) {
			accountCountryDTO=new AccountCountryDTO();
			accountCountryDTO.setCountryName(countryAndCurrency.getCountryname());
			accountCountryDTO.setCityName(countryAndCurrency.getCityname());
			accountCountryDTO.setCurrencyName(countryAndCurrency.getCurrencyname());
			accountCountryDTO.setCurrencySign(countryAndCurrency.getCurrencysign());
			accountCountryDTO.setCountryId(countryAndCurrency.getId());
			accountCountryDTO.setCurrencySort(countryAndCurrency.getCurrencysort());
			RedisUtils.putCache(RedisMoudel.CountryAndCurrency, countryAndCurrency.getId().toString(), mapper.toJson(accountCountryDTO));
		}
		
	}
	
	public List<CountryFlagDTO> getCountryFlagList(){
		List<CountryFlagDTO> list = new ArrayList<>();
		List<AccountCountry> selectAll = accountCountryMapper.selectAll();
		for(AccountCountry country : selectAll){
			CountryFlagDTO dto = new CountryFlagDTO();
			dto.setCountryFlag(country.getCountryFlag());
			dto.setCurrencySort(country.getCurrencysort());
			dto.setCurrencySign(country.getCurrencysign());
			list.add(dto);
		}
//		RedisUtils.putCache(RedisMoudel.CountryAndCurrency, "countryflaglist", list.toString());
		return list ;
	}
}
