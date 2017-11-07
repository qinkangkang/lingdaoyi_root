package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountCountryInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long countryId;
	
	private String statusCode;
	private boolean success = false;
	private String msg;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencyName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySort;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryFlag;
	
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

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	public String getCurrencySort() {
		return currencySort;
	}

	public void setCurrencySort(String currencySort) {
		this.currencySort = currencySort;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMsg() {
		return msg;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCountryFlag() {
		return countryFlag;
	}

	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}
	
}
