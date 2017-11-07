package com.lingdaoyi.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.account.QRCodeDTO;
import com.lingdaoyi.cloud.dto.account.QRCodeInfoDTO;
import com.lingdaoyi.cloud.dto.account.ReceiptQRcodeDTO;
import com.lingdaoyi.cloud.service.QRCodeService;
import com.lingdaoyi.cloud.utils.IpUtil;

/**
 * 二维码接口
 * 
 * @author jack
 *
 */
@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/accountQRCode")
public class QRCodeController {
	private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	@Autowired
	private QRCodeService qRCodeService;

	@PostMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = qRCodeService.test(IpUtil.getRequestIp());
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
	 * 二维码
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @return
	 */
	@PostMapping("/myQRcode")
	public String myQRcode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType) {
		QRCodeDTO dto = null;
		try {
			dto = qRCodeService.myQRcode(ticket, clientType);
			if(dto == null){
				throw new Exception();
			}
		} catch (Exception e) {
			dto = new QRCodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setError_msg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcode.fail"));
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}

	/**
	 * 二维码好友信息接口
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param qrcodestr
	 * @return
	 */
	@PostMapping("/QRcodeInfo")
	public String QRcodeInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "qrcodestr", required = false) String qrcodestr) {

		QRCodeInfoDTO dto = null;
		try {
			dto = qRCodeService.QRcodeInfo(ticket, clientType, qrcodestr);
		} catch (Exception e) {
			dto = new QRCodeInfoDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setError_msg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcodeinfo.fail"));
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}

	public String payQRcode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "qrcodestr", required = false) String qrcodestr) {

		return "";
	}
	/**
	 * 转账二维码接口
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param money
	 * @return
	 */
	@PostMapping("/getReceiptQRcode")
	public String getReceiptQRcode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "money", required = false) String money) {
		QRCodeDTO dto = null;
		try {
			dto = qRCodeService.getReceiptQRcode(ticket, clientType,money);
		} catch (Exception e) {
			dto = new QRCodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setError_msg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcode.fail"));
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}

	/**
	 * 二维码收款
	 * 
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param qrcodestr
	 * @return
	 */
	@PostMapping("/receiptQRcode")
	public String receiptQRcode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "qrcodestr", required = false) String qrcodestr) {

		ReceiptQRcodeDTO dto = null;
		try {
			dto = qRCodeService.receiptQRcode(ticket, clientType, qrcodestr);
		} catch (Exception e) {
			dto = new ReceiptQRcodeDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setError_msg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcodeinfo.fail"));
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}
	/**
	 * 线下充值二维码
	 * @param request
	 * @param response
	 * @param ticket
	 * @param clientType
	 * @param accountId
	 * @return
	 */
	@PostMapping("/offlineRechargeCode")
	public String offlineRechargeCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "accountId", required = false)Long accountId){
		ResponseDTO dto = null;
		try {
			dto = qRCodeService.offlineRechargeCode(ticket, clientType, accountId);
		} catch (Exception e) {
			dto = new ResponseDTO();
			dto.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Account, "emc.account.qrcode.fail"));
			dto.setSuccess(false);
		}
		return mapper.toJson(dto);
	}
}
