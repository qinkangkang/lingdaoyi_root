package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class AccountSysDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msg;// 提示消息

	private boolean enable = false;// 用户是否可用

	private String statusCode = StringUtils.EMPTY;

	private String accountId;

	private String accountName;
	
	private String accountRealName;
	
	private String accountHeadUrl;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountHeadUrl() {
		return accountHeadUrl;
	}

	public void setAccountHeadUrl(String accountHeadUrl) {
		this.accountHeadUrl = accountHeadUrl;
	}

	public String getAccountRealName() {
		return accountRealName;
	}

	public void setAccountRealName(String accountRealName) {
		this.accountRealName = accountRealName;
	}
	
	

}