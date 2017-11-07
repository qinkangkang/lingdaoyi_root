package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.encrypt.MD5Util2;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountCountry;
import com.lingdaoyi.cloud.entity.account.AccountInfo;
import com.lingdaoyi.cloud.entity.account.AccountTicket;
import com.lingdaoyi.cloud.entity.sys.SysSms;
import com.lingdaoyi.cloud.feign.TransferFeignClient;
import com.lingdaoyi.cloud.mapper.AccountCountryMapper;
import com.lingdaoyi.cloud.mapper.AccountInfoMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.mapper.AccountTicketMapper;
import com.lingdaoyi.cloud.mapper.SysSmsMapper;
import com.lingdaoyi.cloud.utils.UUIDUtils;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;
import com.lingdaoyi.cloud.utils.sms.SmsResult;
import com.lingdaoyi.cloud.utils.sms.SmsUtil;

@Service
@Transactional
public class AccountLoginService {

	public static final long Control = 60L;

	@Autowired
	private SysSmsMapper sysSmsMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private AccountTicketMapper accountTicketMapper;

	@Autowired
	private AccountCountryMapper accountCountryMapper;

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private EmcService emcService;

	@Autowired
	private TransferFeignClient transferFeignClient;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO sendAccountLoginSms(Integer clientType, String accountPhone, String addressIP, Integer type,
			String sign) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(accountPhone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("accountPhone" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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
		sb.append(clientType).append(addressIP).append(accountPhone).append(type);
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

		Date date = new Date();
		String checkCode = null;
		if (type != null && SmsUtil.CheckCodeLoginSms == type) {
			// 通过电话号码查询，判断调用接口是否小于60秒
			List<Date> findByPhoneList = sysSmsMapper.findByPhone(accountPhone, SmsUtil.CheckCodeLoginSms);
			if (findByPhoneList != null && findByPhoneList.size() > 0) {
				for (Date date1 : findByPhoneList) {
					BigDecimal bd1 = new BigDecimal(date.getTime() / 1000);
					BigDecimal bd2 = new BigDecimal(date1.getTime() / 1000);
					BigDecimal subtract = bd1.subtract(bd2);
					int intValue = subtract.intValue();
					if (intValue < Control) {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.108"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.108"));
						return responseDTO;
					}
				}
			}
			if (SmsUtil.isCheckCodeLoginSwitch()) {
				checkCode = RandomStringUtils.randomNumeric(6);
				// 用redis缓存验证码
				RedisUtils.putCache(RedisMoudel.SmsLoginCheckCodeCacheKey, accountPhone, checkCode);

				// TODO发送短信验证码
				Map<String, String> smsParamMap = Maps.newHashMap();
				smsParamMap.put("chackCode", checkCode);
				smsParamMap.put("minute", "在3");
				// 发送确认码短信，短信类型码为(1.快捷登陆短信验证码)
				SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeLoginSms, accountPhone, smsParamMap);

				SysSms sysSms = new SysSms();
				sysSms.setSendPhone(accountPhone);
				sysSms.setSendTime(new Date());
				sysSms.setSmsContent(smsResult.getContent());
				sysSms.setSmsType(SmsUtil.CheckCodeLoginSms);
				sysSms.setSendResponse(smsResult.getResponse());
				if (smsResult.isSuccess()) {
					sysSms.setSendSuccess(1);
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.success"));
				} else {
					sysSms.setSendSuccess(0);
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.fail"));
				}
				sysSms.setGmtCreate(date);
				sysSmsMapper.insert(sysSms);
			} else {
				RedisUtils.putCache(RedisMoudel.SmsLoginCheckCodeCacheKey, accountPhone, SmsUtil.isSms);
				responseDTO.setMsg("短信网关已关闭,请使用暗号登陆！");
			}
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.success"));
		} else if (type != null && SmsUtil.CheckCodeRegisterSms == type) {
			// 通过电话号码查询，判断调用接口是否小于60秒
			List<Date> findByPhoneList = sysSmsMapper.findByPhone(accountPhone, SmsUtil.CheckCodeRegisterSms);
			if (findByPhoneList != null && findByPhoneList.size() > 0) {
				for (Date date1 : findByPhoneList) {
					BigDecimal bd1 = new BigDecimal(date.getTime() / 1000);
					BigDecimal bd2 = new BigDecimal(date1.getTime() / 1000);
					BigDecimal subtract = bd1.subtract(bd2);
					int intValue = subtract.intValue();
					if (intValue < Control) {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.108"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.108"));
						return responseDTO;
					}
				}
			}

			if (SmsUtil.isCheckCodeRegisterSwitch()) {
				checkCode = RandomStringUtils.randomNumeric(6);
				// 用redis缓存注册验证码
				RedisUtils.putCache(RedisMoudel.SmsRegisterCheckCodeCacheKey, accountPhone, checkCode);

				// TODO发送短信验证码
				Map<String, String> smsParamMap = Maps.newHashMap();
				smsParamMap.put("chackCode", checkCode);
				smsParamMap.put("minute", "在3");
				// 发送确认码短信，短信类型码为(2.为注册短信验证码)
				SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeRegisterSms, accountPhone, smsParamMap);

				SysSms sysSms = new SysSms();
				sysSms.setSendPhone(accountPhone);
				sysSms.setSendTime(new Date());
				sysSms.setSmsContent(smsResult.getContent());
				sysSms.setSmsType(SmsUtil.CheckCodeRegisterSms);
				sysSms.setSendResponse(smsResult.getResponse());
				if (smsResult.isSuccess()) {
					sysSms.setSendSuccess(1);
				} else {
					sysSms.setSendSuccess(0);
				}
				sysSms.setGmtCreate(date);
				sysSmsMapper.insert(sysSms);
			} else {
				RedisUtils.putCache(RedisMoudel.SmsRegisterCheckCodeCacheKey, accountPhone, SmsUtil.isSms);
				responseDTO.setMsg("短信网关已关闭,请使用暗号登陆！");
			}

			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.account.sms.uloginpwd.success"));
		} else if (type != null && SmsUtil.CheckCodeModifyloginPwdSms == type) {
			// 通过电话号码查询，判断调用接口是否小于60秒
			List<Date> findByPhoneList = sysSmsMapper.findByPhone(accountPhone, SmsUtil.CheckCodeModifyloginPwdSms);
			if (findByPhoneList != null && findByPhoneList.size() > 0) {
				for (Date date1 : findByPhoneList) {
					BigDecimal bd1 = new BigDecimal(date.getTime() / 1000);
					BigDecimal bd2 = new BigDecimal(date1.getTime() / 1000);
					BigDecimal subtract = bd1.subtract(bd2);
					int intValue = subtract.intValue();
					if (intValue < Control) {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.108"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.108"));
						return responseDTO;
					}
				}
			}

			if (SmsUtil.isCheckCodeModifyLoginPwdSwitch()) {
				checkCode = RandomStringUtils.randomNumeric(6);
				// 用redis缓存注册验证码
				RedisUtils.putCache(RedisMoudel.SmsModifyLoginPwdCheckCodeCacheKey, accountPhone, checkCode);

				// TODO发送短信验证码
				Map<String, String> smsParamMap = Maps.newHashMap();
				smsParamMap.put("chackCode", checkCode);
				smsParamMap.put("minute", "在3");
				// 发送确认码短信，短信类型码为(2.为注册短信验证码)
				SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeModifyloginPwdSms, accountPhone, smsParamMap);

				SysSms sysSms = new SysSms();
				sysSms.setSendPhone(accountPhone);
				sysSms.setSendTime(new Date());
				sysSms.setSmsContent(smsResult.getContent());
				sysSms.setSmsType(SmsUtil.CheckCodeModifyloginPwdSms);
				sysSms.setSendResponse(smsResult.getResponse());
				if (smsResult.isSuccess()) {
					sysSms.setSendSuccess(1);
				} else {
					sysSms.setSendSuccess(0);
				}
				sysSms.setGmtCreate(date);
				sysSmsMapper.insert(sysSms);

			} else {
				RedisUtils.putCache(RedisMoudel.SmsModifyLoginPwdCheckCodeCacheKey, accountPhone, SmsUtil.isSms);
				responseDTO.setMsg("短信网关已关闭,请使用暗号登陆！");
			}
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.account.sms.upaypwd.success"));
		} else if (type != null && SmsUtil.CheckCodeModifyPayPwdSms == type) {

			// 通过电话号码查询，判断调用接口是否小于60秒
			List<Date> findByPhoneList = sysSmsMapper.findByPhone(accountPhone, SmsUtil.CheckCodeModifyPayPwdSms);
			if (findByPhoneList != null && findByPhoneList.size() > 0) {
				for (Date date1 : findByPhoneList) {
					BigDecimal bd1 = new BigDecimal(date.getTime() / 1000);
					BigDecimal bd2 = new BigDecimal(date1.getTime() / 1000);
					BigDecimal subtract = bd1.subtract(bd2);
					int intValue = subtract.intValue();
					if (intValue < Control) {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.108"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.108"));
						return responseDTO;
					}
				}
			}

			if (SmsUtil.isCheckCodeModifyPayPwdSwitch()) {
				checkCode = RandomStringUtils.randomNumeric(6);
				// 用redis缓存注册验证码
				RedisUtils.putCache(RedisMoudel.SmsModifyPayPwdCheckCodeCacheKey, accountPhone, checkCode);

				// TODO发送短信验证码
				Map<String, String> smsParamMap = Maps.newHashMap();
				smsParamMap.put("chackCode", checkCode);
				smsParamMap.put("minute", "在3");
				// 发送确认码短信，短信类型码为(2.为注册短信验证码)
				SmsResult smsResult = SmsUtil.sendSms(SmsUtil.CheckCodeModifyPayPwdSms, accountPhone, smsParamMap);

				SysSms sysSms = new SysSms();
				sysSms.setSendPhone(accountPhone);
				sysSms.setSendTime(new Date());
				sysSms.setSmsContent(smsResult.getContent());
				sysSms.setSmsType(SmsUtil.CheckCodeModifyPayPwdSms);
				sysSms.setSendResponse(smsResult.getResponse());
				if (smsResult.isSuccess()) {
					sysSms.setSendSuccess(1);
				} else {
					sysSms.setSendSuccess(0);
				}
				sysSms.setGmtCreate(date);
				sysSmsMapper.insert(sysSms);

			} else {
				RedisUtils.putCache(RedisMoudel.SmsModifyPayPwdCheckCodeCacheKey, accountPhone, SmsUtil.isSms);
				responseDTO.setMsg("短信网关已关闭,请使用暗号登陆！");
			}
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.account.sms.upaypwd.success"));

		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		return responseDTO;
	}

	/**
	 * 
	 * 电子钱包 快捷登陆接口
	 * 
	 * @param clientType
	 * @param checkCode
	 * @param accountPhone
	 * @param addressIP
	 * @param deviceId
	 * @param deviceTokens
	 * @param request
	 * @return
	 */
	public ResponseDTO quickLogin(Integer clientType, String checkCode, String accountPhone, String sign,
			String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(accountPhone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(checkCode).append(accountPhone);
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

		// 读取redis里面的验证码缓存拿出来做对比
		try {
			String checkCacheCode = RedisUtils.getValue(accountPhone, RedisMoudel.SmsLoginCheckCodeCacheKey);
			if (checkCacheCode.equals(checkCode)) {
				RedisUtils.removeCache(RedisMoudel.SmsLoginCheckCodeCacheKey, accountPhone);
			} else {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.106"));
				responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.106"));
				return responseDTO;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;

		Account account = accountMapper.findByLoginName(accountPhone);

		Date now = new Date();
		if (account == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.102"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.102"));
			return responseDTO;
		} else if (account.getStatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.103"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.103"));
			return responseDTO;
		} else if (account.getStatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.104"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.104"));
			return responseDTO;
		} else {
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			AccountTicket accountTicket = accountTicketMapper.findTicketByAccountId(account.getId());
			if (accountTicket == null) {
				accountTicket = new AccountTicket();
				accountTicket.setAccountId(account.getId());
				accountTicket.setType(1);
				accountTicket.setGmtCreate(now);
				accountTicket.setTicket(ticket);
				accountTicketMapper.insert(accountTicket);
			} else {
				oldTicket = accountTicket.getTicket();
			}
			accountTicket.setTicket(ticket);
			accountTicket.setGmtModified(now);
			accountTicketMapper.updateByPrimaryKey(accountTicket);
		}

		// 增加 ticket redis 緩存
		RedisUtils.putCache(RedisMoudel.TicketToId, ticket, String.valueOf(account.getId()));
		if (StringUtils.isNotBlank(oldTicket)) {
			RedisUtils.removeCache(RedisMoudel.TicketToId, oldTicket);
		}

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(account.getId());
		accountDTO.setLoginName(account.getLoginName());
		if (account.getIsAuth() == 0) {
			accountDTO.setAuth(false);
		} else {
			accountDTO.setAuth(true);
		}
		accountDTO.setHeadUrl(account.getHeadUrl());
		accountDTO.setTicket(ticket);
		// 用户真实信息
		accountDTO.setRealName(account.getRealName());
		accountDTO.setCountryId(account.getCountryId());
		accountDTO.setCountryName(account.getCountryName());
		accountDTO.setCountryName(account.getCurrencyName());
		accountDTO.setEmail(account.getEmail());
		accountDTO.setLaborUrl(account.getLaborUrl());
		// 用户账号关键信息
		accountDTO.setAccountLevel(account.getAccountLevel());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountDTO", accountDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login"));
		return responseDTO;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO cheakCode(Integer clientType, String accountPhone, String checkCode, String addressIP,
			Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(accountPhone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("accountPhone" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(checkCode)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("checkCode" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 读取redis里面的验证码缓存拿出来做对比
		if (type != null && SmsUtil.CheckCodeRegisterSms == type) {
			try {
				String checkCacheCode = RedisUtils.getValue(accountPhone, RedisMoudel.SmsRegisterCheckCodeCacheKey);
				if (checkCacheCode != null) {
					if (checkCacheCode.equals(checkCode)) {
						RedisUtils.removeCache(RedisMoudel.SmsRegisterCheckCodeCacheKey, accountPhone);
						responseDTO.setMsg(
								PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.check.success"));
					} else {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.106"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.106"));
						return responseDTO;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (type != null && SmsUtil.CheckCodeModifyloginPwdSms == type) {
			try {
				String checkCacheCode = RedisUtils.getValue(accountPhone,
						RedisMoudel.SmsModifyLoginPwdCheckCodeCacheKey);
				if (checkCacheCode.equals(checkCode)) {
					RedisUtils.removeCache(RedisMoudel.SmsModifyLoginPwdCheckCodeCacheKey, accountPhone);
					responseDTO
							.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.check.success"));
				} else {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.106"));
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.106"));
					return responseDTO;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (type != null && SmsUtil.CheckCodeModifyPayPwdSms == type) {
			try {
				String checkCacheCode = RedisUtils.getValue(accountPhone, RedisMoudel.SmsModifyPayPwdCheckCodeCacheKey);
				if (checkCacheCode.equals(checkCode)) {
					RedisUtils.removeCache(RedisMoudel.SmsModifyPayPwdCheckCodeCacheKey, accountPhone);
					responseDTO
							.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.sms.check.success"));
				} else {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.106"));
					responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.106"));
					return responseDTO;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		return responseDTO;
	}

	/**
	 * 
	 * 电子钱包 账号密码登陆接口
	 * 
	 * @param clientType
	 * @param checkCode
	 * @param accountPhone
	 * @param addressIP
	 * @param deviceId
	 * @param deviceTokens
	 * @param request
	 * @return
	 */
	public ResponseDTO accountPasswordLogin(Integer clientType, String loginName, String loginPassword, String sign,
			String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(loginName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginName" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(loginPassword)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(loginName).append(loginPassword);
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

		Account account = accountMapper.findByLoginName(loginName);

		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;

		Date now = new Date();
		if (account == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.102"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.102"));
			return responseDTO;
		} else if (account.getStatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.103"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.103"));
			return responseDTO;
		} else if (account.getStatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.104"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.104"));
			return responseDTO;
		} else {
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			AccountTicket accountTicket = accountTicketMapper.findTicketByAccountId(account.getId());
			if (accountTicket == null) {
				accountTicket = new AccountTicket();
				accountTicket.setAccountId(account.getId());
				accountTicket.setType(1);
				accountTicket.setGmtCreate(now);
				accountTicket.setTicket(ticket);
				accountTicketMapper.insert(accountTicket);
			} else {
				oldTicket = accountTicket.getTicket();
			}
			accountTicket.setTicket(ticket);
			accountTicket.setGmtModified(now);
			accountTicketMapper.updateByPrimaryKey(accountTicket);
		}

		// 调用加密工具类 对比加密后的账号与密码与数据库是否一致，如果一致则通过验证。
		String desPassword = null;
		try {
			desPassword = MD5Util2.md5Encode(loginPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!desPassword.equals(account.getLoginPassword())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.105"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.105"));
			return responseDTO;
		}

		// 增加 redis 緩存
		RedisUtils.putCache(RedisMoudel.TicketToId, ticket, String.valueOf(account.getId()));
		if (StringUtils.isNotBlank(oldTicket)) {
			RedisUtils.removeCache(RedisMoudel.TicketToId, oldTicket);
		}

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(account.getId());
		accountDTO.setLoginName(account.getLoginName());
		if (account.getIsAuth() == 0) {
			accountDTO.setAuth(false);
		} else {
			accountDTO.setAuth(true);
		}
		accountDTO.setHeadUrl(account.getHeadUrl());
		accountDTO.setTicket(ticket);
		// 用户真实信息
		accountDTO.setRealName(account.getRealName());
		accountDTO.setCountryId(account.getCountryId());
		accountDTO.setCountryName(account.getCountryName());
		accountDTO.setCountryName(account.getCurrencyName());
		accountDTO.setEmail(account.getEmail());
		accountDTO.setLaborUrl(account.getLaborUrl());
		// 用户账号关键信息
		accountDTO.setAccountLevel(account.getAccountLevel());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountDTO", accountDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login"));
		return responseDTO;
	}

	public ResponseDTO registerAccount(Integer clientType, String loginName, Long countryId, String loginPassword,
			String deviceId, String deviceTokens, String sign, String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (countryId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("countryId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(loginName)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginName" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (StringUtils.isBlank(loginPassword)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("loginPassword" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		// 加密参数验证
		String signStr = null;
		StringBuffer sb = new StringBuffer();
		sb.append(clientType).append(addressIP).append(loginName).append(countryId).append(loginPassword)
				.append(deviceId).append(deviceTokens);
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

		Account account = accountMapper.findByLoginName(loginName);
		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		if (account == null) {
			// 加密密码
			String enPassword = null;
			try {
				enPassword = MD5Util2.md5Encode(loginPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 以下为账号敏感信息
			account = new Account();
			account.setLoginName(loginName);
			account.setCountryId(countryId);
			account.setLoginPassword(enPassword);
			// 以下为账号基础信息
			account.setHeadUrl(
					new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl", PropertiesUtil.systemConfig))
							.append(PropertiesUtil.getProperty("system.imageRootPath", PropertiesUtil.systemConfig))
							.append("/emc/noHeadPic.jpg").toString());
			account.setIsAuth(0);
			account.setCountryId(countryId);
			AccountCountry accountCountry = accountCountryMapper.findByAccountCountryId(countryId);
			account.setCountryName(accountCountry.getCountryname());
			account.setCurrencyName(accountCountry.getCurrencyname());
			account.setAccountLevel(0);
			account.setTransferkey("");
			account.setFingerprintPay(0);
			account.setAccountType(1);
			account.setStatus(0);
			account.setGmtCreate(now);
			account.setIsDeleted(0);
			account.setClientType(clientType);
			accountMapper.insert(account);
			account.getAccountLevel();
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			AccountTicket accountTicket = accountTicketMapper.findTicketByAccountId(account.getId());
			if (accountTicket == null) {
				accountTicket = new AccountTicket();
				accountTicket.setAccountId(account.getId());
				accountTicket.setType(1);
				accountTicket.setGmtCreate(now);
				accountTicket.setTicket(ticket);
				accountTicketMapper.insert(accountTicket);

				// 增加 redis 緩存
				RedisUtils.putCache(RedisMoudel.TicketToId, ticket, String.valueOf(account.getId()));
			}
			// 创建账户余额信息
			try {
				if (account.getId() != null) {
					transferFeignClient.createAccountBalance(account.getId(),accountCountry.getCurrencysign());
				}
			} catch (Exception e1) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
				responseDTO.setMsg("创建账户余额信息失败！");
				return responseDTO;
			}

			// 创建账户Info信息
			AccountInfo accountInfo = new AccountInfo();
			accountInfo.setAccountId(account.getId());
			accountInfo.setLoginName(loginName);
			accountInfo.setHeadUrl(account.getHeadUrl());
			// 生成二维码图片
			try {
				accountInfo.setQrCode(UUIDUtils.uuid() + ":" + DesUtil3.encryptThreeDESECB(loginName, DesUtil3.KEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
			accountInfo.setCountryName(account.getCountryName());
			accountInfo.setDeviceId(deviceId);
			accountInfo.setDeviceTokens(deviceTokens);
			accountInfo.setGmtCreate(now);
			accountInfo.setIsDeleted(0);
			accountInfoMapper.insert(accountInfo);
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.101"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.101"));
			return responseDTO;
		}

		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(account.getId());
		accountDTO.setLoginName(account.getLoginName());
		if (account.getIsAuth() == 0) {
			accountDTO.setAuth(false);
		} else {
			accountDTO.setAuth(true);
		}
		accountDTO.setHeadUrl(account.getHeadUrl());
		accountDTO.setTicket(ticket);
		// 用户真实信息
		accountDTO.setRealName(account.getRealName());
		accountDTO.setCountryId(account.getCountryId());
		accountDTO.setCountryName(account.getCountryName());
		accountDTO.setCountryName(account.getCurrencyName());
		accountDTO.setEmail(account.getEmail());
		accountDTO.setLaborUrl(account.getLaborUrl());
		// 用户账号关键信息
		accountDTO.setAccountLevel(account.getAccountLevel());

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("accountDTO", accountDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.register"));
		return responseDTO;
	}

	public ResponseDTO logout(Integer clientType, String ticket, String addressIP) {
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

		accountTicketMapper.clearTicket(Long.valueOf(accountSysDTO.getAccountId()), clientType);
		RedisUtils.removeCache(RedisMoudel.TicketToId, ticket);
		// 清除用户缓存数据
		RedisUtils.removeCache(RedisMoudel.AccountEentity, accountSysDTO.getAccountId());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout"));
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
