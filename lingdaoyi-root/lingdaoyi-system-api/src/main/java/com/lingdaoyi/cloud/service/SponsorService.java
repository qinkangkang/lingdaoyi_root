package com.lingdaoyi.cloud.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.SponsorDTO;
import com.lingdaoyi.cloud.dto.SponsorDTO2;
import com.lingdaoyi.cloud.dto.SponsorDetailDTO;
import com.lingdaoyi.cloud.dto.SponsorGoodsDTO;
import com.lingdaoyi.cloud.dto.SponsorWithdrawDTO;
import com.lingdaoyi.cloud.entity.GoodsSponsor;
import com.lingdaoyi.cloud.mapper.GoodsSponsorMapper;
import com.lingdaoyi.cloud.mapper.GoodsSpuMapper;
import com.lingdaoyi.cloud.utils.SignUtil;

@Service
@Transactional
public class SponsorService {

	@Autowired
	private GoodsSponsorMapper goodsSponsorMapper;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private SystemService systemService;

	/***
	 * 获取首页信息
	 * 
	 * @param countryId
	 *            国家地区id
	 * @param string 
	 * @param sign 
	 * @param clientType
	 *            客户端标识
	 * @return
	 */
	public ResponseDTO getSponsorList(Long countryId, Integer clientType, Integer pageSize, Integer pageNum, String sign, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null || clientType == 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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

		PageHelper.startPage(pageNum, pageSize);
		GoodsSponsor goodsSponsorQuery = new GoodsSponsor();
		goodsSponsorQuery.setCountryId(countryId);
		List<GoodsSponsor> goodsSponsorList = goodsSponsorMapper.select(goodsSponsorQuery);
		List<SponsorDTO> sponsorDTOs = Lists.newArrayList();
		SponsorDTO sponsorDTO = null;
		for (GoodsSponsor goodsSponsor : goodsSponsorList) {
			sponsorDTO = new SponsorDTO();
			sponsorDTO.setSponsorId(goodsSponsor.getId());
			sponsorDTO.setSponsorUrl(systemService.getImageUrl(goodsSponsor.getImage()));
			sponsorDTOs.add(sponsorDTO);
		}

		Map<String, Object> data = Maps.newHashMap();
		PageDTO pageDTO = new PageDTO<GoodsSponsor>(goodsSponsorList);
		pageDTO.setList(sponsorDTOs);
		data.put("page", pageDTO);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorList.success"));
		return responseDTO;
	}

	public ResponseDTO getSponsorDetail(Long sponsorId, String ticket, Integer clientType, String sign, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (sponsorId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("sponsorId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if(!SignUtil.httpEncrypt(ip, clientType, sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}
		GoodsSponsor goodsSponsor = goodsSponsorMapper.selectByPrimaryKey(sponsorId);
		SponsorDetailDTO sponsorDetailDTO = new SponsorDetailDTO();
		sponsorDetailDTO.setSponsorId(goodsSponsor.getId());
		sponsorDetailDTO.setAddress(goodsSponsor.getAddress());
		sponsorDetailDTO.setGps(goodsSponsor.getGps());
		sponsorDetailDTO.setPhone(goodsSponsor.getPhone());
		sponsorDetailDTO.setSponsorBrief(goodsSponsor.getBrief());
		sponsorDetailDTO.setSponsorImage(systemService.getImageUrls(goodsSponsor.getImages()));
		sponsorDetailDTO.setSponsorName(goodsSponsor.getName());
		List<SponsorGoodsDTO> sponsorGoodsList = goodsService.getGoodsBySponsor(sponsorId,goodsSponsor.getCountryId());
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("sponsorDetailDTO", sponsorDetailDTO);
		data.put("sponsorGoodsList", sponsorGoodsList);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorDetail.success"));
		return responseDTO;
	}
	
	/***
	 * 获取首页信息
	 * 
	 * @param countryId
	 *            国家地区id
	 * @param string 
	 * @param sign 
	 * @param clientType
	 *            客户端标识
	 * @return
	 */
	public ResponseDTO getWithdrawSponsorList(Long countryId, Integer clientType, Integer pageSize, Integer pageNum, String sign, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null || clientType == 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("clientType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
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

		PageHelper.startPage(pageNum, pageSize);
		GoodsSponsor goodsSponsorQuery = new GoodsSponsor();
		goodsSponsorQuery.setSponsortype(2);
		goodsSponsorQuery.setCountryId(countryId);
		List<GoodsSponsor> goodsSponsorList = goodsSponsorMapper.select(goodsSponsorQuery);
		List<SponsorWithdrawDTO> sponsorWithdrawDTOs = Lists.newArrayList();
		SponsorWithdrawDTO sponsorDTO = null;
		for (GoodsSponsor goodsSponsor : goodsSponsorList) {
			sponsorDTO = new SponsorWithdrawDTO();
			sponsorDTO.setSponsorId(goodsSponsor.getId());
			sponsorDTO.setAccountId(goodsSponsor.getAccountId());
			sponsorDTO.setAddress(goodsSponsor.getAddress());
			sponsorDTO.setReign(goodsSponsor.getReign());
			sponsorDTO.setSponsorName(goodsSponsor.getName());
			sponsorDTO.setWorkTime(goodsSponsor.getWorktime());
			sponsorWithdrawDTOs.add(sponsorDTO);
		}

		Map<String, Object> data = Maps.newHashMap();
		data.put("page", new PageDTO<SponsorWithdrawDTO>(sponsorWithdrawDTOs));
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Sponsor_Msg, "emc.sponsor.getSponsorList.success"));
		return responseDTO;
	}

	public SponsorDTO2 getSponsorById(Long sponsorId) {
		SponsorDTO2 dto = new SponsorDTO2();
		GoodsSponsor sponsor = goodsSponsorMapper.selectByPrimaryKey(sponsorId);
		if(sponsor!=null){
			dto.setSponsorId(sponsor.getId());
			dto.setSponsorType(sponsor.getSponsortype());
		}
		return dto;
	}

}
