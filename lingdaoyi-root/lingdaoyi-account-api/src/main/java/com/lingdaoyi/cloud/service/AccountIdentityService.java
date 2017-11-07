package com.lingdaoyi.cloud.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountIdentityDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.account.Account;
import com.lingdaoyi.cloud.entity.account.AccountIdentity;
import com.lingdaoyi.cloud.entity.account.AccountInfo;
import com.lingdaoyi.cloud.mapper.AccountIdentityMapper;
import com.lingdaoyi.cloud.mapper.AccountInfoMapper;
import com.lingdaoyi.cloud.mapper.AccountMapper;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class AccountIdentityService {

	@Autowired
	private AccountIdentityMapper accountIdentityMapper;

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private EmcService emcService;

	/**
	 * 检测用户认证信息是否存在
	 * 
	 * @param accountId
	 * @return
	 */
	public ResponseDTO checkByAcconId(String accountId) {
		ResponseDTO dto = null;
		if (StringUtils.isBlank(accountId)) {
			dto = new ResponseDTO();
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setMsg("accountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			dto.setSuccess(false);
			return dto;
		}
		AccountIdentity selectByAccontId = accountIdentityMapper.selectByAccontId(Long.parseLong(accountId));
		if (selectByAccontId != null && selectByAccontId.getId() > 0) {
			dto = new ResponseDTO();
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
			dto.setMsg("该用户已有实名认证记录，不可重复认证");
			dto.setSuccess(false);
			return dto;
		}
		return dto;
	}

	public ResponseDTO saveIdentityInfo(Long accountId, String idcard, String realname, String area, String sex,
			String enkey, String ticket, Integer clientType) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		if (accountId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("accountId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		Account account = accountMapper.selectByPrimaryKey(accountId);
		AccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(accountId);
		if (account != null) {
			String decryptrealname = "";
			try {
				decryptrealname = DesUtil3.decryptThreeDESECB(realname, enkey);
			} catch (Exception e) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				responseDTO.setMsg("realname" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				return responseDTO;
			}
			account.setRealName(decryptrealname);
			account.setIsAuth(1);// 1表示认证
			account.setGmtModified(new Date());
			accountMapper.updateByPrimaryKeySelective(account);
		}
		if (accountInfo != null) {
			accountInfo.setGender(sex);
			accountInfo.setGmtModified(new Date());
			accountInfoMapper.updateByPrimaryKeySelective(accountInfo);
		}

		AccountIdentity accountIdentity = new AccountIdentity();
		accountIdentity.setAccountId(accountId);
		accountIdentity.setAddress(area);
		accountIdentity.setCertificateNo(idcard);
		accountIdentity.setCertificateType(1);// 1 身份证
		accountIdentity.setIsDeleted(0);
		accountIdentity.setRealName(realname);
		accountIdentity.setGmtCreate(new Date());
		accountIdentity.setEnkey(enkey);
		accountIdentityMapper.insert(accountIdentity);

		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.aidentity.success"));
		return responseDTO;
	}

	public ResponseDTO viewAccountAuth(String ticket, Integer clientType) {

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

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		AccountIdentityDTO accountIdentityDTO = null;
		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		AccountIdentity accountIdentity = accountIdentityMapper
				.selectByAccontId(Long.valueOf(accountSysDTO.getAccountId()));
		if (account != null) {
			accountIdentityDTO = new AccountIdentityDTO();

			if (account.getHeadUrl() == null) {
				accountIdentityDTO.setHeadUrl(account.getHeadUrl());
			}

			if (account.getIsAuth() == 0) {
				accountIdentityDTO.setAuth(false);
			} else {
				accountIdentityDTO.setAuth(true);
			}
			if (account.getLaborUrl() == null) {
				accountIdentityDTO.setLaborContract(false);
			} else {
				accountIdentityDTO.setLaborContract(true);
			}

			if (account.getEmail() == null) {
				accountIdentityDTO.setEmailStatus(false);
			} else {
				accountIdentityDTO.setEmailStatus(true);
			}

			if (account.getLaborUrl() == null) {
				accountIdentityDTO.setLaborContract(false);
			} else {
				accountIdentityDTO.setLaborContract(true);
			}
			accountIdentityDTO.setCountryName(account.getCountryName());

			if (accountIdentity != null) {

				String certificateNo = null;
				String realName = null;

				try {
					if (accountIdentity.getCertificateNo() != null) {
						certificateNo = DesUtil3.decryptThreeDESECB(accountIdentity.getCertificateNo(),
								accountIdentity.getEnkey());
					}
					if (accountIdentity.getRealName() != null) {
						realName = DesUtil3.decryptThreeDESECB(accountIdentity.getRealName(),
								accountIdentity.getEnkey());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				accountIdentityDTO.setRealName(realName);
				if (certificateNo != null) {
					char first = certificateNo.charAt(0);
					char last = certificateNo.charAt(certificateNo.length() - 1);
					String.valueOf(first);
					String IdentityNo = String.valueOf(first) + "***********" + String.valueOf(last);
					accountIdentityDTO.setCertificateNo(IdentityNo);
				}

				if (accountIdentity.getAuditStatus() != null) {
					if (accountIdentity.getAuditStatus() == 1) {
						accountIdentityDTO.setIdPhoto(true);
					} else {
						accountIdentityDTO.setIdPhoto(false);
					}
				}

				if (accountIdentity.getCertificateType() == 0) {
					accountIdentityDTO.setCertificateType("身份证");
				}
				accountIdentityDTO.setValidityPeriod(accountIdentity.getEndDate());
			}

		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("accountIdentity", accountIdentityDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.sidentity.success"));
		return responseDTO;
	}

	public ResponseDTO addIdentityInfo(String name, String num, String sex, String address, String birth,
			String startDate, String nationality, String endDate, String issue, String email, String laborUrl,
			String ticket, Integer clientType) {
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

		Account account = accountMapper.selectByPrimaryKey(Long.valueOf(accountSysDTO.getAccountId()));
		AccountIdentity accountIdentity = accountIdentityMapper
				.selectByAccontId(Long.valueOf(accountSysDTO.getAccountId()));

		String iDnumber = null;
		String rname = null;
		if (accountIdentity != null) {
			try {
				if (StringUtils.isNotBlank(num)) {
					iDnumber = DesUtil3.encryptThreeDESECB(num, accountIdentity.getEnkey());
				}
				if (StringUtils.isNotBlank(name)) {
					rname = DesUtil3.encryptThreeDESECB(name, accountIdentity.getEnkey());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (StringUtils.isNoneBlank(accountIdentity.getRealName())
					&& StringUtils.isNoneBlank(accountIdentity.getCertificateNo())) {

				if (StringUtils.isNotBlank(iDnumber) && StringUtils.isNotBlank(rname)) {
					if (!rname.equals(accountIdentity.getRealName())
							&& !iDnumber.equals(accountIdentity.getCertificateNo())) {
						responseDTO.setSuccess(false);
						responseDTO
								.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.122"));
						responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.msg.122"));
						return responseDTO;
					}
				}
			}
		}
		if (account != null) {
			account.setEmail(email);
			account.setLaborUrl(laborUrl);
			accountMapper.updateByPrimaryKeySelective(account);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		if (accountIdentity != null) {

			accountIdentity.setAddress(address);
			try {
				if (StringUtils.isNotBlank(birth)) {
					accountIdentity.setBirth(df.parse(birth));
				}
				if (StringUtils.isNotBlank(startDate)) {
					accountIdentity.setStartDate(df.parse(startDate));
				}
				if (StringUtils.isNotBlank(endDate)) {
					accountIdentity.setEndDate(df.parse(endDate));
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			accountIdentity.setGmtModified(new Date());
			accountIdentity.setIssue(issue);
			accountIdentity.setNationality(nationality);
			accountIdentity.setSex(sex);
			accountIdentityMapper.updateByPrimaryKeySelective(accountIdentity);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.pidentity.success"));
		return responseDTO;
	}

	public ResponseDTO updatePayPwdAuth(String realName, String num, String ticket, Integer clientType,
			String requestIp, String sign) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (StringUtils.isBlank(num)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("num" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
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
		sb.append(clientType).append(requestIp).append(realName).append(num);
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

		AccountSysDTO accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		if (!accountSysDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(accountSysDTO.getStatusCode());
			responseDTO.setMsg(accountSysDTO.getMsg());
			return responseDTO;
		}

		AccountIdentity accountIdentity = accountIdentityMapper
				.selectByAccontId(Long.valueOf(accountSysDTO.getAccountId()));

		if (accountIdentity == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.121"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.121"));
			return responseDTO;
		}
		
		Map<String, Object> returnData = Maps.newHashMap();
		if (realName.equals(accountIdentity.getRealName().toString())
				&& num.equals(accountIdentity.getCertificateNo().toString())) {
			returnData.put("authInfo", true);
		} else {
			returnData.put("authInfo", false);
		}
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg("认证结束！");
		return responseDTO;

	}

}
