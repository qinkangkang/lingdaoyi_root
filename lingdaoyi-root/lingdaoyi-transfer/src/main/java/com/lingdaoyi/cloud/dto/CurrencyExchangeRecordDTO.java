package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class CurrencyExchangeRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Long recordId;

	// 账户ID
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Long accountId;

	// 账户币种
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountCurrencyName;

	// 账户币种缩写
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountCurrencySort;

	// 账户兑换金额
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountExchangeSum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountCurrencySign;

	// 账户国旗URL
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountFlagUrl;

	// 兑换币种
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeCurrencyName;

	// 兑换币种缩写
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeCurrencySort;

	// 兑换金额
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeCurrencySum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeCurrencySign;

	// 兑换国家国旗URL
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeFlagUrl;

	// 汇率比例
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeRateRatio;

	// 兑换时间
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exchangeTime;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountCurrencyName() {
		return accountCurrencyName;
	}

	public void setAccountCurrencyName(String accountCurrencyName) {
		this.accountCurrencyName = accountCurrencyName;
	}

	public String getAccountCurrencySort() {
		return accountCurrencySort;
	}

	public void setAccountCurrencySort(String accountCurrencySort) {
		this.accountCurrencySort = accountCurrencySort;
	}

	public String getAccountExchangeSum() {
		return accountExchangeSum;
	}

	public void setAccountExchangeSum(String accountExchangeSum) {
		this.accountExchangeSum = accountExchangeSum;
	}

	public String getAccountFlagUrl() {
		return accountFlagUrl;
	}

	public void setAccountFlagUrl(String accountFlagUrl) {
		this.accountFlagUrl = accountFlagUrl;
	}

	public String getExchangeCurrencyName() {
		return exchangeCurrencyName;
	}

	public void setExchangeCurrencyName(String exchangeCurrencyName) {
		this.exchangeCurrencyName = exchangeCurrencyName;
	}

	public String getExchangeCurrencySort() {
		return exchangeCurrencySort;
	}

	public void setExchangeCurrencySort(String exchangeCurrencySort) {
		this.exchangeCurrencySort = exchangeCurrencySort;
	}

	public String getExchangeCurrencySum() {
		return exchangeCurrencySum;
	}

	public void setExchangeCurrencySum(String exchangeCurrencySum) {
		this.exchangeCurrencySum = exchangeCurrencySum;
	}

	public String getExchangeFlagUrl() {
		return exchangeFlagUrl;
	}

	public void setExchangeFlagUrl(String exchangeFlagUrl) {
		this.exchangeFlagUrl = exchangeFlagUrl;
	}

	public String getExchangeRateRatio() {
		return exchangeRateRatio;
	}

	public void setExchangeRateRatio(String exchangeRateRatio) {
		this.exchangeRateRatio = exchangeRateRatio;
	}

	public String getAccountCurrencySign() {
		return accountCurrencySign;
	}

	public void setAccountCurrencySign(String accountCurrencySign) {
		this.accountCurrencySign = accountCurrencySign;
	}

	public String getExchangeCurrencySign() {
		return exchangeCurrencySign;
	}

	public void setExchangeCurrencySign(String exchangeCurrencySign) {
		this.exchangeCurrencySign = exchangeCurrencySign;
	}

	public String getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

}
