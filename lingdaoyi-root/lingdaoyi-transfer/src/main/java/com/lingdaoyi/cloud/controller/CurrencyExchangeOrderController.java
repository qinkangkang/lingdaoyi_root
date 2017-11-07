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
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.CurrencyExchangeOrderService;
import com.lingdaoyi.cloud.utils.IpUtil;

@RestController
@RequestMapping("/currencyexchangeorder")
public class CurrencyExchangeOrderController {
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeOrderController.class);
	@Autowired
	private CurrencyExchangeOrderService currencyExchangeOrderService;

	@RequestMapping("/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = currencyExchangeOrderService.test(IpUtil.getRequestIp());
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
	 * 创建购买外币订单
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param fromCurrency
	 * @param fromAmount
	 * @param toCurrency
	 * @param toAmount
	 * @param rate
	 * @param exchangeTime
	 * @param sponsorId
	 *            商户ID
	 * @param accountId
	 *            账户ID
	 * @param fee
	 *            手续费
	 * @return 订单信息
	 */
	@PostMapping("/createOrder")
	public String createOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "fromCurrency", required = true) String fromCurrency,
			@RequestParam(value = "fromAmount", required = true) String fromAmount,
			@RequestParam(value = "toCurrency", required = true) String toCurrency,
			@RequestParam(value = "toAmount", required = true) String toAmount,
			@RequestParam(value = "rate", required = true) String rate,
			@RequestParam(value = "exchangeTime", required = true) String exchangeTime,
			@RequestParam(value = "sponsorId", required = true) Long sponsorId,
			@RequestParam(value = "accountId", required = true) Long accountId,
			@RequestParam(value = "fee", required = true) String fee) {
		ResponseDTO dto = null;
		try {
			dto = currencyExchangeOrderService.createOrder(ticket, clientType, fromCurrency, fromAmount, toCurrency,
					toAmount, rate, exchangeTime, sponsorId, accountId, fee);
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.currencyexchangeorder.fail"));
		}
		return mapper.toJson(dto);
	}
	
	
	@PostMapping("/updateordertype")
	public String updateOrderType(HttpServletRequest request, HttpServletResponse response,String sign){
		ResponseDTO dto = null;
		try {
			dto = currencyExchangeOrderService.updateOrderType(sign);
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
		}
		return mapper.toJson(dto);
	}
	
	
	@PostMapping("/getCurrencyExchangeOrderById")
	public String getCurrencyExchangeOrderById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = true) String ticket,
			@RequestParam(value = "clientType", required = true) Integer clientType,
			@RequestParam(value = "currencyExchangeOrderId", required = true)Long currencyExchangeOrderId){
		ResponseDTO dto = null;
		try {
			dto = currencyExchangeOrderService.getCurrencyExchangeOrderById(ticket,clientType,currencyExchangeOrderId);
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.select.currencyexchangeorder.fail"));
		}
		return mapper.toJson(dto);
	}
	@PostMapping("/insertCurrencyExchangeRecord")
	public String insertRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sign", required = true)String sign,
			@RequestParam(value = "currencyExchangeOrderId", required = true)Long currencyExchangeOrderId,
			@RequestParam(value = "clientType", required = true)Integer clientType){
		ResponseDTO dto = null;
		try {
			dto = currencyExchangeOrderService.insertRecord(sign, currencyExchangeOrderId, clientType, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setSuccess(false);
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Transfer_Msg, "emc.currencyexchangerecord.fail"));
		}
		return mapper.toJson(dto);
	}
}
