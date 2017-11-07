package com.lingdaoyi.cloud.controller;

import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.service.IndexService;
import com.lingdaoyi.cloud.service.SystemService;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.init.Constant;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestController
@RequestMapping("/system")
public class SystemController {

	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SystemService systemService;

	@PostMapping(value = "/getImageUrl")
	public String getImageUrl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id) {

		String url = "";
		try {
			url = systemService.getImageUrl(id);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return mapper.toJson(url);
	}
	
	@PostMapping(value = "/getImageUrls")
	public String getImageUrls(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ids", required = false) String ids) {

		String[] imageUrls = null ;
		try {
			imageUrls = systemService.getImageUrls(ids);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return mapper.toJson(imageUrls);
	}
	
	@PostMapping(value = "/addDictionary")
	public String addDictionary(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id) {

		try {
			systemService.addDictionary();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return mapper.toJson("字典添加成功");
	}
	
	/**
	 * webuploader上传图片文件的方法
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/imageUploadDG", method = RequestMethod.POST)
	public String imageUploadDG(HttpServletRequest request,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "file") String base64) {

		// 操作信息的map，将map通过json方式返回给页面
		ResponseDTO responseDTO = null;
		try {
			responseDTO = systemService.imageUploadDG(base64,type);
		} catch (Exception e) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Goods_Msg, "emc.system.imageUploadDG.fail"));
		}
		return mapper.toJson(responseDTO);
	}

}
