package com.lingdaoyi.cloud.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Cryptos;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.controller.OrderController;
import com.lingdaoyi.cloud.dto.AccountSysDTO;
import com.lingdaoyi.cloud.dto.BuyNowDTO;
import com.lingdaoyi.cloud.dto.GoodsDTO;
import com.lingdaoyi.cloud.dto.GoodsDetailDTO;
import com.lingdaoyi.cloud.dto.OrderDTO;
import com.lingdaoyi.cloud.dto.PageDTO;
import com.lingdaoyi.cloud.dto.PayTypeDTO;
import com.lingdaoyi.cloud.dto.ResponseDTO;
import com.lingdaoyi.cloud.dto.SponsorDTO;
import com.lingdaoyi.cloud.dto.SponsorGoodsDTO;
import com.lingdaoyi.cloud.dto.ViewOrderDTO;
import com.lingdaoyi.cloud.dto.account.AccountCountryDTO;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.entity.GoodsSponsor;
import com.lingdaoyi.cloud.entity.GoodsSpu;
import com.lingdaoyi.cloud.entity.OrderInfo;
import com.lingdaoyi.cloud.entity.OrderPayinfo;
import com.lingdaoyi.cloud.entity.OrderStatusChange;
import com.lingdaoyi.cloud.feign.TransferFeignClient;
import com.lingdaoyi.cloud.mapper.GoodsSponsorMapper;
import com.lingdaoyi.cloud.mapper.GoodsSpuMapper;
import com.lingdaoyi.cloud.mapper.OrderInfoMapper;
import com.lingdaoyi.cloud.mapper.OrderPayInfoMapper;
import com.lingdaoyi.cloud.mapper.OrderStatusChangeMapper;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.IpUtil;
import com.lingdaoyi.cloud.utils.NumberUtil;
import com.lingdaoyi.cloud.utils.SignUtil;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

import ch.qos.logback.core.encoder.ByteArrayUtil;

