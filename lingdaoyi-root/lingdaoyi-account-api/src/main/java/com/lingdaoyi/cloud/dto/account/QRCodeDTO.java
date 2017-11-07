package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class QRCodeDTO implements Serializable {

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
	private Integer qrcodeType;
	public QRCodeDTO() {
		super();
		// TODO Auto-generated constructor stub
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
