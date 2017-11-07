package com.lingdaoyi.cloud.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.ChannelItemDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.entity.SysAppversion;
import com.lingdaoyi.cloud.entity.SysChannelItem;
import com.lingdaoyi.cloud.entity.SysChannelSetting;
import com.lingdaoyi.cloud.mapper.SysAppversionMapper;
import com.lingdaoyi.cloud.mapper.SysChannelItemMapper;
import com.lingdaoyi.cloud.mapper.SysChannelSettingMapper;
import com.lingdaoyi.cloud.utils.SignUtil;

@Service
@Transactional
public class IndexService {

	@Autowired
	private SysAppversionMapper sysAppversionMapper;

	@Autowired
	private SysChannelSettingMapper sysChannelSettingMapper;

	@Autowired
	private SysChannelItemMapper sysChannelItemMapper;
	
	@Autowired
	private SystemService systemService;


	/***
	 * 获取首页信息
	 * 
	 * @param countryId
	 *            国家地区id
	 * @param clientType
	 *            客户端标识
	 * @return
	 */
	public ResponseDTO getIndexPageInfo(Long countryId, Integer clientType,String sign,String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (countryId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("countryId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if(!SignUtil.httpEncrypt(ip, clientType, sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}

		// 获取该地区栏目
		SysChannelSetting sysChannelSettingQuery = new SysChannelSetting();
		sysChannelSettingQuery.setCountryId(countryId);
		sysChannelSettingQuery.setIsvisible(1);
		List<SysChannelSetting> sysChannelSettingList = Lists.newArrayList();
		sysChannelSettingList = sysChannelSettingMapper.select(sysChannelSettingQuery);
		if (sysChannelSettingList == null || sysChannelSettingList.size() == 0) {
			sysChannelSettingQuery.setCountryId(0L);
			sysChannelSettingList = sysChannelSettingMapper.select(sysChannelSettingQuery);
		}

		// banner图
		List<ChannelItemDTO> bannerChannelItemList = Lists.newArrayList();
		// 功能区
		List<ChannelItemDTO> demandChannelItemList = Lists.newArrayList();
		// 工具区
		List<ChannelItemDTO> toolsChannelItemList = Lists.newArrayList();
		// 广告位
		List<ChannelItemDTO> advertChannelItemList = Lists.newArrayList();

		if (sysChannelSettingList != null && sysChannelSettingList.size() > 0) {
			// 获取具体栏目信息
			SysChannelItem sysChannelItemquery = new SysChannelItem();
			sysChannelItemquery.setSettingId(sysChannelSettingList.get(0).getId());
			sysChannelItemquery.setIsvisible(1);
			List<SysChannelItem> sysChannelItemList = sysChannelItemMapper.select(sysChannelItemquery);
			ChannelItemDTO channelItemDTO = null;
			for (SysChannelItem sysChannelItem : sysChannelItemList) {
				if (sysChannelItem.getIsvisible().intValue() == 1) {
					// banner
					if (sysChannelItem.getSeetingtype().intValue() == 1) {
						channelItemDTO = new ChannelItemDTO();
						channelItemDTO.setEntityId(sysChannelItem.getEntityId());
						channelItemDTO.setEntityTitle(sysChannelItem.getEntityName());
						channelItemDTO.setImageUrl(systemService.getImageUrl(sysChannelItem.getImageId().toString()));
						channelItemDTO.setLinkUrl(sysChannelItem.getWeburl());
						channelItemDTO.setUrlType(sysChannelItem.getSkiptype());
						bannerChannelItemList.add(channelItemDTO);
					}
					// 功能
					if (sysChannelItem.getSeetingtype().intValue() == 2) {
						channelItemDTO = new ChannelItemDTO();
						channelItemDTO.setImageUrl(systemService.getImageUrl(sysChannelItem.getImageId().toString()));
						channelItemDTO.setUrlType(sysChannelItem.getSkiptype());
						demandChannelItemList.add(channelItemDTO);
					}
					// 工具
					if (sysChannelItem.getSeetingtype().intValue() == 3) {
						channelItemDTO.setImageUrl(systemService.getImageUrl(sysChannelItem.getImageId().toString()));
						channelItemDTO.setUrlType(sysChannelItem.getSkiptype());
						toolsChannelItemList.add(channelItemDTO);
					}
					// 广告位
					if (sysChannelItem.getSeetingtype().intValue() == 4) {
						channelItemDTO = new ChannelItemDTO();
						channelItemDTO.setEntityId(sysChannelItem.getEntityId());
						channelItemDTO.setEntityTitle(sysChannelItem.getEntityName());
						channelItemDTO.setImageUrl(systemService.getImageUrl(sysChannelItem.getImageId().toString()));
						channelItemDTO.setLinkUrl(sysChannelItem.getWeburl());
						channelItemDTO.setUrlType(sysChannelItem.getSkiptype());
						advertChannelItemList.add(channelItemDTO);
					}
				}
			}
		}

		// transferFeignClient.findLastRecord(userId);

		Map<String, Object> data = Maps.newHashMap();
		data.put("bannerChannelItemList", bannerChannelItemList);
		data.put("demandChannelItemList", demandChannelItemList);
		data.put("toolsChannelItemList", toolsChannelItemList);
		data.put("advertChannelItemList", advertChannelItemList);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.getIndexInfo.success"));
		return responseDTO;
	}

	/***
	 * 获取首页信息
	 * 
	 * @param countryId
	 *            国家地区id
	 * @param ip 
	 * @param sign 
	 * @param clientType
	 *            客户端标识
	 * @return
	 */
	public ResponseDTO sysColumns(Integer type, Long countryId, String sign, String ip ,Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (type == null || type == 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
				responseDTO.setMsg("type" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
				return responseDTO;
		}
		if (countryId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("countryId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if(!SignUtil.httpEncrypt(ip, clientType, sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}

		// 获取该地区优惠头图
		SysChannelSetting sysChannelSettingQuery = new SysChannelSetting();
		sysChannelSettingQuery.setCountryId(countryId);
		sysChannelSettingQuery.setIsvisible(1);
		sysChannelSettingQuery.setSeetingtype(5);
		List<SysChannelSetting> sysChannelSettingList = Lists.newArrayList();
		sysChannelSettingList = sysChannelSettingMapper.select(sysChannelSettingQuery);
		if (sysChannelSettingList == null || sysChannelSettingList.size() == 0) {
			sysChannelSettingQuery.setCountryId(0L);
			sysChannelSettingList = sysChannelSettingMapper.select(sysChannelSettingQuery);
		}

		// 优惠头图
		List<ChannelItemDTO> preferentialChanneList = Lists.newArrayList();

		if (sysChannelSettingList != null && sysChannelSettingList.size() > 0) {
			// 获取具体栏目信息
			SysChannelItem sysChannelItemquery = new SysChannelItem();
			sysChannelItemquery.setSettingId(sysChannelSettingList.get(0).getId());
			sysChannelItemquery.setIsvisible(1);
			sysChannelItemquery.setSeetingtype(5);
			List<SysChannelItem> sysChannelItemList = sysChannelItemMapper.select(sysChannelItemquery);
			ChannelItemDTO channelItemDTO = null;
			for (SysChannelItem sysChannelItem : sysChannelItemList) {
				if (sysChannelItem.getIsvisible().intValue() == 1) {
					channelItemDTO = new ChannelItemDTO();
					channelItemDTO.setEntityId(sysChannelItem.getEntityId());
					channelItemDTO.setEntityTitle(sysChannelItem.getEntityName());
					channelItemDTO.setImageUrl(systemService.getImageUrl(sysChannelItem.getImageId().toString()));
					channelItemDTO.setLinkUrl(sysChannelItem.getWeburl());
					channelItemDTO.setUrlType(sysChannelItem.getSkiptype());
					preferentialChanneList.add(channelItemDTO);
				}
			}
		}

		Map<String, Object> data = Maps.newHashMap();
		data.put("preferentialChanneList", preferentialChanneList);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.system.sysColumns.success"));
		return responseDTO;
	}
	
	public ResponseDTO test(String addressIp) {
	    ResponseDTO responseDTO = new ResponseDTO();

	    Map<String, Object> returnData = Maps.newHashMap();

	    returnData.put("addressIp", "您的ip为" + addressIp + "服务当前运转正常,谢谢！");
	    responseDTO.setData(returnData);

	    responseDTO.setSuccess(true);
	    responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
	    responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Login, "emc.login.logout"));

	    return responseDTO;
	  }

}
