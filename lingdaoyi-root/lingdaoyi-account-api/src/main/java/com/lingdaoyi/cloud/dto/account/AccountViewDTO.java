package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountViewDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	private boolean isAuth = false;

	private boolean isSetPayPwd = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nikeName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sex;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer age;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String career;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String income;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Long countryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	// 货币缩写
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySort;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencysign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryFlag;

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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public boolean isSetPayPwd() {
		return isSetPayPwd;
	}

	public void setisSetPayPwd(boolean isSetPayPwd) {
		this.isSetPayPwd = isSetPayPwd;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getCurrencysign() {
		return currencysign;
	}

	public void setCurrencysign(String currencysign) {
		this.currencysign = currencysign;
	}

	public String getCountryFlag() {
		return countryFlag;
	}

	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrencySort() {
		return currencySort;
	}

	public void setCurrencySort(String currencySort) {
		this.currencySort = currencySort;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
