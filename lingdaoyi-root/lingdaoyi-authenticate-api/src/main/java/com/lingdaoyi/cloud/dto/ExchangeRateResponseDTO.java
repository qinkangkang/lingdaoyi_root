package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class ExchangeRateResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long fromCountryId;
	
	private Long toCountryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromCountryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromCurrencyName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromCurrencySign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromCurrencySort;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromCountryFlag;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toCountryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toCurrencyName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toCurrencySign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toCurrencySort;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toCountryFlag;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String fromAmount;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String toAmount;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String rate;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String updateTime;

	public Long getFromCountryId() {
		return fromCountryId;
	}

	public Long getToCountryId() {
		return toCountryId;
	}

	public String getFromCountryName() {
		return fromCountryName;
	}

	public String getFromCurrencyName() {
		return fromCurrencyName;
	}

	public String getFromCurrencySign() {
		return fromCurrencySign;
	}

	public String getFromCurrencySort() {
		return fromCurrencySort;
	}

	public String getFromCountryFlag() {
		return fromCountryFlag;
	}

	public String getToCountryName() {
		return toCountryName;
	}

	public String getToCurrencyName() {
		return toCurrencyName;
	}

	public String getToCurrencySign() {
		return toCurrencySign;
	}

	public String getToCurrencySort() {
		return toCurrencySort;
	}

	public String getToCountryFlag() {
		return toCountryFlag;
	}

	public String getFromAmount() {
		return fromAmount;
	}

	public String getToAmount() {
		return toAmount;
	}

	public String getRate() {
		return rate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setFromCountryId(Long fromCountryId) {
		this.fromCountryId = fromCountryId;
	}

	public void setToCountryId(Long toCountryId) {
		this.toCountryId = toCountryId;
	}

	public void setFromCountryName(String fromCountryName) {
		this.fromCountryName = fromCountryName;
	}

	public void setFromCurrencyName(String fromCurrencyName) {
		this.fromCurrencyName = fromCurrencyName;
	}

	public void setFromCurrencySign(String fromCurrencySign) {
		this.fromCurrencySign = fromCurrencySign;
	}

	public void setFromCurrencySort(String fromCurrencySort) {
		this.fromCurrencySort = fromCurrencySort;
	}

	public void setFromCountryFlag(String fromCountryFlag) {
		this.fromCountryFlag = fromCountryFlag;
	}

	public void setToCountryName(String toCountryName) {
		this.toCountryName = toCountryName;
	}

	public void setToCurrencyName(String toCurrencyName) {
		this.toCurrencyName = toCurrencyName;
	}

	public void setToCurrencySign(String toCurrencySign) {
		this.toCurrencySign = toCurrencySign;
	}

	public void setToCurrencySort(String toCurrencySort) {
		this.toCurrencySort = toCurrencySort;
	}

	public void setToCountryFlag(String toCountryFlag) {
		this.toCountryFlag = toCountryFlag;
	}

	public void setFromAmount(String fromAmount) {
		this.fromAmount = fromAmount;
	}

	public void setToAmount(String toAmount) {
		this.toAmount = toAmount;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
