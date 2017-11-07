package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.controller.OrderController;
import com.lingdaoyi.cloud.dto.GoodsDTO;
import com.lingdaoyi.cloud.dto.GoodsDetailDTO;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.SponsorDTO;
import com.lingdaoyi.cloud.dto.SponsorGoodsDTO;
import com.lingdaoyi.cloud.dto.account.AccountCountryDTO;
import com.lingdaoyi.cloud.entity.GoodsSponsor;
import com.lingdaoyi.cloud.entity.GoodsSpu;
import com.lingdaoyi.cloud.mapper.GoodsSponsorMapper;
import com.lingdaoyi.cloud.mapper.GoodsSpuMapper;
import com.lingdaoyi.cloud.utils.SignUtil;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

@Service
@Transactional
public class GoodsService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private GoodsSpuMapper goodsSpuMapper;
	
	@Autowired
	private GoodsSponsorMapper goodsSponsorMapper;
	
	@Autowired
	private SystemService systemService;

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
	public ResponseDTO getGoodsList(Long countryId, String searchKey,Integer clientType, Integer pageSize, Integer pageNum, String sign, String ip) {
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
		List<GoodsSpu> goodsSpuList = goodsSpuMapper.findGoodsSpuList(0L, countryId, searchKey);
		List<GoodsDTO> goodsDTOs = Lists.newArrayList();
		GoodsDTO goodsDTO = null;
		for (GoodsSpu goodsSpu : goodsSpuList) {
			goodsDTO = new GoodsDTO();
			goodsDTO.setGoodsId(goodsSpu.getId());
			goodsDTO.setGoodsUrl(systemService.getImageUrl(goodsSpu.getImage1()));
			goodsDTOs.add(goodsDTO);
		}

		Map<String, Object> data = Maps.newHashMap();
		PageDTO pageDTO = new PageDTO<GoodsSpu>(goodsSpuList);
		pageDTO.setList(goodsDTOs);
		data.put("page", pageDTO);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.goods.getGoodsList.success"));
		return responseDTO;
	}

	/**
	 * 获取商品详情
	 * @param goodsSpuId 商品id
	 * @param ticket
	 * @param clientType
	 * @param ip 
	 * @param sign 
	 * @return
	 */
	public ResponseDTO getGoodsDetail(Long goodsSpuId, String ticket, Integer clientType, String sign, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (goodsSpuId == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("goodsSpuId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if(!SignUtil.httpEncrypt(ip, clientType, sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.107"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.107"));
			return responseDTO;
		}
		GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(goodsSpuId);
		GoodsSponsor goodsSponsor = goodsSponsorMapper.selectByPrimaryKey(goodsSpu.getSponsorId());
		GoodsDetailDTO goodsDetailDTO = new GoodsDetailDTO();
		goodsDetailDTO.setImageUrl(systemService.getImageUrls(goodsSpu.getImage2()));
		goodsDetailDTO.setGoodsId(goodsSpu.getId());
		goodsDetailDTO.setGoodsTitle(goodsSpu.getName());
		goodsDetailDTO.setDesc(goodsSpu.getBrief());
		goodsDetailDTO.setLimitation(goodsSpu.getLimitation());
		goodsDetailDTO.setOriginalPrice(this.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getOriginalprice().toString());
		goodsDetailDTO.setPresentPrice(this.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().toString());
		goodsDetailDTO.setPurchaseDesc(goodsSpu.getPurchasedesc());
		goodsDetailDTO.setSaleTotal(goodsSpu.getSaletotal());
		goodsDetailDTO.setSpec(goodsSpu.getSpec());
		goodsDetailDTO.setSponsorId(goodsSponsor.getId());
		goodsDetailDTO.setSponsorAddress(goodsSponsor.getAddress());
		goodsDetailDTO.setSponsorName(goodsSponsor.getName());
		goodsDetailDTO.setSponsorPhone(goodsSponsor.getPhone());
		goodsDetailDTO.setDetailHtmlUrl(goodsSpu.getDetailhtmlurl());
		goodsDetailDTO.setStatus(goodsSpu.getStatus());
		Map<String, Object> data = Maps.newHashMap();
		data.put("goodsDetailDTO", goodsDetailDTO);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_System_Msg, "emc.goods.getGoodsDetail.success"));
		return responseDTO;
	}
	
	/**
	 * 获取商户商品列表
	 * @param sponsorId
	 * @param countryId 
	 * @return
	 */
	public List<SponsorGoodsDTO> getGoodsBySponsor(long sponsorId, Long countryId) {

		PageHelper.startPage(1, 3);
		List<GoodsSpu> goodsSpuList = goodsSpuMapper.findGoodsSpuList(sponsorId, countryId, null);
		List<SponsorGoodsDTO> sponsorGoodsDTOs = Lists.newArrayList();
		SponsorGoodsDTO sponsorGoodsDTO = null;
		for (GoodsSpu goodsSpu : goodsSpuList) {
			sponsorGoodsDTO = new SponsorGoodsDTO();
			sponsorGoodsDTO.setDesc(goodsSpu.getBrief());
			sponsorGoodsDTO.setGoodsId(goodsSpu.getId());
			sponsorGoodsDTO.setGoodsTitle(goodsSpu.getName());
			sponsorGoodsDTO.setOriginalPrice(this.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getOriginalprice().toString());
			sponsorGoodsDTO.setPresentPrice(this.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().toString());
			sponsorGoodsDTO.setSaleTotal(goodsSpu.getSaletotal());
			sponsorGoodsDTO.setSpec(goodsSpu.getSpec());
			sponsorGoodsDTO.setImageUrl(systemService.getImageUrl(goodsSpu.getImage1()));
			sponsorGoodsDTOs.add(sponsorGoodsDTO);
		}

		return sponsorGoodsDTOs;
	}
	
	public static String goodsAmount(Long countryId){
		String redis = "";
		try {
			redis = RedisUtils.getValue(countryId.toString(), RedisMoudel.CountryAndCurrency);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AccountCountryDTO accountCountryDTO = mapper.fromJson(redis,AccountCountryDTO.class);
		return accountCountryDTO.getCurrencySign();
	}

}
