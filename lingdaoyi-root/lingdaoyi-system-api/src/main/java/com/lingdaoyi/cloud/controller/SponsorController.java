package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.lingdaoyi.cloud.dto.SponsorDTO2;
import com.lingdaoyi.cloud.service.SponsorService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/sponsor")
public class SponsorController {

	private static final Logger logger = LoggerFactory.getLogger(SponsorController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SponsorService sponsorService;

	@RequestMapping(value = "/getSponsorList")
	@ResponseBody
	public String getSponsorList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "countryId", required = false, defaultValue = "1") Long countryId,
			@RequestParam(value = "orderBy", required = false, defaultValue = "0") Integer orderBy,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getSponsorList(countryId, clientType, pageSize, pageNum,sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorList.fail"));
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
	@RequestMapping(value = "/getSponsorDetail")
	public String getSponsorDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "sponsorId", required = false, defaultValue = "1") Long sponsorId,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getSponsorDetail(sponsorId, ticket, clientType,sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorDetail.fail"));
		}
		return mapper.toJson(responseDTO);

	}
	
	@RequestMapping(value = "/getWithdrawSponsorList")
	@ResponseBody
	public String getWithdrawSponsorList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "countryId", required = false, defaultValue = "1") Long countryId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "sign", required = false) String sign) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = sponsorService.getWithdrawSponsorList(countryId, clientType, pageSize, pageNum,sign, IpUtil.getIpAddr(request));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorList.fail"));
		}
		return mapper.toJson(responseDTO);
	}

	@PostMapping("/getSponsorById")
	public String getSponsorById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sponsorId", required = false)Long sponsorId){

		SponsorDTO2  dto = sponsorService.getSponsorById(sponsorId);
	
		return mapper.toJson(dto);
	}
}
