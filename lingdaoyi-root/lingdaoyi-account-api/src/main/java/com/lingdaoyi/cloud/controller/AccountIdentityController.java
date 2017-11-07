package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountIdentityService;
import com.lingdaoyi.cloud.service.AccountService;
import com.lingdaoyi.cloud.utils.IpUtil;

@Configuration
@Controller
@SpringBootApplication
@RequestMapping("/accountIdentity")
public class AccountIdentityController {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountIdentityService accountIdentityService;

	@Autowired
	private AccountService accountService;

	/**
	 * 身份验证成功后添加身份信息
	 */
	@ResponseBody
	@PostMapping(value = "/saveIdentityInfo")
	public String saveIdentityInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = false) Long accountId,
			@RequestParam(value = "idcard", required = false) String idcard,
			@RequestParam(value = "realname", required = false) String realname,
			@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "enkey", required = false) String enkey,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountIdentityService.saveIdentityInfo(accountId, idcard, realname, area, sex, enkey, ticket,
					clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.person.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 个人信息下认证接口
	 */
	@ResponseBody
	@PostMapping(value = "/viewAccountAuth")
	public String viewAccountAuth(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountIdentityService.viewAccountAuth(ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.sidentity.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 完善证件照片、电子邮箱、劳动合同信息 补全认证信息
	 */
	@ResponseBody
	@PostMapping(value = "/perfectIdentityInfo")
	public String perfectIdentityInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "num", required = false) String num,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "birth", required = false) String birth,
			@RequestParam(value = "nationality", required = false) String nationality,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "issue", required = false) String issue,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "laborUrl", required = false) String laborUrl,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountIdentityService.addIdentityInfo(name, num, sex, address, birth, startDate,
					nationality, endDate, issue, email, laborUrl, ticket, clientType);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.sidentity.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 根据账户ID查询认证状态
	 */
	@ResponseBody
	@PostMapping(value = "/getAuthStatus")
	public String getAuthStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = false) Long accountId) {
		String authStatus = accountService.getAuthStatus(accountId);
		return authStatus;
	}

	/**
	 * 修改支付密码身份证认证
	 */
	@ResponseBody
	@PostMapping(value = "/updatePayPwdAuth")
	public String updatePayPwdAuth(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "num", required = false) String num,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountIdentityService.updatePayPwdAuth(realName, num, ticket, clientType, IpUtil.getRequestIp(), sign);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.sidentity.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
