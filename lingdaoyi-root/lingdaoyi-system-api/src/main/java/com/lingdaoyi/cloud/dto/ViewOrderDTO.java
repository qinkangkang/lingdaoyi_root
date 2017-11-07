package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;


public class ViewOrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorPhone;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorAddress;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorGps;

	private int status = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	private int payType = 0;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payTypeString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String verificationCode;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String verificationCodeAuto;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String confirmationTime;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cancelTime = "0";
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String price;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderTotal;

	private int goodsNum = 0;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getSponsorPhone() {
		return sponsorPhone;
	}

	public void setSponsorPhone(String sponsorPhone) {
		this.sponsorPhone = sponsorPhone;
	}

	public String getSponsorAddress() {
		return sponsorAddress;
	}

	public void setSponsorAddress(String sponsorAddress) {
		this.sponsorAddress = sponsorAddress;
	}

	public String getSponsorGps() {
		return sponsorGps;
	}

	public void setSponsorGps(String sponsorGps) {
		this.sponsorGps = sponsorGps;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getVerificationCodeAuto() {
		return verificationCodeAuto;
	}

	public void setVerificationCodeAuto(String verificationCodeAuto) {
		this.verificationCodeAuto = verificationCodeAuto;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getConfirmationTime() {
		return confirmationTime;
	}

	public void setConfirmationTime(String confirmationTime) {
		this.confirmationTime = confirmationTime;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getPayTypeString() {
		return payTypeString;
	}

	public void setPayTypeString(String payTypeString) {
		this.payTypeString = payTypeString;
	}

}