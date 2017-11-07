package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountInfoExtDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Long accountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String qrCode;

	private boolean isAuth = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nickname;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gender;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer age;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String career;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String income;

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

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

}
