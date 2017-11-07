package com.lingdaoyi.cloud.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.entity.AuthenticationRecord;
import com.lingdaoyi.cloud.mapper.AuthenticationRecordMapper;

@Service
@Transactional
public class AuthenticationRecordService {

	@Autowired
	private AuthenticationRecordMapper authenticationRecordMapper;
	
	
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
	
	/**
	 * 添加鉴权流水
	 * @param accountId
	 * @param accountRealname
	 * @param idcard
	 * @param mobile
	 * @param bankcard
	 * @param bankName
	 * @param bankCode
	 * @param bankIdcard
	 * @param bankRealname
	 * @param bankMobile
	 * @param key
	 * @param isOk
	 */
	public void insertAuthenticationRecord(Long accountId, String accountRealname, String idcard, String mobile,
			String bankcard, String bankName, String bankCode, String bankIdcard, String bankRealname,
			String bankMobile, String enkey, Integer isOk,String ordersign) {
		AuthenticationRecord authenticationRecord = new AuthenticationRecord();
		authenticationRecord.setAccountId(accountId);
		authenticationRecord.setAccountRealname(accountRealname);
		authenticationRecord.setIdcard(idcard);
		authenticationRecord.setMobile(mobile);
		authenticationRecord.setBankcard(bankcard);
		authenticationRecord.setBankCode(bankCode);
		authenticationRecord.setBankIdcard(bankIdcard);
		authenticationRecord.setBankMobile(bankMobile);
		authenticationRecord.setBankName(bankName);
		authenticationRecord.setBankRealname(bankRealname);
		authenticationRecord.setEnkey(enkey);
		authenticationRecord.setIsOk(isOk);
		authenticationRecord.setOrdersign(ordersign);
		authenticationRecord.setGmtCreate(new Date());
		authenticationRecordMapper.insertSelective(authenticationRecord);

	}
}
