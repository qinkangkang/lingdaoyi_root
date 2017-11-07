package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class OffLineQRCodeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String qrcode;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountId;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountLoginName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountName;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer qrcodeType;
	public OffLineQRCodeDTO() {
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
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getAccountLoginName() {
		return accountLoginName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountLoginName(String accountLoginName) {
		this.accountLoginName = accountLoginName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Integer getQrcodeType() {
		return qrcodeType;
	}
	public void setQrcodeType(Integer qrcodeType) {
		this.qrcodeType = qrcodeType;
	}
	
}
