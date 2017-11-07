package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.GoodsService;
import com.lingdaoyi.cloud.service.OrderService;
import com.lingdaoyi.cloud.service.SponsorService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/order")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/buyNow")
	public String buyNow(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSpuId", required = false) Long goodsSkuId,
			@RequestParam(value = "num", required = false) Integer num,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.buyNow(ticket, goodsSkuId, num, clientType,IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.getIndexInfo.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@RequestMapping(value = "/goSettlement")
	public String goSettlement(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSpuId", required = false) Long goodsSpuId,
			@RequestParam(value = "num", required = false) Integer num,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.goSettlement(ticket, goodsSpuId, num, clientType,IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.goSettlement.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@RequestMapping(value = "/toPayOrderApp")
	public String toPayOrderApp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "goodsSpuId", required = false) Long goodsSpuId,
			@RequestParam(value = "countryId", required = false) Long countryId,
			@RequestParam(value = "num", required = false) Integer num,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "deviceId", required = false) String deviceId) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.toPayOrderApp(clientType, ticket, payType,goodsSpuId,num,
					IpUtil.getIpAddr(request), deviceId, countryId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.toPayOrderApp.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@RequestMapping(value = "/appPay")
	public String appPay(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "countryId", required = false) Long countryId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "fingerprintSuccess", required = false, defaultValue = "") String fingerprintSuccess,
			@RequestParam(value = "payType", required = false) Integer payType) {

		ResponseDTO responseDTO = null;

		try {
			responseDTO = orderService.appPay(clientType, ticket, payType, orderId,countryId, IpUtil.getIpAddr(request),
					fingerprintSuccess,password);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.appPay.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@RequestMapping(value = "/getOrderList")
	public String getOrderList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.getOrderList(clientType, ticket, status, pageSize, pageNum);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.getOrderList.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@RequestMapping(value = "/viewOrder")
	public String viewOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) Long orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.viewOrder(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.viewOrder.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	@PostMapping(value = "/refundOrder")
	public String cancelPayOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "orderId", required = false) Long orderId) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = orderService.cancelPayOrder(clientType, ticket, orderId);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.refundOrder.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
