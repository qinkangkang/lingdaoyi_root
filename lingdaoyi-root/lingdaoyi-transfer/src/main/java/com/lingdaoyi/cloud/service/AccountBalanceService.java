package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountBalanceDTO;
import com.lingdaoyi.cloud.dto.AccountDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.AccountBalance;
import com.lingdaoyi.cloud.entity.AccountBankCard;
import com.lingdaoyi.cloud.entity.AccountRecord;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.mapper.AccountBalanceMapper;
import com.lingdaoyi.cloud.mapper.AccountBankCardMapper;
import com.lingdaoyi.cloud.mapper.AccountRecordMapper;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;
import com.lingdaoyi.cloud.utils.UUIDUtils;
import com.lingdaoyi.cloud.utils.date.DateFormatUtils;
import com.lingdaoyi.cloud.utils.date.DateUtils;

@Service
@Transactional
public class AccountBalanceService {

	@Autowired
	private AccountBalanceMapper acountBalanceMapper;

	@Autowired
	private AccountFeignClient accountFeignClient;

	@Autowired
	private AccountBankCardMapper accountBankCardMapper;

	@Autowired
	private AccountRecordMapper accountRecordMapper;

	public ResponseDTO getAccountBalance(String ticket, Integer clientType, String sign, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(addressIP)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("addressIP" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

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

		AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();

		// 查询该账户余额
		AccountBalance balance = acountBalanceMapper.selectByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		if (balance == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.111"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.111"));
			return responseDTO;
		}
		accountBalanceDTO.setAcountId(balance.getAccountId());
		accountBalanceDTO.setBalance(balance.getBalance());
		accountBalanceDTO.setCurrencySign(balance.getCurrencySign());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountBalance", accountBalanceDTO);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.balance.success"));
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public AccountBalanceDTO getAccountBalanceById(Long accountId) {
		AccountBalanceDTO balanceDTO = null;
		if (accountId == null) {
			return balanceDTO;
		}
		balanceDTO = new AccountBalanceDTO();
		AccountBalance balance = acountBalanceMapper.selectByPrimaryKey(accountId);
		balanceDTO.setAcountId(accountId);
		balanceDTO.setBalance(balance.getBalance());
		return balanceDTO;
	}

	public void createAccountBalance(Long acocuntId, String currencySign) {
		AccountBalance balance = new AccountBalance();
		balance.setAccountId(acocuntId);
		balance.setBalance(new BigDecimal(0));
		balance.setGmtCreate(new Date());
		balance.setCurrencySign(currencySign);
		acountBalanceMapper.insert(balance);

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

	/**
	 * 余额提现
	 * 
	 * @param acocuntId
	 * @param money
	 * @param clientType
	 * @param ticket
	 * @return
	 */
	public ResponseDTO withdrawBalance(String payPassword, String money, Long bankcardId, String ticket,
			Integer clientType, String sign, String addressIP, Integer type) {
		ResponseDTO dto = new ResponseDTO();
		if (StringUtils.isBlank(payPassword)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("payPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if (StringUtils.isBlank(money)) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("money" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}

		if (type != 3 && type != 6) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("type" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if (type == 3) {
			if (bankcardId == null || bankcardId < 1) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				dto.setMsg("bankcardId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				return dto;
			}
		}
		ResponseDTO accountBalance = getAccountBalance(ticket, clientType, sign, addressIP);
		if (!accountBalance.isSuccess()) {
			return accountBalance;
		}
		try {
			money = DesUtil3.decryptThreeDESECB(money, DesUtil3.KEY);
			// bankcard = DesUtil3.decryptThreeDESECB(bankcard, DesUtil3.KEY);
			// payPassword = DesUtil3.decryptThreeDESECB(payPassword,
			// DesUtil3.KEY);
		} catch (Exception e) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.202"));
			dto.setMsg("money/bankcard" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.202"));
			return dto;
		}
		Map<String, Object> data = accountBalance.getData();
		AccountBalanceDTO accountBalanceDTO = (AccountBalanceDTO) data.get("accountBalance");
		BigDecimal balance = accountBalanceDTO.getBalance();
		BigDecimal bigDecimalMoney = new BigDecimal(money);

		Long accountIdL = Long.valueOf(accountBalanceDTO.getAcountId());
		String accountById = accountFeignClient.getAccountById(accountIdL);
		AccountDTO accountDTO = JsonMapper.fromJsonString(accountById, AccountDTO.class);
		if (type == 6 && accountDTO.getCountryId()== 1L) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.119"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCard.119"));
			return dto;
		}
		if (type == 3) {
			// 获取银行卡信息
			AccountBankCard accountBankCard = accountBankCardMapper.selectAccountBankCardById(bankcardId);
			if (accountBankCard == null) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCard.error"));
				return dto;
			}
			if (accountBankCard != null && accountBankCard.getAccountId() != accountIdL) {
				dto.setSuccess(false);
				dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
				dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.backCard.error2"));
				return dto;
			}
		}
		// 支付密码校验--依据id查询account中的支付密码(数据库中是加密的密码)
		if (!payPassword.equals(accountDTO.getPayPassword())) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.120"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.120"));
			return dto;
		}
		if (balance.doubleValue() < bigDecimalMoney.doubleValue()) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.115"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.115"));
			return dto;
		}
		// 查询该账户余额
		AccountBalance selectByAccountId = acountBalanceMapper.selectByAccountId(accountIdL);
		if (selectByAccountId == null) {
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.111"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.111"));
			return dto;
		}
		// 获取用户锁定金额，修改用户余额及锁定金额
		BigDecimal lockingMoney = selectByAccountId.getLockingMoney();
		BigDecimal newbalance = balance.subtract(bigDecimalMoney);
		if (lockingMoney != null && lockingMoney.doubleValue() > 0) {
			selectByAccountId.setBalance(newbalance);
			selectByAccountId.setLockingMoney(lockingMoney.add(bigDecimalMoney));
			acountBalanceMapper.updateByPrimaryKeySelective(selectByAccountId);
		} else {
			selectByAccountId.setBalance(newbalance);
			selectByAccountId.setLockingMoney(bigDecimalMoney);
			acountBalanceMapper.updateByPrimaryKeySelective(selectByAccountId);
		}

		if (type == 3) {
			// 线上提现
			// 调用预付卡转账接口
			//
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.withdrawbalance.success"));
		} else {
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.200"));
		}
		Date date = new Date();
		String year = DateUtils.date2String(date, DateFormatUtils.DATE_YEAR);
		String month = DateUtils.date2String(date, DateFormatUtils.DATE_MONTH);
		String day = DateUtils.date2String(date, DateFormatUtils.DATE_DAY);
		StringBuffer buffer = new StringBuffer();
		buffer.append(year).append(month).append(day).append(UUIDUtils.uuid());
		String payNo = buffer.toString();
		// 添加流水
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setPayNo(payNo);
		accountRecord.setAccountId(accountIdL);
		accountRecord.setReceiveCardId(bankcardId);
		accountRecord.setTransferMoney(bigDecimalMoney.negate());
		accountRecord.setAddressIp(addressIP);
		accountRecord.setType(type);
		accountRecord.setIsDeleted((byte) 0);
		accountRecord.setGmtCreate(date);
		if (type == 3) {
			accountRecord.setTransferInstructions(DictionaryUtil.getString(DictionaryUtil.TransferType, 3));
		} else {
			accountRecord.setTransferInstructions(DictionaryUtil.getString(DictionaryUtil.TransferType, 6));
		}
		accountRecord.setCurrencyName(accountDTO.getCurrencyName());
		accountRecordMapper.insertSelective(accountRecord);

		dto.setSuccess(true);
		dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		return dto;
	}

	public AccountBalanceDTO getBalance(Long accountId) {
		AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
		if (accountId != null) {
			// 查询该账户余额
			AccountBalance balance = acountBalanceMapper.selectByAccountId(accountId);
			if (balance != null) {
				accountBalanceDTO.setAcountId(balance.getAccountId());
				accountBalanceDTO.setBalance(balance.getBalance());
				accountBalanceDTO.setCurrencySign(balance.getCurrencySign());
			}
		}
		return accountBalanceDTO;
	}

}
