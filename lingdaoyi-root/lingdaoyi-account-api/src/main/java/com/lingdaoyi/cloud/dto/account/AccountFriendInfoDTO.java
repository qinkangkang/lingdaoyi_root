package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountFriendInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;

	private boolean isFriends;

	private boolean isAuth;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public boolean isFriends() {
		return isFriends;
	}

	public void setFriends(boolean isFriends) {
		this.isFriends = isFriends;
	}

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

}
