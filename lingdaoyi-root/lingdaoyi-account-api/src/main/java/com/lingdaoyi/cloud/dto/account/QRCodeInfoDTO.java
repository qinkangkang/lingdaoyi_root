package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class QRCodeInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String error_code;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String error_msg;
	private boolean success = false;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String qrcode;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountId;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nickName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer qrcodeType;
	private boolean isAuth = false;
	public QRCodeInfoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	
	public boolean isAuth() {
		return isAuth;
	}
	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public Integer getQrcodeType() {
		return qrcodeType;
	}
	public void setQrcodeType(Integer qrcodeType) {
		this.qrcodeType = qrcodeType;
	}
	
}
