package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class CountryFlagDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySort;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryFlag;
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currencySign;
	public String getCurrencySort() {
		return currencySort;
	}
	public String getCountryFlag() {
		return countryFlag;
	}
	public void setCurrencySort(String currencySort) {
		this.currencySort = currencySort;
	}
	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}
	public String getCurrencySign() {
		return currencySign;
	}
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}
	
	
}
