package com.lingdaoyi.cloud.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.utils.Exceptions;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.AccountRecordService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.JsonMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/record")
public class AccountRecordController {

	private static final Logger logger = LoggerFactory.getLogger(AccountRecordController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private AccountRecordService accountRecordService;

	/**
	 * 最近的几条资金记录流水
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/findLastRecord")
	public String findLastRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "count", required = false) Integer count,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountRecordService.findLastRecord(ticket, clientType,sign,IpUtil.getRequestIp(),count);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.record.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	/**
	 * 获取资金流水
	 * 
	 * @param request
	 * @param response
	 * @param clientType:客户端
	 * @param ticket:账户唯一标识
	 * @param type:0:充值;1:一般转账;2:收钱;3.提现;4:购物消费转账;5:二维码支付
	 * @param receiveAccountId:对方的账户ID
	 * @param sign:加密
	 * @param pageNum:当前页
	 * @param pageSize:每页的数据条数
	 * @return
	 */
	@PostMapping("/getCapitalFlowRecord")
	public String getCapitalFlowRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "type", required =false) Integer type,
			@RequestParam(value = "receiveAccountId", required = false) Long receiveAccountId,
			@RequestParam(value = "isGroup", required = false) Integer isGroup,
			@RequestParam(value = "sign", required = true) String sign,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

		ResponseDTO responseDTO = null;
		try {
			//TODO 更改ip 并确认记录分组方法
			responseDTO = accountRecordService.getCapitalFlowRecord(ticket, clientType, pageNum, pageSize, type,
					receiveAccountId, sign, IpUtil.getRequestIp(),isGroup);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.record.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	
	
	
	/**
	 * 
	 * 余额转账,余额扣款以及余额支付
	 * 
	 * @param clientType:客户端
	 * @param ticket:用户唯一标识
	 * @param transferAmount:转账余额
	 * @param org_currency:当前账户币种
	 * @param trans_currency:对方货币币种
	 * @param exchageTime:汇率时间
	 * @param exchangeValue:汇率
	 * @param type:支付方式:(1:一般转账;4:购物类转账;5:二维码支付)
	 * @param receiveAccountId:对方账户id
	 * @param payPassword:支付密码
	 * @param sign:加密规则
	 * @return
	 */
	@PostMapping("/accountTransfer")
	public String accountTransfer(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "transferAmount", required = true) String transferAmount,
			@RequestParam(value = "receiveAccountId", required = true) String receiveAccountId,
			@RequestParam(value = "payPassword", required = false) String payPassword,
			@RequestParam(value = "orgCurrency", required = true) String orgCurrency,
			@RequestParam(value = "transCurrency", required = true) String transCurrency,
			@RequestParam(value = "exchageTime", required = true) String exchageTime,
			@RequestParam(value = "exchangeValue", required = true) String exchangeValue,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "afterTransferAmount", required = true) String afterTransferAmount,
			@RequestParam(value = "fingerprintSuccess", required =false ) String fingerprintSuccess,
			@RequestParam(value = "sign", required = true) String sign) {
		ResponseDTO responseDTO = null;
		try {
			//TODO
			//IP
			responseDTO = accountRecordService.accountTransfer(type, ticket, clientType, transferAmount,
					receiveAccountId, payPassword, sign, IpUtil.getRequestIp(),orgCurrency,transCurrency,exchageTime,exchangeValue,afterTransferAmount,remarks,fingerprintSuccess);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.accountTransfer.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 余额账户充值-线上/线下(未完成)
	 * 
	 * @param ticket:用户唯一标识
	 * @param money:充值金额
	 * @param AccountBankCardId:充值用的银行卡ID
	 * @param clientType:客户端
	 * @param password:充值密码
	 * @param sign:加密规则
	 * @return
	 */
	@PostMapping("/accountRecharge")
	public String accountRecharge(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "transferAmount", required = true) String transferAmount,
			@RequestParam(value = "accountBankCardId", required = true) Long AccountBankCardId,
			@RequestParam(value = "payPassword", required = true) String payPassword,
			@RequestParam(value = "sign", required = true) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountRecordService.accountRecharge( ticket, transferAmount, clientType,
					AccountBankCardId, payPassword, sign, IpUtil.getRequestIp(),type);
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.recharge.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 余额转账时的校验:判断余额是否不足及转账国籍
	 * 
	 * @param request
	 * @param response
	 * @param clientType
	 * @param ticket
	 * @param transferAmount
	 * @return
	 */
	@PostMapping("/checkBalanceAndCounty")
	public String checkBalanceAndCounty(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "transferAmount", required = true) String transferAmount,
			@RequestParam(value = "receivedAccountId", required = true) Long receivedAccountId,
			@RequestParam(value = "sign", required = true) String sign) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountRecordService.checkBalanceAndCounty(ticket, transferAmount, clientType, sign,
					receivedAccountId,IpUtil.getRequestIp());
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.recharge.fail"));
		}
		return mapper.toJson(responseDTO);

	}

	
	
	
	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountRecordService.test(IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout.fail"));
		}
		return mapper.toJson(responseDTO);

	}
	
	
	
	/**
	 * 依据id获取记录详情
	 * @param request
	 * @param response
	 * @param clientType
	 * @param ticket
	 * @param accountRecordId
	 * @param sign
	 * @return
	 */
	
	@PostMapping(value = "/findRecordById")
	public String findRecordById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "accountRecordId", required = true) Long accountRecordId,
			@RequestParam(value = "type", required = true) Integer  type,
			@RequestParam(value = "sign", required = true) String sign){
		ResponseDTO responseDTO = null;
		try {
			responseDTO = accountRecordService.findRecordById(clientType,ticket,accountRecordId,sign,type,IpUtil.getRequestIp());
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.getRecordInfo.fail"));
		}
		return mapper.toJson(responseDTO);
	}
}
