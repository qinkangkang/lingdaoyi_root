package com.lingdaoyi.cloud.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.controller.IndexController;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.AppVersionDTO;
import com.lingdaoyi.cloud.dto.DictionaryDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.entity.SysAppversion;
import com.lingdaoyi.cloud.entity.SysDictionary;
import com.lingdaoyi.cloud.entity.SysImage;
import com.lingdaoyi.cloud.feign.AccountFeignClient;
import com.lingdaoyi.cloud.mapper.SysAppversionMapper;
import com.lingdaoyi.cloud.mapper.SysDictionaryMapper;
import com.lingdaoyi.cloud.mapper.SysImageMapper;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.FileUtils;
import com.lingdaoyi.cloud.utils.image.ImageUtil;
import com.lingdaoyi.cloud.utils.init.Constant;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class SystemService {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private SysDictionaryMapper sysDictionaryMapper;

	@Autowired
	private AccountFeignClient accountFeignClient;
	
	@Autowired
	private SysAppversionMapper sysAppversionMapper;
	
	@Autowired
	private SysImageMapper sysImageMapper;

	@Transactional(readOnly = true)
	public void addDictionary() {
		SysDictionary sysDictionaryQuery = new SysDictionary();
		sysDictionaryQuery.setStatus(1);
		List<SysDictionary> list = sysDictionaryMapper.select(sysDictionaryQuery);

		List<DictionaryDTO> dictionaryDTOs = Lists.newArrayList();
		DictionaryDTO dictionaryDTO = null;
		for (SysDictionary sysDictionary : list) {
			dictionaryDTO = new DictionaryDTO();
			dictionaryDTO.setClassId(sysDictionary.getClassId());
			dictionaryDTO.setCode(sysDictionary.getCode());
			dictionaryDTO.setName(sysDictionary.getName());
			dictionaryDTO.setValue(sysDictionary.getValue());
			dictionaryDTOs.add(dictionaryDTO);
		}
		RedisUtils.putCache(RedisMoudel.Dictionary, "secondDictonary", mapper.toJson(dictionaryDTOs));
		DictionaryUtil.initDictionary();

	}

	public AccountSysDTO getAccountByTicket(String ticket, Integer clientType) {
		AccountSysDTO accountDTO = new AccountSysDTO();
		String dto = accountFeignClient.getAccountByTicket(ticket, clientType);
		accountDTO = mapper.fromJson(dto, AccountSysDTO.class);
		return accountDTO;
	}
	
	@Transactional(readOnly = true)
	public ResponseDTO getAppVersion(Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		AppVersionDTO versionDTO = new AppVersionDTO();
		
		String appversion = "";
		try {
			appversion = RedisUtils.getValue(clientType.toString(), RedisMoudel.AppVersion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(appversion)){
			versionDTO = mapper.fromJson(appversion, AppVersionDTO.class);
		}else{
			Map<Integer, AppVersionDTO> avMap = Maps.newHashMap();
			AppVersionDTO appVersionDTO= null;
			
			List<SysAppversion> sysAppversionList = sysAppversionMapper.selectAll();
			for(SysAppversion sysAppversion:sysAppversionList){
				appVersionDTO = new AppVersionDTO();
				appVersionDTO.setAppType(clientType);
				appVersionDTO.setDescription(sysAppversion.getDescription());
				if(sysAppversion.getForceupgradevalue()==1){
					appVersionDTO.setIfUpdate(true);
				}else{
					appVersionDTO.setIfUpdate(false);
				}
				appVersionDTO.setReleaseDate(DateFormatUtils.format(sysAppversion.getReleasedate(), "yyyy-MM-dd HH:mm"));
				appVersionDTO.setVersionNum(sysAppversion.getVersionnum());
				appVersionDTO.setVersionValue(sysAppversion.getVersionvalue());
				avMap.put(clientType, appVersionDTO);
				RedisUtils.putCache(RedisMoudel.AppVersion, clientType.toString(), mapper.toJson(appVersionDTO));
			}
			versionDTO = avMap.get(clientType);
		}
		

		Map<String, Object> returnData = Maps.newHashMap();
		if (versionDTO == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.101"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.noAppVersion"));
			return responseDTO;
		}
		returnData.put("appVersion", versionDTO);
		responseDTO.setSuccess(true);
		responseDTO.setData(returnData);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.getAppVersion.success"));
		return responseDTO;
	}
	
	@Transactional(readOnly = true)
	public String getImageUrl(String id) {
		String res = null;

		if (StringUtils.isBlank(id)) {
			return StringUtils.EMPTY;
		}
		DictionaryUtil.clear();
		SysImage sysImage = sysImageMapper.selectByPrimaryKey(Long.valueOf(id));
		try {
			StringBuilder imageUrl = new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig))
					.append(PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig));
			imageUrl.append(sysImage.getRelativepath());
			imageUrl.append(sysImage.getStorefilename());
			imageUrl.append(".").append(sysImage.getStorefileext());
			res = imageUrl.toString();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			res = new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig))
					.append(PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig))
					.append("/foms/noPic.jpg").toString();
		}
		return res;
	}

	@Transactional(readOnly = true)
	public String[] getImageUrls(String ids) {
		if (StringUtils.isBlank(ids)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		String[] urls = ids.split(";");
		List<Long> idList = Lists.newArrayList();
		for (String string : urls) {
			if (StringUtils.isNotBlank(string)) {
				idList.add(Long.valueOf(string));
			}
		}
		List<SysImage> imageList = sysImageMapper.findimageList(idList);

		String rootUrl = PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig) + 
				PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig);
		String[] imageUrls = ArrayUtils.EMPTY_STRING_ARRAY;

		StringBuilder imageUrl = new StringBuilder();
		try {
			for (SysImage sysImage : imageList) {
				imageUrl.delete(0, imageUrl.length());
				imageUrl.append(rootUrl);
				imageUrl.append(sysImage.getRelativepath());
				imageUrl.append(sysImage.getStorefilename());
				imageUrl.append(".").append(sysImage.getStorefileext());
				imageUrls = ArrayUtils.add(imageUrls, imageUrl.toString());
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				imageUrls = ArrayUtils.add(imageUrls, new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig))
						.append(PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig))
						.append("/foms/noPic.jpg").toString());
		}
		return imageUrls;
	}
	
	/**
	 * 保存上传的图片文件保存到服务器，并返回图片文件描述的ImageJsonBean对象
	 * 
	 * @param valueMap
	 */
	public ResponseDTO imageUploadDG(String base64,String type) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		String filePathVar = PropertiesUtil.getProperty("system.fileServerUrl", PropertiesUtil.systemConfig);
		//MultipartFile file = imageUploadDTO.getFile();
		// 定义相对路径，并且将日期作为分隔子目录
		StringBuilder relativePath = new StringBuilder(type).append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"))
				.append("/");
		// 定义全路径，为了以后将相对路径和文件名添加进去
		StringBuilder rootPath = new StringBuilder(Constant.RootPath)
				.append(PropertiesUtil.getProperty("system.imageRootPath", PropertiesUtil.systemConfig)).append(relativePath);
		File tempFile = null;
		try { 
			String[] headAndBody = StringUtils.split(base64, ",");
			// String head = headAndBody[0];
			String body = headAndBody[1];

			// 先创建保存文件的目录
			File destDir = new File(rootPath.toString());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			// 定义最终目标文件对象
			File destFile = null;
			// 定义存储文件名
			String storeFileName = Identities.uuid2() + ".jpg";
			tempFile = new File(System.getProperty("java.io.tmpdir"), storeFileName);
			// 将Base64字符串图片内容转换成byte数组
			byte[] fileByte = Base64Utils.decodeFromString(body);

			// 将byte数组写入临时文件中
			org.apache.commons.io.FileUtils.writeByteArrayToFile(tempFile, fileByte, false);
			// 目标文件
			destFile = new File(rootPath.append(storeFileName).toString());
			// 将临时文件剪裁成为200高宽的正方图保存到目标文件中
			ImageUtil.square(tempFile, destFile, 200);
			// 相对路径加上保存文件名得到图片文件的相对全路径
			relativePath.append(storeFileName);
			
			SysImage image = new SysImage();
			image.setFilename(storeFileName);
			/*image.setFilesize(filesize);
			image.setImageheight(imageheight);
			image.setImagewidth(imagewidth);*/
			image.setRelativepath(relativePath.toString()); 
			image.setStatus(10);
			image.setStorefileext("jpg");
			image.setStorefilename(storeFileName);
			image.setUploadtime(new Date());
			sysImageMapper.insert(image);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		returnData.put("relativePath", relativePath.toString());
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.system.imageUploadDG.success"));
		return responseDTO;
	}
	
}
