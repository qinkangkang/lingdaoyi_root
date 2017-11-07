package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.AccountDTO;

import com.lingdaoyi.cloud.service.AccountInfoService;
import com.lingdaoyi.cloud.service.AccountLevelService;
import com.lingdaoyi.cloud.service.AccountService;
import com.lingdaoyi.cloud.service.EmcService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.image.ImageMoudel;

@Configuration
@Controller
@SpringBootApplication
@RequestMapping("/account")
public class AccountController {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static final Logger logger = LoggerFactory.getLogger(AccountLoginController.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountLevelService accountLevelService;

	@Autowired
	private AccountInfoService accountInfoService;

	@Autowired
	private EmcService emcService;

	/**
	 * 账户信息
	 */
	@ResponseBody
	@PostMapping(value = "/viewAccount")
	public String viewAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.viewAccount(ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 账户个人信息
	 */
	@ResponseBody
	@PostMapping(value = "/viewAccountInfo")
	public String viewAccountInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.viewAccountInfo(ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.select.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 修改账户个人信息
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccountInfo")
	public String updateAccountInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "career", required = false) String career,
			@RequestParam(value = "income", required = false) String income,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountInfoService.updateAccountInfo(nickname, income, career, ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.accountInfo.update.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 修改账户信息
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccount")
	public String updateAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "headUrl", required = false) String headUrl,
			@RequestParam(value = "isAuth", required = false) Integer isAuth,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "accountLevel", required = false) Integer accountLevel,
			@RequestParam(value = "fingerprintPay", required = false) Integer fingerprintPay,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.updateAccount(loginName, headUrl, isAuth, email, accountLevel,
					fingerprintPay, status, ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.update.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 修改账户登录密码
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccountLoginPwd")
	public String updateAccountLoginPwd(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "loginPassword", required = false) String loginPassword,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.updateAccountLoginPwd(clientType, loginPassword, ticket, IpUtil.getRequestIp(),
					sign);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.update.lpwd.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 修改账户支付密码
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccountPayPwd")
	public String updateAccountPayPwd(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "payPassword", required = false) String payPassword,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.updateAccountPayPwd(clientType, payPassword, ticket, IpUtil.getRequestIp(),
					sign);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.update.ppwd.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 根据账户ID查询账户
	 */
	@ResponseBody
	@PostMapping(value = "/getAccountById")
	public String getAccountById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = false) Long accountId) {
		AccountDTO accountDTO = accountService.getAccountById(accountId);
		return mapper.toJson(accountDTO);
	}

	/**
	 * 修改账户认证状态
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccountAuth")
	public void updateAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = false) Long accountId,
			@RequestParam(value = "isAuth", required = false) Integer isAuth) {

		accountService.updateAccountAuth(accountId, isAuth);

	}

	/**
	 * 通过ticket获取用户信息(此方法总是放到最后)
	 */
	@ResponseBody
	@PostMapping(value = "/getAccountByTicket")
	public String getAccountByTicket(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		AccountSysDTO accountSysDTO = null;
		try {
			accountSysDTO = emcService.getAccountByTicket(ticket, clientType);
		} catch (Exception e) {
			accountSysDTO = new AccountSysDTO();
			accountSysDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			accountSysDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.person.fail"));
		}
		return mapper.toJson(accountSysDTO);
	}

	/**
	 * 根据账户名查询账户信息
	 */
	@ResponseBody
	@PostMapping(value = "/getAccountByPhone")
	public String getAccountByPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.getAccountByPhone(loginName, ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg("根据手机号查询账户信息时出错！");
		}
		return mapper.toJson(responseDTO);
	}
	

	/**
	 * 账户级别
	 */
	@ResponseBody
	@PostMapping(value = "/getAccountlevel")
	public String getAccountlevel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountLevelService.getAccountlevel( ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.level.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 收入接口
	 */
	@ResponseBody
	@PostMapping(value = "/getIncomeList")
	public String getIncomeList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.getIncomeList(ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.income.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	
	/**
	 * 修改头像接口
	 */
	@ResponseBody
	@PostMapping(value = "/updateAccountHead")
	public String updateAccountHead(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) String file,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountService.updateAccountHead(ImageMoudel.AccountHeadImagePath, file ,ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.head.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
