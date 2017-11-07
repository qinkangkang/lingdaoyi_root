package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountDTO;
import com.lingdaoyi.cloud.dto.AccountRecordByMonthDTO;
import com.lingdaoyi.cloud.dto.AccountRecordDTO;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.AccountBalance;
import com.lingdaoyi.cloud.entity.AccountBankCard;
import com.lingdaoyi.cloud.entity.AccountExchangerate;
import com.lingdaoyi.cloud.entity.AccountRecord;
import com.lingdaoyi.cloud.entity.AccountRecordByMonth;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.mapper.AccountBalanceMapper;
import com.lingdaoyi.cloud.mapper.AccountBankCardMapper;
import com.lingdaoyi.cloud.mapper.AccountExchangerateMapper;
import com.lingdaoyi.cloud.mapper.AccountRecordMapper;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;
import com.lingdaoyi.cloud.utils.UUIDUtils;
import com.lingdaoyi.cloud.utils.date.DateFormatUtils;
import com.lingdaoyi.cloud.utils.date.DateUtils;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

import junit.framework.Assert;

@Service
@Transactional
public class AccountRecordService {

	@Autowired
	private AccountRecordMapper accountRecordMapper;

	@Autowired
	private AccountBalanceMapper accountBalanceMapper;

	@Autowired
	private AccountExchangerateMapper accountExchangeMapper;

	@Autowired
	private AccountBankCardMapper accountBankCardMapper;

	@Autowired
	private AccountFeignClient accountFeignClient;

