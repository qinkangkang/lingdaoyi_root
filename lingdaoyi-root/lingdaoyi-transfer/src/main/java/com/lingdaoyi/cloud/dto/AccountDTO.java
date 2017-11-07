package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	private boolean isAuth = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;

	private Long countryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	// 货币缩写
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySort;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencyName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String email;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String laborUrl;

	private Integer accountLevel = 0;

	private BigDecimal balance = BigDecimal.ZERO;

	private Integer status = 0;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payPassword;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ticket;

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

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCurrencySort() {
		return currencySort;
	}

	public void setCurrencySort(String currencySort) {
		this.currencySort = currencySort;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLaborUrl() {
		return laborUrl;
	}

	public void setLaborUrl(String laborUrl) {
		this.laborUrl = laborUrl;
	}

	public Integer getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(Integer accountLevel) {
		this.accountLevel = accountLevel;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
