package com.lingdaoyi.cloud.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.OffLineQRCodeDTO;
import com.lingdaoyi.cloud.dto.account.QRCodeDTO;
import com.lingdaoyi.cloud.dto.account.QRCodeInfoDTO;
import com.lingdaoyi.cloud.dto.account.ReceiptQRcodeDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountInfo;
import com.lingdaoyi.cloud.mapper.AccountInfoMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.utils.base.StringUtils;

@Service
@Transactional
public class QRCodeService {

	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private AccountInfoMapper accountInfoMapper;
	@Autowired
	private EmcService emcService;
	
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
	
	
	public QRCodeDTO myQRcode(String ticket, Integer clientType) {
		QRCodeDTO dto = new QRCodeDTO();
		if(org.apache.commons.lang3.StringUtils.isBlank(ticket)){
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if(clientType < 1 || clientType > 3){
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		AccountSysDTO accountByTicket = emcService.getAccountByTicket(ticket, clientType);
		if (!accountByTicket.isEnable()) {
			
			dto.setError_code(accountByTicket.getStatusCode());
			dto.setSuccess(false);
			dto.setError_msg(accountByTicket.getMsg());
			return dto;
		}
		String accountId = accountByTicket.getAccountId();
		long accountIdL = Long.parseLong(accountId);
		AccountInfo accountInfo = accountInfoMapper.findByAccountId(accountIdL);
		if (accountInfo != null && accountInfo.getAccountId() > 0) {
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			String qrCode = accountInfo.getQrCode();
			StringBuffer sb = new StringBuffer(qrCode);
			sb.append(":1");
			dto.setQrcode(sb.toString());
			dto.setAccountId(accountId);
			dto.setSuccess(true);
			dto.setQrcodeType(1);
		}
		return dto;
	}

	public QRCodeInfoDTO QRcodeInfo(String ticket, Integer clientType, String qrcodestr) {
		QRCodeInfoDTO dto = null;
		String loginname = "";
		if(org.apache.commons.lang3.StringUtils.isBlank(ticket)){
			dto = new QRCodeInfoDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if(clientType < 1 || clientType > 3){
			dto = new QRCodeInfoDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		try {
			int lastIndexOf = qrcodestr.lastIndexOf(":");
			qrcodestr = qrcodestr.substring(lastIndexOf + 1);
			loginname = DesUtil3.decryptThreeDESECB(qrcodestr, DesUtil3.KEY);
		} catch (Exception e) {
			dto = new QRCodeInfoDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setSuccess(false);
			dto.setError_msg("qrcodestr" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			return dto;
		}

		AccountSysDTO accountByTicket = emcService.getAccountByTicket(ticket, clientType);
		if (!accountByTicket.isEnable()) {
			dto = new QRCodeInfoDTO();
			dto.setError_code(accountByTicket.getStatusCode());
			dto.setSuccess(false);
			dto.setError_msg(accountByTicket.getMsg());
			return dto;
		}

		Account account = accountMapper.findByLoginName(loginname);
		AccountInfo accountInfo = null;
		String accountId = "";
		if (account != null && account.getId() > 0) {
			accountId = account.getId().toString();
			accountInfo = accountInfoMapper.findByAccountId(account.getId());
		}

		if (accountInfo != null && accountInfo.getId() > 0) {
			dto = new QRCodeInfoDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			Integer isAuth = account.getIsAuth();
			if (isAuth == 1) {
				dto.setAuth(true);
			} else {
				dto.setAuth(false);
			}
			dto.setAccountId(accountId);
			dto.setHeadUrl(account.getHeadUrl());
			String loginName = account.getLoginName();
//			dto.setLoginName(StringUtils.replaceStr(loginName, 3, loginName.length() - 2, "*"));
			dto.setLoginName(loginName);
			dto.setNickName(accountInfo.getNickname());
			dto.setRealName(account.getRealName());
			dto.setSuccess(true);
		}
		dto.setQrcodeType(1);
		return dto;
	}

	public ReceiptQRcodeDTO receiptQRcode(String ticket, Integer clientType, String qrcodestr) {
		ReceiptQRcodeDTO dto = null;
		String loginname = "";
		String money = "";
		String x = ":";
		
		if(org.apache.commons.lang3.StringUtils.isBlank(ticket)){
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if(org.apache.commons.lang3.StringUtils.isBlank(qrcodestr)){
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("qrcodestr" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if(clientType < 1 || clientType > 3){
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setError_msg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		try {
			String[] split = qrcodestr.split(x);
			if(split.length == 3){
				loginname = DesUtil3.decryptThreeDESECB(split[1], DesUtil3.KEY);
			}else if(split.length == 4){
				loginname = DesUtil3.decryptThreeDESECB(split[1], DesUtil3.KEY);
				money = DesUtil3.decryptThreeDESECB(split[3], DesUtil3.KEY);
			}else{
				dto = new ReceiptQRcodeDTO();
				dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
				dto.setSuccess(false);
				dto.setError_msg("qrcodestr" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
				return dto;
			}	
		} catch (Exception e) {
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setSuccess(false);
			dto.setError_msg("qrcodestr" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			return dto;
		}

		AccountSysDTO accountByTicket = emcService.getAccountByTicket(ticket, clientType);
		if (!accountByTicket.isEnable()) {
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(accountByTicket.getStatusCode());
			dto.setSuccess(false);
			dto.setError_msg(accountByTicket.getMsg());
			return dto;
		}
		Account account = accountMapper.findByLoginName(loginname);
		AccountInfo accountInfo = null;
		String accountId = "";
		if (account != null && account.getId() > 0) {
			accountId = account.getId().toString();
			accountInfo = accountInfoMapper.findByAccountId(account.getId());
		}
		if (accountInfo != null && accountInfo.getId() > 0) {
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setAccountId(accountId);
			dto.setHeadUrl(account.getHeadUrl());
			dto.setNickName(accountInfo.getNickname());
			dto.setReceiptType(1);
			dto.setRealName(account.getRealName());
			dto.setSuccess(true);
			dto.setMoney(money);
		}
		dto.setQrcodeType(2);
		return dto;
	}


	public QRCodeDTO getReceiptQRcode(String ticket, Integer clientType, String money) {
		QRCodeDTO myQRcode = null;
		myQRcode = myQRcode(ticket,clientType);
		if(myQRcode!=null&&myQRcode.isSuccess()){
			String qrcode = myQRcode.getQrcode();
			myQRcode.setQrcode(qrcode.replace("==:1", "==:2"));
			if(org.apache.commons.lang3.StringUtils.isNotBlank(money)){
				StringBuffer sb = new StringBuffer();
				sb = sb.append(myQRcode.getQrcode()).append(":").append(money);
				myQRcode.setQrcode(sb.toString());
				return myQRcode;
			}
		}else{
			myQRcode = new QRCodeDTO();
			myQRcode.setSuccess(false);
			myQRcode.setError_msg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcode.fail"));
			myQRcode.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
		}
		myQRcode.setQrcodeType(2);
		return myQRcode;
	}


	public ResponseDTO offlineRechargeCode(String ticket, Integer clientType, Long accountId) {
		ResponseDTO dto = new ResponseDTO();
		Account account = null;
		AccountInfo accountInfo = null;
		if(org.apache.commons.lang3.StringUtils.isBlank(ticket)){
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}else{
			AccountSysDTO accountByTicket = emcService.getAccountByTicket(ticket, clientType);
			if (!accountByTicket.isEnable()) {
				dto.setStatusCode(accountByTicket.getStatusCode());
				dto.setSuccess(false);
				dto.setMsg(accountByTicket.getMsg());
				return dto;
			}
		}
		if(accountId < 1){
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setMsg("accountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}else{
			account = accountMapper.selectByPrimaryKey(accountId);
			accountInfo = accountInfoMapper.findByAccountId(accountId);
		}
		if(clientType < 1 || clientType > 3){
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setSuccess(false);
			dto.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		OffLineQRCodeDTO offlinedto = new OffLineQRCodeDTO();
//		String loginName = StringUtils.replaceStr(accountInfo.getLoginName(), 0, 7, "*");
		offlinedto.setAccountLoginName(accountInfo.getLoginName());
		String qrCode = accountInfo.getQrCode();
		StringBuffer sb = new StringBuffer(qrCode);
		sb.append(":3");
		offlinedto.setQrcode(sb.toString());
		offlinedto.setAccountName(account.getRealName());
		offlinedto.setAccountId(accountId.toString());
		offlinedto.setQrcodeType(3);
		dto.setSuccess(true);
		dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
		Map<String,Object> map = new HashMap<>();
		map.put("result", offlinedto);
		dto.setData(map);
		return dto;
	}

}