	public ResponseDTO findLastRecord(String ticket, Integer clientType, String sign, String addressIP, Integer count) {
		ResponseDTO responseDTO = new ResponseDTO();

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

		// sign校验
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP);
		try {
			signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
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

		List<AccountRecord> recordList = accountRecordMapper.findLastRecord(Long.valueOf(accountSysDTO.getAccountId()),
				count);

		if (recordList == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.114"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.114"));
			return responseDTO;
		}
		if (recordList.size() == 0) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.118"));
			return responseDTO;
		}
		List<AccountRecordDTO> dtoList = new ArrayList<AccountRecordDTO>();

		for (AccountRecord record : recordList) {
			AccountRecordDTO recordDTO = new AccountRecordDTO();
			// 根据id查询当前账户的真实姓名,账号
			String accountById = accountFeignClient.getAccountById(record.getAccountId());
			AccountDTO accountDTO = JsonMapper.fromJsonString(accountById, AccountDTO.class);
			recordDTO.setAccountLoginName(accountDTO.getLoginName());
			recordDTO.setAccountRealName(accountDTO.getRealName());
			recordDTO.setAccountRecordId(record.getId());
			Long receiveAccountId = record.getReceiveAccountId();// 获取收账人ID
			// 获取对方的账户信息,先从缓存中获取ticket,没有,则依据id查询数据库获取信息
			// 获取用户ticket缓存对象
			String receiveName = null;
			String accountEentity = null;
			try {
				// 用户缓存信息
				accountEentity = RedisUtils.getValue(receiveAccountId.toString(), RedisMoudel.AccountEentity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 如果缓存为null,则从数据库查找
			if (accountEentity == null) {
				String receiveAccountJson = accountFeignClient.getAccountById(receiveAccountId);
				AccountDTO receiveAccountDTO = JsonMapper.fromJsonString(receiveAccountJson, AccountDTO.class);
				recordDTO.setReceiveAccountLoginName(receiveAccountDTO.getLoginName());
				recordDTO.setReceiveAccountRealName(receiveAccountDTO.getRealName());
				recordDTO.setAccountHeadUrl(receiveAccountDTO.getHeadUrl());
			} else {
				AccountSysDTO receiveAccountSysDTO = JsonMapper.fromJsonString(accountEentity, AccountSysDTO.class);
				recordDTO.setReceiveAccountRealName(receiveAccountSysDTO.getAccountRealName());
				recordDTO.setReceiveAccountLoginName(receiveAccountSysDTO.getAccountName());
				recordDTO.setAccountHeadUrl(receiveAccountSysDTO.getAccountHeadUrl());
			}
			recordDTO.setType(record.getType());// 资金交易方式
			// if(record.getIsSuccess().equals("1")){
			// recordDTO.setTransferState("交易成功");
			// }else{
			// recordDTO.setTransferState("交易失败");
			// }
			// 备注
			recordDTO.setTransferInstructions(record.getTransferInstructions());

			// 流水号
			recordDTO.setPayNo(record.getPayNo());

			recordDTO.setTransferMoney(record.getTransferMoney());
			Date date = record.getGmtCreate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String dateNowStr = sdf.format(date);
			recordDTO.setCreatetTime(dateNowStr);
			dtoList.add(recordDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("recordListDTO", dtoList);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setData(returnData);
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.record.success"));

		return responseDTO;

	}

	public ResponseDTO getCapitalFlowRecord(String ticket, Integer clientType, Integer pageNum, Integer pageSize,
			Integer type, Long receiveAccountId, String sign, String addressIP, Integer isGroup) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> data = Maps.newHashMap();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (pageNum == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("pageNum" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (pageSize == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("pageSize" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// TODO 加密校验
		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PageHelper.startPage(pageNum, pageSize);
		Long id = Long.valueOf(accountSysDTO.getAccountId());
		List<AccountRecordByMonth> list = accountRecordMapper.selectTransferRecordList(id, receiveAccountId, type);
		List<String> monthList = accountRecordMapper.selectAllMonth();
		List<AccountRecordDTO> dtoList = new ArrayList<AccountRecordDTO>();
		List<AccountRecordByMonthDTO> mothDtoList = null;
		if (isGroup != null && isGroup == 1) {
			mothDtoList = new ArrayList<AccountRecordByMonthDTO>();
			for (String time : monthList) {
				AccountRecordByMonthDTO monthDTO = new AccountRecordByMonthDTO();
				List<AccountRecordDTO> recordList = new ArrayList<AccountRecordDTO>();
				for (AccountRecordByMonth record : list) {
					if (record.getMonth().equals(time)) {
						monthDTO.setMonth(time);
						AccountRecordDTO recordDTO = new AccountRecordDTO();
						recordDTO.setAccountRecordId(record.getId());// 资金记录的id
						recordDTO.setMonth(record.getMonth());
						recordDTO.setAccountLoginName(accountSysDTO.getAccountName());
						recordDTO.setAccountRealName(accountSysDTO.getAccountRealName());
						recordDTO.setTransferWay(record.getTransferWay());
						Integer recordType = record.getType();
						//创建的时间-月-日
						Date date = record.getGmtCreate();
						String month = DateUtils.date2String(date, DateFormatUtils.DATE_MONTH);
						String day = DateUtils.date2String(date, DateFormatUtils.DATE_DAY);
						StringBuffer buffer = new StringBuffer();
						buffer.append(month).append("-").append(day);
						recordDTO.setCreateMonthAndDay(buffer.toString());
						// 转账或支付类有接收人
						if (recordType == 1 || recordType == 2 || recordType == 4 || recordType == 5) {
							// 获取对方的账户信息,先从缓存中获取,没有,查询数据库
							Long receviceId = record.getReceiveAccountId();
							String receiveName = null;
							// 获取用户DTO缓存对象
							String accountEentity = null;
							try {
								// 用户缓存信息
								accountEentity = RedisUtils.getValue(receviceId.toString(), RedisMoudel.AccountEentity);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 如果缓存为null,则从数据库查找
							if (accountEentity == null) {
								String receiveAccountJson = accountFeignClient.getAccountById(receviceId);
								AccountDTO accountDTO = JsonMapper.fromJsonString(receiveAccountJson, AccountDTO.class);
								recordDTO.setReceiveAccountRealName(accountDTO.getRealName());
								recordDTO.setReceiveAccountLoginName(accountDTO.getLoginName());
								recordDTO.setAccountHeadUrl(accountDTO.getHeadUrl());

							} else {
								AccountSysDTO receiveAccountSys = JsonMapper.fromJsonString(accountEentity,
										AccountSysDTO.class);
								recordDTO.setReceiveAccountLoginName(receiveAccountSys.getAccountName());
								recordDTO.setReceiveAccountRealName(receiveAccountSys.getAccountRealName());
								recordDTO.setAccountHeadUrl(receiveAccountSys.getAccountHeadUrl());
							}
							// TODO
							// 付款/收款理由设置为"收款"
							recordDTO.setTransferReason("收款");
						} else if (recordType == 0 || recordType == 7) {
							// 充值
							Long blankCardId = record.getBlankCardId();//充值用的银行卡id;
							AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(blankCardId);
							String bankName = bankCard.getBankName();
							recordDTO.setBankName(bankName);
							

						} else if (recordType == 3 || recordType == 6) {
							// 提现
							Long receiveCardId = record.getReceiveCardId();//提现的银行卡的id
							AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(receiveCardId);
							String bankName = bankCard.getBankName();
							String bankCardNumber = bankCard.getBankCard();
							String substring = bankCardNumber.substring(bankCardNumber.length()-4);
							StringBuffer bf=new StringBuffer();
							bf.append(bankName).append("(").append(substring).append(")");
							recordDTO.setBankName(buffer.toString());
							recordDTO.setBankImage(bankCard.getBankImage());
						}


						recordDTO.setTransferMoney(record.getTransferMoney());// 交易金额
						recordDTO.setType(record.getType());// 资金交易方式
						// 资金记录创建时间
						String dateNowStr = sdf.format(date);
						recordDTO.setCreatetTime(dateNowStr);

						recordDTO.setTransferState(DictionaryUtil.getString(DictionaryUtil.payStatus,
								Integer.valueOf(record.getIsSuccess())));
						// 备注
						recordDTO.setTransferInstructions(record.getTransferInstructions());
						// 流水号
						recordDTO.setPayNo(record.getPayNo());
						recordList.add(recordDTO);
						recordDTO.setReceivedTime(sdf.format(new Date()));//到账时间;
					}
				}

				monthDTO.setRecordList(recordList);
				if (recordList != null && recordList.size() > 0) {
					mothDtoList.add(monthDTO);
				}

			}
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.record.success"));
			PageDTO pageDTO = new PageDTO<AccountRecordByMonth>(list);
			pageDTO.setList(mothDtoList);
			data.put("page", pageDTO);
			responseDTO.setData(data);
		} else {
			for (AccountRecordByMonth record : list) {
				AccountRecordDTO recordDTO = new AccountRecordDTO();
				recordDTO.setAccountRecordId(record.getId());// 资金记录的id
				recordDTO.setMonth(record.getMonth());
				recordDTO.setAccountLoginName(accountSysDTO.getAccountName());
				recordDTO.setAccountRealName(accountSysDTO.getAccountRealName());
				recordDTO.setReceivedTime(sdf.format(new Date()));
				recordDTO.setTransferWay(record.getTransferWay());
				Integer recordType = record.getType();
				//创建的时间-月-日
				Date date = record.getGmtCreate();
				String month = DateUtils.date2String(date, DateFormatUtils.DATE_MONTH);
				String day = DateUtils.date2String(date, DateFormatUtils.DATE_DAY);
				StringBuffer buffer = new StringBuffer();
				buffer.append(month).append("-").append(day);
				recordDTO.setCreateMonthAndDay(buffer.toString());
				// 转账或支付类有接收人
				if (recordType == 1 || recordType == 2 || recordType == 4 || recordType == 5) {
					// 获取对方的账户信息,先从缓存中获取,没有,查询数据库
					Long receviceId = record.getReceiveAccountId();
					String receiveName = null;
					// 获取用户DTO缓存对象
					String accountEentity = null;
					try {
						// 用户缓存信息
						accountEentity = RedisUtils.getValue(receviceId.toString(), RedisMoudel.AccountEentity);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// 如果缓存为null,则从数据库查找
					if (accountEentity == null) {
						String receiveAccountJson = accountFeignClient.getAccountById(receviceId);
						AccountDTO accountDTO = JsonMapper.fromJsonString(receiveAccountJson, AccountDTO.class);
						recordDTO.setReceiveAccountRealName(accountDTO.getRealName());
						recordDTO.setReceiveAccountLoginName(accountDTO.getLoginName());
						recordDTO.setAccountHeadUrl(accountDTO.getHeadUrl());

					} else {
						AccountSysDTO receiveAccountSys = JsonMapper.fromJsonString(accountEentity,
								AccountSysDTO.class);
						recordDTO.setReceiveAccountLoginName(receiveAccountSys.getAccountName());
						recordDTO.setReceiveAccountRealName(receiveAccountSys.getAccountRealName());
						recordDTO.setAccountHeadUrl(receiveAccountSys.getAccountHeadUrl());
					}
					// TODO
					// 付款/收款理由设置为"收款"
					recordDTO.setTransferReason("收款");
				} else if (recordType == 0 || recordType == 7) {
					// 充值
					Long blankCardId = record.getBlankCardId();//充值用的银行卡id;
					AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(blankCardId);
					String bankName = bankCard.getBankName();
					recordDTO.setBankName(bankName);
					recordDTO.setBankImage(bankCard.getBankImage());
				} else if (recordType == 3 || recordType == 6) {
					// 提现
					Long receiveCardId = record.getReceiveCardId();//提现的银行卡的id
					AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(receiveCardId);
					String bankName = bankCard.getBankName();
					String bankCardNumber = bankCard.getBankCard();
					String substring = bankCardNumber.substring(bankCardNumber.length()-4);
					StringBuffer bf=new StringBuffer();
					bf.append(bankName).append("(").append(substring).append(")");
					recordDTO.setBankName(buffer.toString());
					recordDTO.setBankImage(bankCard.getBankImage());
				}

				recordDTO.setTransferMoney(record.getTransferMoney());// 交易金额
				recordDTO.setType(record.getType());// 资金交易方式

				// 资金记录创建时间
				String dateNowStr = sdf.format(date);
				recordDTO.setCreatetTime(dateNowStr);

				recordDTO.setTransferState(
						DictionaryUtil.getString(DictionaryUtil.payStatus, Integer.valueOf(record.getIsSuccess())));

				// 备注
				recordDTO.setTransferInstructions(record.getTransferInstructions());
				// 流水号
				recordDTO.setPayNo(record.getPayNo());

				dtoList.add(recordDTO);

			}
			PageDTO pageDTO = new PageDTO<AccountRecordByMonth>(list);
			pageDTO.setList(dtoList);
			data.put("page", pageDTO);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.record.success"));
			responseDTO.setData(data);

		}

		return responseDTO;
	}

	public ResponseDTO accountTransfer(String type, String ticket, Integer clientType, String transferAmount,
			String receiveAccountId, String payPassword, String sign, String addressIP, String orgCurrency,
			String transCurrency, String exchageTime, String exchangeValue, String afterTransferAmount, String remarks,
			String fingerprintSuccess) {

		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> data = Maps.newHashMap();
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

		// 前端判断指纹正确后调用转账接口;如果无指纹验证支付密码
		if (StringUtils.isBlank(fingerprintSuccess) || !"1".equals(fingerprintSuccess)) {
			if (StringUtils.isBlank(payPassword)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				responseDTO
						.setMsg("payPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				return responseDTO;
			}
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(transferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("transferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(afterTransferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(
					"afterTransferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(type)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("type" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(receiveAccountId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("receiveAccountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;

		}

		// 汇率确认
		if (StringUtils.isBlank(orgCurrency)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("orgCurrency" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(afterTransferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(
					"afterTransferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(transCurrency)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("transCurrency" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(exchangeValue)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("exchangeValue" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String ticketCache = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(ticketCache, AccountSysDTO.class);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		// 1. transferAmount及payPassword解密
		String des3PayPassword = null;
		String des3TransferAmount = null;
		String des3AfterTransferAmount = null;
		String des3Type = null;
		String des3ReceiveAccountId = null;
		// 原货币,转换货币以及汇率解密
		String des3Org_currency = null;
		String des3Trans_currency = null;
		String des3ExchangeValue = null;
		try {
			if (StringUtils.isNotBlank(payPassword)) {
				des3PayPassword = DesUtil3.decryptThreeDESECB(payPassword, DesUtil3.KEY);
			}
			des3TransferAmount = DesUtil3.decryptThreeDESECB(transferAmount, DesUtil3.KEY);
			des3Type = DesUtil3.decryptThreeDESECB(type, DesUtil3.KEY);
			des3ReceiveAccountId = DesUtil3.decryptThreeDESECB(receiveAccountId, DesUtil3.KEY);
			des3Org_currency = DesUtil3.decryptThreeDESECB(orgCurrency, DesUtil3.KEY);
			des3Trans_currency = DesUtil3.decryptThreeDESECB(transCurrency, DesUtil3.KEY);
			des3ExchangeValue = DesUtil3.decryptThreeDESECB(exchangeValue, DesUtil3.KEY);
			des3AfterTransferAmount = DesUtil3.decryptThreeDESECB(afterTransferAmount, DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}

		// 2. 加密后与传递参数比对
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(des3Type).append(des3ReceiveAccountId)
				.append(des3TransferAmount);
		if (StringUtils.isNotBlank(des3PayPassword)) {
			sb.append(des3PayPassword);
		}
		try {
			signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}

		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}

		// 记录转账汇率
		AccountExchangerate exchangerate = new AccountExchangerate();
		exchangerate.setExchangeratevalue(new BigDecimal(des3ExchangeValue));
		exchangerate.setTransCurrency(des3Trans_currency);
		exchangerate.setOrgCurrency(des3Org_currency);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date string2Date = null;
		try {
			string2Date = sdf.parse(exchageTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		exchangerate.setGmtCreate(string2Date);
		Assert.assertEquals(1, accountExchangeMapper.insertSelective(exchangerate));// 插入汇率进数据库
		Assert.assertNotNull(exchangerate.getId());// ID回写,不为空

		// 根据id获取account:当前账户及对方账户
		String accountSysDtoJson = accountFeignClient.getAccountById(Long.valueOf(accountSysDTO.getAccountId()));
		AccountDTO accountDTO = JsonMapper.fromJsonString(accountSysDtoJson, AccountDTO.class);

		String receiveAccountSysDtoJson = accountFeignClient.getAccountById(Long.valueOf(des3ReceiveAccountId));
		AccountDTO receiveAccountDTO = JsonMapper.fromJsonString(receiveAccountSysDtoJson, AccountDTO.class);

		// 交易金额money与客户的余额进行比对,如果余额小于money,提示
		AccountBalance balanceEntity = accountBalanceMapper
				.selectByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		if (balanceEntity == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.111"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.111"));
			return responseDTO;
		}

		BigDecimal accountBalance = balanceEntity.getBalance();
		// 验证余额是否足够
		int intValue = accountBalance.subtract(new BigDecimal(des3TransferAmount)).intValue();
		if (intValue < 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.115"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.115"));
			return responseDTO;
		}

		// 支付密码校验--依据id查询account中的支付密码(数据库中是加密的密码)
		if (StringUtils.isNotBlank(payPassword)) {
			if (!payPassword.equalsIgnoreCase(accountDTO.getPayPassword())) {
				responseDTO.setSuccess(false);
				responseDTO.setData(data);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.120"));
				responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.120"));
				return responseDTO;
			}
		}

		// 流水编号:当前时间的年月日+uuid;
		Date date = new Date();
		String year = DateUtils.date2String(date, DateFormatUtils.DATE_YEAR);
		String month = DateUtils.date2String(date, DateFormatUtils.DATE_MONTH);
		String day = DateUtils.date2String(date, DateFormatUtils.DATE_DAY);
		StringBuffer buffer = new StringBuffer();
		buffer.append(year).append(month).append(day).append(UUIDUtils.uuid());
		String payNo = buffer.toString();

		// 添加转账记录:当前账户的转账记录
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		accountRecord.setReceiveAccountId(Long.valueOf(des3ReceiveAccountId));
		accountRecord.setTransferMoney(new BigDecimal(des3TransferAmount).negate());
		accountRecord.setCurrencyName(accountDTO.getCurrencyName());
		accountRecord.setType(Integer.valueOf(des3Type));
		accountRecord.setTransferInstructions(
				DictionaryUtil.getString(DictionaryUtil.TransferType, Integer.valueOf(des3Type)));
		accountRecord.setPayNo(payNo);
		accountRecord.setGmtCreate(date);
		accountRecord.setExchangeId(exchangerate.getId());
		if (StringUtils.isNotBlank(remarks)) {
			accountRecord.setRemarks(remarks);
		}
		
		//TODO
		//设置转账方式为余额
		accountRecord.setTransferWay("余额");
		// 此时将基本数据存进数据库,避免代码发生错误时可以分辨是否为预付卡部分接口错误;
		accountRecordMapper.insertSelective(accountRecord);

		// TODO 添加预付卡公司接口
		// 预付卡的接口;-返回预付卡的订单编号,及完成状态;
		// 这部分暂时是假数据,用于测试接口
		//
		Long payAdvanceOrder_NO = new Date().getTime();// 预付卡订单编号
		String payAdvance_isSuccess = "1";// 预付卡的完成状态;
		accountRecord.setPayAdvance_isSuccess(payAdvance_isSuccess);
		accountRecord.setPayAdvanceOrder_NO(payAdvanceOrder_NO);

		if (!"1".equals(payAdvance_isSuccess)) {
			accountRecord.setIsSuccess("0");// 预付卡未完成,订单为未完成状态;
			accountRecordMapper.updateByPrimaryKeySelective(accountRecord);
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.accountTransfer.fail"));
			responseDTO.setData(data);

		} else {
			// 判断完成当前用户的状态,如果是成功的,当前账户余额扣款,并给收款方/对方记录流水
			accountRecord.setIsSuccess("3");// 预付卡完成,订单为完成状态;3
			accountRecord.setPayAdvanceOrder_NO(payAdvanceOrder_NO);
			accountRecordMapper.updateByPrimaryKeySelective(accountRecord);
			// 当前账户扣款
			BigDecimal newAccountBalance = accountBalance.subtract(new BigDecimal(des3TransferAmount));
			balanceEntity.setBalance(newAccountBalance);
			accountBalanceMapper.updateByPrimaryKey(balanceEntity);

			// TODO
			// 购物扣款待确认,目前按照康康设置,不设置收款方记录及余额变更;
			if (!"4".equals(des3Type)) {
				// 更新对方的余额及添加资金记录
				// 对方收钱
				AccountBalance receiveBalance = accountBalanceMapper
						.selectByAccountId(Long.valueOf(des3ReceiveAccountId));// 获取对方的余额实体
				BigDecimal balance2 = receiveBalance.getBalance();
				BigDecimal newBalance2 = balance2.add(new BigDecimal(des3AfterTransferAmount));
				receiveBalance.setBalance(newBalance2);
				accountBalanceMapper.updateByPrimaryKey(receiveBalance);
				// 添加资金记录
				AccountRecord receivAccountRecord = new AccountRecord();
				receivAccountRecord.setAccountId(Long.valueOf(des3ReceiveAccountId));
				receivAccountRecord.setReceiveAccountId(Long.valueOf(accountSysDTO.getAccountId()));
				receivAccountRecord.setTransferInstructions(
						DictionaryUtil.getString(DictionaryUtil.TransferType, Integer.valueOf(des3Type)));
				receivAccountRecord.setType(Integer.valueOf(des3Type));
				receivAccountRecord.setPayNo(payNo);
				receivAccountRecord.setGmtCreate(date);
				receivAccountRecord.setCurrencyName(receiveAccountDTO.getCurrencyName());
				if (StringUtils.isNotBlank(remarks)) {
					receivAccountRecord.setRemarks(remarks);
				}
				// TODO 预付卡的接口状态
				receivAccountRecord.setPayAdvance_isSuccess("1");
				receivAccountRecord.setPayAdvanceOrder_NO(payAdvanceOrder_NO);
				// TODO 汇率接口
				receivAccountRecord.setExchangeId(exchangerate.getId());
				if (des3ExchangeValue != null) {
					receivAccountRecord.setTransferMoney(new BigDecimal(des3AfterTransferAmount));
				}

				receivAccountRecord.setIsSuccess("3");//
				accountRecordMapper.insertSelective(receivAccountRecord);
			}

		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.accountTransfer.success"));
		responseDTO.setData(data);
		return responseDTO;
	}

	public ResponseDTO accountRecharge(String ticket, String transferAmount, Integer clientType, Long accountBankCardId,
			String payPassword, String sign, String addressIP, String type) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> data = Maps.newHashMap();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(type)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("type" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sign" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (accountBankCardId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(
					"accountBankCardId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(payPassword)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("payPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(transferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("transferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String ticketCache = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(ticketCache, AccountSysDTO.class);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证

		// transferAmount及payPassword解密
		String des3PayPassword = null;
		String des3TransferAmount = null;
		try {
			des3PayPassword = DesUtil3.decryptThreeDESECB(payPassword, DesUtil3.KEY);
			des3TransferAmount = DesUtil3.decryptThreeDESECB(transferAmount, DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		BigDecimal des3TransferAmount1 = new BigDecimal(des3TransferAmount);
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(des3PayPassword).append(des3TransferAmount);
		try {
			signStr = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		// 加密验证
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}

		// 支付密码验证
		String accountByIdJson = accountFeignClient.getAccountById(Long.valueOf(accountSysDTO.getAccountId()));
		AccountDTO accountDTO = JsonMapper.fromJsonString(accountByIdJson, AccountDTO.class);
		if (!payPassword.equals(accountDTO.getPayPassword())) {
			responseDTO.setSuccess(false);
			responseDTO.setData(data);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.120"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.120"));
			return responseDTO;
		}

		// TODO
		// 预付卡公司的接口;充值成功后,更改当前余额,并添加记录

		// 记录资金记录:充值不设置汇率
		AccountRecord accountRechargeRecord = new AccountRecord();
		accountRechargeRecord.setAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		accountRechargeRecord.setTransferMoney(des3TransferAmount1);
		accountRechargeRecord.setType(0);
		accountRechargeRecord.setTransferInstructions(DictionaryUtil.getString(DictionaryUtil.TransferType, 0));
		accountRechargeRecord.setPayNo(new Date() + UUIDUtils.uuid());
		accountRechargeRecord.setGmtCreate(new Date());
		// TODO
		accountRechargeRecord.setPayAdvance_isSuccess("1");// 预付卡充值完成;
		accountRechargeRecord.setIsSuccess("1");// 资金记录充值是否成功
		accountRecordMapper.insert(accountRechargeRecord);

		// 余额更改
		AccountBalance balance = accountBalanceMapper.selectByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		if (balance == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.111"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.111"));
			return responseDTO;
		}
		BigDecimal accountBalance = balance.getBalance();
		BigDecimal newAccountBalance = accountBalance.add(des3TransferAmount1);
		balance.setBalance(newAccountBalance);

		int key = accountBalanceMapper.updateByPrimaryKey(balance);
		if (key <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.116"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.116"));
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.recharge.success"));
		responseDTO.setData(data);
		return responseDTO;
	}

	public ResponseDTO checkBalanceAndCounty(String ticket, String transferAmount, Integer clientType, String sign,
			Long receivedAccountId, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (receivedAccountId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(
					"receivedAccountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(transferAmount)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("transferAmount" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String ticketCache = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(ticketCache, AccountSysDTO.class);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		// transferAmount解密
		String des3TransferAmount = null;
		try {
			des3TransferAmount = DesUtil3.decryptThreeDESECB(transferAmount, DesUtil3.KEY);
		} catch (Exception e) {
			// TODO 异常处理
			e.printStackTrace();
		}
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(des3TransferAmount).append(receivedAccountId);
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

		// TODO
		// 判断交易的国籍是否支持:目前设置国外->国内,及国外->国外的设置可以支持
		// 当前账户
		String accountByIdJson = accountFeignClient.getAccountById(Long.valueOf(accountSysDTO.getAccountId()));
		AccountDTO accountDTO = JsonMapper.fromJsonString(accountByIdJson, AccountDTO.class);

		// 对方账户
		String receivedAccountByIdJson = accountFeignClient.getAccountById(receivedAccountId);
		AccountDTO receivedAccountDTO = JsonMapper.fromJsonString(receivedAccountByIdJson, AccountDTO.class);

		if (accountDTO.getCountryName().equals(receivedAccountDTO.getCountryName())
				&& accountDTO.getCountryName().equals("中国")) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.119"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.119"));
			return responseDTO;
		}

		if (!accountDTO.getCountryName().equals(receivedAccountDTO.getCountryName())
				&& accountDTO.getCountryName().equals("中国")) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.119"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.119"));
			return responseDTO;
		}

		// 交易金额money与客户的余额进行比对,如果余额小于money,提示
		AccountBalance balanceEntity = accountBalanceMapper
				.selectByAccountId(Long.valueOf(accountSysDTO.getAccountId()));
		if (balanceEntity == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.111"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.111"));
			return responseDTO;
		}

		BigDecimal accountBalance = balanceEntity.getBalance();
		int intValue = accountBalance.subtract(new BigDecimal(des3TransferAmount)).intValue();// 验证余额是否足够
		if (intValue < 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.115"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.115"));
			return responseDTO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setData(returnData);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.exchange.success"));
		return responseDTO;
	}

	public ResponseDTO last3Record(String ticket, Integer clientType, String sign, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
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

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String ticketCache = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(ticketCache, AccountSysDTO.class);
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

		List<AccountRecord> recordList = accountRecordMapper.findLast3Record(Long.valueOf(ticketCache));
		if (recordList == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.msg.117"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.117"));
			return responseDTO;
		}
		List<AccountRecordDTO> dtoList = new ArrayList<AccountRecordDTO>();
		for (AccountRecord accountRecord : recordList) {
			// 根据id查询当前账户的真实姓名,账号
			String accountById = accountFeignClient.getAccountById(accountRecord.getAccountId());
			AccountDTO accountDTO = JsonMapper.fromJsonString(accountById, AccountDTO.class);
			AccountRecordDTO recordDTO = new AccountRecordDTO();
			recordDTO.setAccountLoginName(accountDTO.getLoginName());
			recordDTO.setAccountHeadUrl(accountDTO.getLoginName());
			recordDTO.setAccountRealName(accountDTO.getRealName());
			dtoList.add(recordDTO);
		}
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("dtoList", dtoList);
		responseDTO.setData(returnData);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.recharge.success"));
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

	public ResponseDTO findRecordById(Integer clientType, String ticket, Long accountRecordId, String sign,
			Integer type, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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
		if (type == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("type" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (accountRecordId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO
					.setMsg("accountRecordId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 根据ticket查询数据库,判断account是否可用
		String ticketCache = accountFeignClient.getAccountByTicket(ticket, clientType);
		AccountSysDTO accountSysDTO = JsonMapper.fromJsonString(ticketCache, AccountSysDTO.class);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(accountRecordId);
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
		AccountRecord accountRecord = accountRecordMapper.selectByPrimaryKey(accountRecordId);
		if (accountRecord == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.125"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.125"));
			return responseDTO;
		}
		AccountRecordDTO recordDTO = new AccountRecordDTO();
		Date gmtCreate = accountRecord.getGmtCreate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatTime = sdf.format(gmtCreate);
		recordDTO.setCreatetTime(formatTime);
		recordDTO.setAccountRecordId(accountRecord.getId());
		recordDTO.setPayNo(accountRecord.getPayNo());
		recordDTO.setTransferMoney(accountRecord.getTransferMoney());
		recordDTO.setType(type);
		recordDTO.setRemarks(accountRecord.getRemarks());
		recordDTO.setTransferState(
				DictionaryUtil.getString(DictionaryUtil.payStatus, Integer.valueOf(accountRecord.getIsSuccess())));
		recordDTO.setTransferInstructions(accountRecord.getTransferInstructions());
		if (type == 1 || type == 2 || type == 4 || type == 5) {
			Long accountId = accountRecord.getAccountId();
			String accountById = accountFeignClient.getAccountById(accountId);
			AccountDTO accountDTO = JsonMapper.fromJsonString(accountById, AccountDTO.class);
			recordDTO.setAccountHeadUrl(accountDTO.getHeadUrl());
			recordDTO.setAccountLoginName(accountDTO.getLoginName());
			recordDTO.setAccountRealName(accountDTO.getRealName());
			Long receivedAccountId = accountRecord.getReceiveAccountId();
			String receivedAccountById = accountFeignClient.getAccountById(receivedAccountId);
			AccountDTO receivedAccountDTO = JsonMapper.fromJsonString(receivedAccountById, AccountDTO.class);
			recordDTO.setReceiveAccountLoginName(receivedAccountDTO.getLoginName());
			recordDTO.setReceiveAccountRealName(receivedAccountDTO.getRealName());
			// 收款方式设置定值
			recordDTO.setTransferWay("余额");
			recordDTO.setTransferMoney(accountRecord.getTransferMoney());
		} else if (type == 0 || type == 7) {
			Long blankCardId = accountRecord.getBlankCardId();
			AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(blankCardId);
			String bankCardNO = bankCard.getBankCard();
			String subStringBankCardNo = bankCardNO.substring(bankCardNO.length() - 4);
			StringBuffer buffer = new StringBuffer();
			buffer.append(bankCard.getBankName()).append("(").append(subStringBankCardNo).append(")");
			recordDTO.setTransferWay(buffer.toString());
			Long accountId = accountRecord.getAccountId();
			String accountById = accountFeignClient.getAccountById(accountId);
			AccountDTO accountDTO = JsonMapper.fromJsonString(accountById, AccountDTO.class);
			recordDTO.setAccountHeadUrl(accountDTO.getHeadUrl());
			recordDTO.setAccountLoginName(accountDTO.getLoginName());
			recordDTO.setAccountRealName(accountDTO.getRealName());
			recordDTO.setTransferMoney(accountRecord.getTransferMoney());

		} else if (type == 3 || type == 6) {
			// 提现:银行卡为基准.money为+
			Long receivedBlankCardId = accountRecord.getReceiveCardId();
			AccountBankCard bankCard = accountBankCardMapper.selectByPrimaryKey(receivedBlankCardId);
			String bankCardNO = bankCard.getBankCard();
			String subStringBankCardNo = bankCardNO.substring(bankCardNO.length() - 4);
			StringBuffer buffer = new StringBuffer();
			String bankName = bankCard.getBankName();
			buffer.append(bankName).append("(").append(subStringBankCardNo).append(")");
			recordDTO.setTransferWay(buffer.toString());
			recordDTO.setTransferMoney(accountRecord.getTransferMoney().negate());
			recordDTO.setBankName(bankName);
			recordDTO.setBankImage(bankCard.getBankImage());
		}
		returnData.put("recordDTO", recordDTO);
		responseDTO.setData(returnData);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.getRecordInfo.success"));
		return responseDTO;
	}

}
