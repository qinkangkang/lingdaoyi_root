package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountInfoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Long accountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String qrCode;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nickname;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gender;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String area;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String signature;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer age;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String career;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String income;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String deviceId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String deviceTokens;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date gmtCreate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date gmtModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceTokens() {
		return deviceTokens;
	}

	public void setDeviceTokens(String deviceTokens) {
		this.deviceTokens = deviceTokens;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	
	
}