package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.GoodsService;
import com.lingdaoyi.cloud.service.SponsorService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/getGoodsList")
	@ResponseBody
	public String getGoodsList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "countryId", required = false, defaultValue = "1") Long countryId,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsList(countryId, searchKey ,clientType, pageSize, pageNum,sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Goods_Msg, "emc.goods.getGoodsList.fail"));
		}
		return mapper.toJson(responseDTO);
	}
	
	/**
	 * 获取商户详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGoodsDetail")
	public String getGoodsDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "goodsSpuId", required = false, defaultValue = "1") Long goodsSpuId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = goodsService.getGoodsDetail(goodsSpuId, ticket, clientType,sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Goods_Msg, "emc.goods.getGoodsDetail.fail"));
		}
		return mapper.toJson(responseDTO);

	}

}
