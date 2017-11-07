package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountRecordDTO implements Serializable {

	private Long accountRecordId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	String month;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountLoginName;// 当前用户的用户名
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountRealName; // 当前账户的真实名

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String receiveAccountLoginName; // 收账账户的用户名

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String receiveAccountRealName; // 收账账户的用户名

	private BigDecimal transferMoney;// 交易金额

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payNo;// 关联订单id(用于购物扣款)
	private Integer type;// 0:充值,1:转账;2:收账;3:提现

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountHeadUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transferState;// 交易状态;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String remarks;// 备注信息;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transferWay;// 支付方式;余额或银行卡等
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String receivedTime;//到账时间
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createMonthAndDay;//创建的时间格式:月-日;
	
	
	
	

	

	public String getAccountHeadUrl() {
		return accountHeadUrl;
	}

	public void setAccountHeadUrl(String accountHeadUrl) {
		this.accountHeadUrl = accountHeadUrl;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getAccountRealName() {
		return accountRealName;
	}

	public void setAccountRealName(String accountRealName) {
		this.accountRealName = accountRealName;
	}

	public String getReceiveAccountLoginName() {
		return receiveAccountLoginName;
	}

	public void setReceiveAccountLoginName(String receiveAccountLoginName) {
		this.receiveAccountLoginName = receiveAccountLoginName;
	}

	public String getReceiveAccountRealName() {
		return receiveAccountRealName;
	}

	public void setReceiveAccountRealName(String receiveAccountRealName) {
		this.receiveAccountRealName = receiveAccountRealName;
	}

	public void setAccountLoginName(String accountLoginName) {
		this.accountLoginName = accountLoginName;
	}

	public String getAccountLoginName() {
		return accountLoginName;
	}

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class) // 转账说明
	private String transferInstructions;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createtTime;// 创建时间

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transferReason;// 转账说明

	public Long getAccountRecordId() {
		return accountRecordId;
	}

	public void setAccountRecordId(Long accountRecordId) {
		this.accountRecordId = accountRecordId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(BigDecimal transferMoney) {
		this.transferMoney = transferMoney;
	}

	public String getTransferInstructions() {
		return transferInstructions;
	}

	public void setTransferInstructions(String transferInstructions) {
		this.transferInstructions = transferInstructions;
	}

	public String getCreatetTime() {
		return createtTime;
	}

	public void setCreatetTime(String createtTime) {
		this.createtTime = createtTime;
	}

	public String getTransferState() {
		return transferState;
	}

	public void setTransferState(String transferState) {
		this.transferState = transferState;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

	public String getTransferWay() {
		return transferWay;
	}

	public void setTransferWay(String transferWay) {
		this.transferWay = transferWay;
	}

	

	public String getBankImage() {
		return bankImage;
	}

	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTransferReason() {
		return transferReason;
	}

	public void setTransferReason(String transferReason) {
		this.transferReason = transferReason;
	}

	public String getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public String getCreateMonthAndDay() {
		return createMonthAndDay;
	}

	public void setCreateMonthAndDay(String createMonthAndDay) {
		this.createMonthAndDay = createMonthAndDay;
	}

	
	
	

}