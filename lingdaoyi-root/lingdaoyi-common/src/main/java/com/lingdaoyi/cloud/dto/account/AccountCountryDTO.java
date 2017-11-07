package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountCountryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long countryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cityName;

	private Integer code = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencyName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySort;

	private Long sort;

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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

}