@Service
@Transactional
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private GoodsSpuMapper goodsSpuMapper;
	
	@Autowired
	private GoodsSponsorMapper goodsSponsorMapper;
	
	@Autowired
	private OrderInfoMapper orderInfoMapper;
	
	@Autowired
	private OrderPayInfoMapper orderPayInfoMapper;
	
	@Autowired
	private OrderStatusChangeMapper orderStatusChangeMapper;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private TransferFeignClient transferFeignClient;
	
	/**
	 * 立即购买信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO buyNow(String ticket, Long goodsSkuId, Integer num, Integer clientType, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (goodsSkuId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("goodsSkuId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (num == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("num" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		
		BuyNowDTO buyNowDTO = new BuyNowDTO();
		GoodsSpu goodsSpu =goodsSpuMapper.selectByPrimaryKey(goodsSkuId);
		buyNowDTO.setGoodsId(goodsSpu.getId());
		buyNowDTO.setGoodsTitle(goodsSpu.getName());
		buyNowDTO.setImageUrl(systemService.getImageUrl(goodsSpu.getImage1()));
		buyNowDTO.setNum(num);
		buyNowDTO.setPhone(accountSysDTO.getAccountName());
		buyNowDTO.setPresentPrice(GoodsService.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().toString());
		buyNowDTO.setSpec(goodsSpu.getSpec());
		buyNowDTO.setTotalPrice(GoodsService.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().multiply(new BigDecimal(num))
				.setScale(2, RoundingMode.HALF_UP).toString());
		
		returnData.put("buyNowDTO", buyNowDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.buyNow.success"));
		return responseDTO;
	}

	/**
	 * 提交订单信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO goSettlement(String ticket, Long goodsSpuId, Integer num, Integer clientType ,String ipAddr) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (goodsSpuId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("goodsSpuId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (num == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("num" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		
		BuyNowDTO buyNowDTO = new BuyNowDTO();
		GoodsSpu goodsSpu =goodsSpuMapper.selectByPrimaryKey(goodsSpuId);
		buyNowDTO.setGoodsId(goodsSpu.getId());
		buyNowDTO.setGoodsTitle(goodsSpu.getName());
		buyNowDTO.setImageUrl(systemService.getImageUrl(goodsSpu.getImage1()));
		buyNowDTO.setNum(num);
		buyNowDTO.setPhone(accountSysDTO.getAccountName());
		buyNowDTO.setPresentPrice(GoodsService.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().toString());
		buyNowDTO.setSpec(goodsSpu.getSpec());
		buyNowDTO.setTotalPrice(GoodsService.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getPresentprice().multiply(new BigDecimal(num))
				.setScale(2, RoundingMode.HALF_UP).toString());
		
		List<PayTypeDTO> payTypeList = Lists.newArrayList();
		PayTypeDTO payTypeDTO = null;
		
		Map<Integer, String> all = DictionaryUtil.getStatueMap(DictionaryUtil.PayType);
		for (Iterator it = all.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				payTypeDTO = new PayTypeDTO();
				payTypeDTO.setValue(e.getKey());
				payTypeDTO.setName(e.getValue().toString());
				payTypeDTO.setPayTypeurl(DictionaryUtil.getCode(DictionaryUtil.PayType, e.getKey()));
				payTypeList.add(payTypeDTO);
		}
		
		returnData.put("buyNowDTO", buyNowDTO);
		returnData.put("payTypeList", payTypeList);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.goSettlement.success"));
		return responseDTO;
	}

	/**
	 * 下单支付信息
	 * @param num 
	 * @param countryId 
	 * @param goodsSkuId 
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO toPayOrderApp(Integer clientType, String ticket, Integer payType, Long goodsSpuId, Integer num, 
			String ipAddr, String deviceId, Long countryId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		
		if (goodsSpuId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("goodsSpuId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		if (countryId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("countryId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(goodsSpuId);
		if(goodsSpu.getStatus()!=20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.101"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.101"));
			return responseDTO;
		}
		if(goodsSpu.getStock()-num<0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.102"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.102"));
			return responseDTO;
		}
		GoodsSponsor goodsSponsor = goodsSponsorMapper.selectByPrimaryKey(goodsSpu.getSponsorId());
		OrderInfo orderInfo = new OrderInfo();
		String orderNum = NumberUtil.getOrderNum(countryId);
		Date now = new Date();
		
		orderInfo.setAccountId(Long.parseLong(accountSysDTO.getAccountId()));
		orderInfo.setCustomername(accountSysDTO.getAccountName());
		orderInfo.setCustomerphone(accountSysDTO.getAccountName());
		orderInfo.setChannel(clientType);
		orderInfo.setCount(num);
		orderInfo.setCountryId(countryId);
		orderInfo.setCreatetime(now);
		orderInfo.setGoodsId(goodsSpu.getId());
		orderInfo.setGoodsname(goodsSpu.getName());
		orderInfo.setGoodsurl(systemService.getImageUrl(goodsSpu.getImage1()));
		orderInfo.setGps("");
		orderInfo.setOrdernum(orderNum);
		BigDecimal total = goodsSpu.getPresentprice().multiply(new BigDecimal(num));
		orderInfo.setOrdertotal(total);
		orderInfo.setReceivabletotal(total);
		//orderInfo.setRecipient(recipient);
		orderInfo.setSponsorfullname(goodsSponsor.getFullname());
		orderInfo.setSponsorId(goodsSponsor.getId());
		orderInfo.setSponsorname(goodsSponsor.getName());
		orderInfo.setSponsornumber(goodsSponsor.getNumber());
		orderInfo.setSponsorphone(goodsSponsor.getPhone());
		orderInfo.setStatus(10);
		Date fdate = null;
			fdate = DateUtils.addMinutes(now, 60* 24);
		
		orderInfo.setUnpayfailuretime(fdate);
		orderInfo.setVerificationcode(NumberUtil.getVerificationCode());
		orderInfoMapper.insert(orderInfo);
		
		goodsSpu.setTotal(goodsSpu.getTotal()-num);
		goodsSpu.setStock(goodsSpu.getStock()-num);
		goodsSpu.setSaletotal(goodsSpu.getSaletotal()+num);
		goodsSpuMapper.updateByPrimaryKey(goodsSpu);
		this.orderStatusChange(1, Long.parseLong(accountSysDTO.getAccountId()), orderInfo.getId(), "下单", 0, 10);
		
		
		returnData.put("orderId", orderInfo.getId());
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.toPayOrderApp.success"));
		return responseDTO;
	}

	/**
	 * 支付订单信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO appPay(Integer clientType, String ticket, Integer payType, Long orderId,Long countryId, String ipAddr, 
			String fingerprintSuccess,String password) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		
		if (StringUtils.isEmpty(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		if (orderId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("orderId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		if (payType==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("payType" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		
		OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
		if(orderInfo==null || orderInfo.getStatus()!=10) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.103"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.103"));
			return responseDTO;
		}
		
		OrderPayinfo orderPayinfo = new OrderPayinfo();
		orderPayinfo.setAccountId(Long.parseLong(accountSysDTO.getAccountId()));
		orderPayinfo.setChannel(DictionaryUtil.getString(DictionaryUtil.PayType, payType));
		orderPayinfo.setClienttype(clientType);
		orderPayinfo.setConfirmpaytime(new Date());
		orderPayinfo.setCreatetime(new Date());
		orderPayinfo.setCurrencytype("");
		orderPayinfo.setPayinout(1);
		orderPayinfo.setOrdernum(orderInfo.getOrdernum());
		orderPayinfo.setOrderId(orderInfo.getId());
		//orderPayinfo.setOrdertype(ordertype);
		orderPayinfo.setPayamount(orderInfo.getOrdertotal());
		orderPayinfo.setPaytype(payType);
		orderPayinfo.setStatus(orderInfo.getStatus());
		orderPayInfoMapper.insert(orderPayinfo);
		
		orderInfo.setStatus(20);
		
		//TODO
		if(payType==1) {
			responseDTO = this.orderTransfer(accountSysDTO.getAccountId(), orderPayinfo,
					countryId, ticket, clientType, password,fingerprintSuccess,ipAddr);
			if(!responseDTO.getStatusCode().equals("200")) {
				return responseDTO;
			}
		}
		orderInfo.setPaytype(payType);
		orderInfo.setPaytime(new Date());
		orderInfoMapper.updateByPrimaryKey(orderInfo);
		this.orderStatusChange(2, Long.parseLong(accountSysDTO.getAccountId()), orderInfo.getId(), "支付订单", 10, 20);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.appPay.success"));
		return responseDTO;
	}

	/**
	 * 获取订单列表信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO getOrderList(Integer clientType, String ticket, Integer status, Integer pageSize,
			Integer pageNum) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("ticket" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		PageHelper.startPage(pageNum, pageSize);
		List<OrderInfo> orderInfoList = orderInfoMapper.findOrderList(status, accountSysDTO.getAccountId());
		
		List<OrderDTO> orderDTOList = Lists.newArrayList();
		OrderDTO orderDTO = null;
		for(OrderInfo orderInfo:orderInfoList) {
			orderDTO = new OrderDTO();
			orderDTO.setGoodsTitle(orderInfo.getGoodsname());
			orderDTO.setImageUrl(orderInfo.getGoodsurl());
			orderDTO.setGoodsTitle(orderInfo.getGoodsname());
			orderDTO.setOrderId(orderInfo.getId());
			orderDTO.setStatus(orderInfo.getStatus());
			orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, orderInfo.getStatus()));
			orderDTOList.add(orderDTO);
		}
		PageDTO pageDTO = new PageDTO<OrderInfo>(orderInfoList);
		pageDTO.setList(orderDTOList);
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.getOrderList.success"));
		return responseDTO;
	}

	/**
	 * 获取订单详情信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseDTO viewOrder(Integer clientType, String ticket, Long orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		
		if (orderId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			responseDTO.setMsg("orderId" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return responseDTO;
		}
		
		ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
		OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
		if(orderInfo==null ) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.105"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.105"));
			return responseDTO;
		}
		GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(orderInfo.getGoodsId());
		GoodsSponsor goodsSponsor = goodsSponsorMapper.selectByPrimaryKey(orderInfo.getSponsorId());
		if(orderInfo.getUnpayfailuretime()!=null) {
			Integer time = Seconds.secondsBetween(new DateTime(new Date()), new DateTime
					(orderInfo.getUnpayfailuretime())).getSeconds();
			if(time<=0){
				time = 0;
			}
			viewOrderDTO.setCancelTime(time.toString());
		}
		if(orderInfo.getVerificationtime()!=null) {
			viewOrderDTO.setConfirmationTime(DateFormatUtils.format(orderInfo.getVerificationtime(), "yyyy-MM-dd HH:mm"));
		}
		viewOrderDTO.setOrderId(orderId);
		viewOrderDTO.setOrderNum(orderInfo.getOrdernum());
		if(orderInfo.getCreatetime()!=null) {
			viewOrderDTO.setOrderTime(DateFormatUtils.format(orderInfo.getCreatetime(), "yyyy-MM-dd HH:mm"));
		}if(orderInfo.getPaytime()!=null) {
			viewOrderDTO.setPayTime(DateFormatUtils.format(orderInfo.getPaytime(), "yyyy-MM-dd HH:mm"));
			viewOrderDTO.setPayType(orderInfo.getPaytype());
			viewOrderDTO.setPayTypeString(DictionaryUtil.getString(DictionaryUtil.PayType, orderInfo.getPaytype()));
		}
		viewOrderDTO.setSponsorAddress(goodsSponsor.getAddress());
		viewOrderDTO.setSponsorGps(goodsSponsor.getGps());
		viewOrderDTO.setSponsorName(goodsSponsor.getName());
		viewOrderDTO.setSponsorPhone(goodsSponsor.getPhone());
		viewOrderDTO.setStatus(orderInfo.getStatus());
		//viewOrderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, orderInfo.getStatus()));
		viewOrderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, orderInfo.getStatus()));
		viewOrderDTO.setVerificationCode(orderInfo.getVerificationcode());
		viewOrderDTO.setVerificationCodeAuto(this.ordercode(orderInfo, goodsSponsor));
		
		viewOrderDTO.setGoodsId(goodsSpu.getId().toString());
		viewOrderDTO.setGoodsName(goodsSpu.getName());
		viewOrderDTO.setGoodsNum(orderInfo.getCount());
		viewOrderDTO.setGoodsImage(systemService.getImageUrl(goodsSpu.getImage1()));
		viewOrderDTO.setPrice(GoodsService.goodsAmount(goodsSpu.getCountryId())+goodsSpu.getOriginalprice().toString());
		viewOrderDTO.setOrderTotal(GoodsService.goodsAmount(goodsSpu.getCountryId())+orderInfo.getOrdertotal().toString());
		
		returnData.put("viewOrderDTO", viewOrderDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.viewOrder.success"));
		return responseDTO;
	}
	
	public String ordercode(OrderInfo orderInfo,GoodsSponsor goodsSponsor){
		// 商家编号作为加密的秘钥
		String fsponsorNumber = goodsSponsor.getNumber();
		// 生成自动核销编码
		StringBuilder allKey = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			allKey.append(fsponsorNumber);
		}
		logger.info("allKey：" + allKey);
		String orderNumAndSponsorNum = new StringBuilder().append(orderInfo.getOrdernum()).append("###")
				.append(fsponsorNumber).toString();
		logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
		String verificationCodeAuto = null;
		try {
			verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(orderNumAndSponsorNum.getBytes(),
					allKey.substring(0, 16).toString().getBytes()));
			logger.info("加密结果：" + verificationCodeAuto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verificationCodeAuto;
	}
	
	public void orderStatusChange(int operatorType, long operatorId, long orderId, String changeReason,
			int beforeStatus, int afterStatus) {
		Date now = new Date();

		OrderStatusChange orderStatusChange = new OrderStatusChange();
		orderStatusChange.setAfterstatus(afterStatus);
		orderStatusChange.setBeforestatus(beforeStatus);
		orderStatusChange.setChangereason(changeReason);
		orderStatusChange.setCreatetime(now);
		orderStatusChange.setOperatorid(operatorId);
		orderStatusChange.setOperatortype(operatorType);
		orderStatusChange.setOrderId(orderId);
		orderStatusChange.setUpdatetime(now);
		orderStatusChangeMapper.insert(orderStatusChange);
	}
	
	public ResponseDTO orderTransfer(String receiveAccountId, OrderPayinfo orderPayinfo,Long countryId,String ticket,Integer clientType,
			String password,String fingerprintSuccess,String ipAddr) {
		ResponseDTO responseDTO = new ResponseDTO();
		StringBuilder sb = new StringBuilder();
		sb.append(clientType).append(ipAddr).append(4).append(receiveAccountId).append(orderPayinfo.getPayamount());
		if(StringUtils.isNoneBlank(password)){
			sb.append(password);
			try {
				password = DesUtil3.encryptThreeDESECB(password, DesUtil3.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String dto = "";
		try {
		String redis = RedisUtils.getValue(countryId.toString(), RedisMoudel.CountryAndCurrency);
		AccountCountryDTO accountCountryDTO = mapper.fromJson(redis,AccountCountryDTO.class);
		String sign = DesUtil3.encryptThreeDESECB(sb.toString(), DesUtil3.KEY);
		dto = transferFeignClient.orderTransfer(DesUtil3.encryptThreeDESECB("4", DesUtil3.KEY),
				ticket, 
				clientType, 
				DesUtil3.encryptThreeDESECB(orderPayinfo.getPayamount().toString(), DesUtil3.KEY),
				DesUtil3.encryptThreeDESECB(receiveAccountId.toString(), DesUtil3.KEY),
				password,
				DesUtil3.encryptThreeDESECB(accountCountryDTO.getCurrencySort(), DesUtil3.KEY)
				, DesUtil3.encryptThreeDESECB(accountCountryDTO.getCurrencySort(), DesUtil3.KEY), 
				orderPayinfo.getCreatetime().toString(), 
				DesUtil3.encryptThreeDESECB("1", DesUtil3.KEY), 
				"下单支付", 
				DesUtil3.encryptThreeDESECB(orderPayinfo.getPayamount().toString(), DesUtil3.KEY), 
				fingerprintSuccess,
				sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseDTO = mapper.fromJson(dto, ResponseDTO.class);
		return responseDTO;
	}

	public ResponseDTO cancelPayOrder(Integer clientType, String ticket, Long orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		AccountSysDTO accountSysDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			accountSysDTO = systemService.getAccountByTicket(ticket, clientType);
			if (!accountSysDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(accountSysDTO.getStatusCode());
				responseDTO.setMsg(accountSysDTO.getMsg());
				return responseDTO;
			}
		}
		
		Integer afterStatus = 0;
		Integer beforestatus = 0;
		
		OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
		if(orderInfo==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.105"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.105"));
			return responseDTO;
		}else if(orderInfo.getStatus()==10){
			afterStatus = 110;
			beforestatus = 10;
		}else if(orderInfo.getStatus()==20){
			afterStatus = 100;
			beforestatus = 20;
		}else{
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.105"));
			responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.105"));
			return responseDTO;
		}
		
		orderInfo.setStatus(afterStatus);
		orderInfoMapper.updateByPrimaryKey(orderInfo);
		this.orderStatusChange(2, Long.parseLong(accountSysDTO.getAccountId()), orderInfo.getId(), "取消订单", beforestatus, afterStatus);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.200"));
		responseDTO.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Order_Msg, "emc.order.refundOrder.success"));
		return responseDTO;
	}


	
}
