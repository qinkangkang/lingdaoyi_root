package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class ReceiptQRcodeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String error_code;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String error_msg;
	private boolean success = false;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountId;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String money;
	private Integer receiptType;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nickName;
	public ReceiptQRcodeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}
