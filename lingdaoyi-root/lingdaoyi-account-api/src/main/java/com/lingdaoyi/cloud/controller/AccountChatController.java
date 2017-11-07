package com.lingdaoyi.cloud.controller;

import java.math.BigDecimal;
import java.util.Date;

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
import com.lingdaoyi.cloud.service.AccountChatListService;
import com.lingdaoyi.cloud.service.AccountFriendService;
import com.lingdaoyi.cloud.service.AccountService;

@Configuration
@Controller
@SpringBootApplication
@RequestMapping("/chat")
public class AccountChatController {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountChatListService accountChatListService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountFriendService accountFriendService;

	/**
	 * 获取最近转账朋友列表
	 * 
	 * @param request
	 * @param response
	 * @param clientType
	 * @param accountId
	 * @param ticket
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/getLatelyFriendsList")
	public String getLatelyFriendsList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountChatListService.getLatelyFriendsList(clientType, ticket, pageNum, pageSize);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.chat.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@PostMapping(value = "/addChatRecord")
	public void addChatRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId", required = false) Long accountId,
			@RequestParam(value = "toAccountId", required = false) Long toAccountId,
			@RequestParam(value = "amounts", required = false) BigDecimal amounts,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "transferDate", required = false) Date transferDate) {

		try {
			accountChatListService.addChatRecord(accountId, toAccountId, amounts, type, transferDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@PostMapping(value = "/findAccountPhone")
	public String findAccountPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountFriendService.findAccountPhone(clientType, phone, ticket);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.chat.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 获取账户好友列表
	 */
	@ResponseBody
	@PostMapping(value = "/getFriendList")
	public String getFriendList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountFriendService.getFriendList(clientType, ticket);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.person.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@ResponseBody
	@PostMapping(value = "/addFriends")
	public String addFriends(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "toAccountId", required = false) Long toAccountId,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountFriendService.addFriends(clientType, toAccountId, ticket);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.friend.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@ResponseBody
	@PostMapping(value = "/getAccountExistence")
	public String getAccountExistence(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "toAccountIdList", required = false) String toAccountIdList,
			@RequestParam(value = "ticket", required = false) String ticket) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountFriendService.getAccountExistence(clientType, toAccountIdList, ticket);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.existence.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
