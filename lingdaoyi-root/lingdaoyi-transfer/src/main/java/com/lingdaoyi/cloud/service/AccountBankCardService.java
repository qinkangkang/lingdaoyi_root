package com.lingdaoyi.cloud.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountBankCardDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.encrypt.other.StringUtils;
import com.lingdaoyi.cloud.entity.AccountBankCard;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.mapper.AccountBankCardMapper;
import com.lingdaoyi.cloud.utils.JsonMapper;

@Service
@Transactional
public class AccountBankCardService {
	private static final Logger logger = LoggerFactory.getLogger(AccountRecordService.class);

	@Autowired
	private AccountBankCardMapper accountBankCardMapper;

	@Autowired
	private AccountFeignClient accountFeignClient;

	public ResponseDTO bindingBankCard(Integer clientType, String ticket, String bankCardNO, String securityCode,
			String sign, String addressIP, String bankName, String bankCode, String cardType, String bankImage,
			String telephone) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(telephone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("telephone" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(bankCardNO)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("backCardNO" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(bankCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("bankCode" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(bankImage)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("bankImage" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(cardType)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("cardType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(securityCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("securityCode" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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

		String des3cardNO = null;
		String des3Telephone=null;
		String des3tSecurityCode = null;
		// 银行卡卡号解密; 验证码解密
		try {
			des3cardNO = DesUtil3.decryptThreeDESECB(bankCardNO, DesUtil3.KEY);
			des3Telephone = DesUtil3.decryptThreeDESECB(telephone, DesUtil3.KEY);
			des3tSecurityCode = DesUtil3.decryptThreeDESECB(securityCode, DesUtil3.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 解密的银行卡卡号和验证码加密后与sign做比对，判断是否一致
		// 加密参数验证
//		String signStr = null;
//		StringBuffer sb = new StringBuffer();
		// TODO
		//sb.append("18746684211");
//		sb.append(clientType).append(addressIP).append(des3cardNO).append(des3tSecurityCode).append(des3Telephone);
//		try {
//			signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		if (!signStr.equals(sign)) {
//			responseDTO.setSuccess(false);
//			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
//			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
//			return responseDTO;
//		}

		// TODO
		// 与银行验证验证码比对

		// 判断数据库中是否已经存在此银行卡信息;
		// 如果已存在,则更改时间;
		AccountBankCard accountBankCard = new AccountBankCard();
		accountBankCard.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		accountBankCard.setBankCard(des3cardNO);
		accountBankCard.setBankName(bankName);
		accountBankCard.setBankCode(bankCode);
		accountBankCard.setBankImage(bankImage);
		accountBankCard.setCard_type(cardType);
		accountBankCard.setRealName(accountSysDTO.getAccountName());
		accountBankCard.setStatus(1);
		AccountBankCard bankCard = accountBankCardMapper.selectOne(accountBankCard);
		if (bankCard == null) {// 如果没有;则重新绑定
			accountBankCard.setGmtCreate(new Date());
			accountBankCard.setMobile(des3Telephone);
			accountBankCardMapper.insertSelective(accountBankCard);
		} else {// 如果已绑定;重新修改绑定时间
			bankCard.setGmtCreate(new Date());
			if (!des3Telephone.equals(bankCard.getMobile())) {
				bankCard.setMobile(des3Telephone);
			}
			accountBankCardMapper.updateByPrimaryKey(bankCard);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCard.success"));
		return responseDTO;
	}

	public ResponseDTO getBankCardList(String ticket, Integer clientType, String sign, String addressIP) {

		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		if (clientType == null) {
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

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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

		List<AccountBankCard> bankCardList = accountBankCardMapper
				.selectBankCardByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		if (bankCardList != null && bankCardList.size() == 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			responseDTO.setMsg("您还未绑定银行卡!!!");
			return responseDTO;
		}
		List<AccountBankCardDTO> dtoList = new ArrayList<AccountBankCardDTO>();
		for (AccountBankCard accountBankCard : bankCardList) {
			AccountBankCardDTO cardDTO = new AccountBankCardDTO();
			String bankNO = accountBankCard.getBankCard();
			cardDTO.setBankCardId(accountBankCard.getId());
			cardDTO.setBankCardNO(bankNO.substring(bankNO.length() - 4));
			cardDTO.setBankName(accountBankCard.getBankName());
			cardDTO.setStatus(accountBankCard.getStatus());
			cardDTO.setBankCardImage(accountBankCard.getBankImage());
			cardDTO.setBankCardUserName(accountBankCard.getRealName());
			cardDTO.setCardType(accountBankCard.getCard_type());
			dtoList.add(cardDTO);
		}

		returnData.put("bankCardDTOList", dtoList);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCardList.success"));
		return responseDTO;
	}

	public ResponseDTO deleteBankCard(String ticket, Long bankCardId, Integer clientType, String sign,
			String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (bankCardId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("bankCardId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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
		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(bankCardId);
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
		AccountBankCard accountBankCard = new AccountBankCard();
		accountBankCard.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		accountBankCard.setId(bankCardId);
		accountBankCard.setStatus(1);
		AccountBankCard bankCard = accountBankCardMapper.selectOne(accountBankCard);
		bankCard.setStatus(0);
		accountBankCardMapper.updateByPrimaryKeySelective(bankCard);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.deleteBackCard.success"));
		return responseDTO;
	}

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

}
