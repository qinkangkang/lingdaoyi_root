package com.lingdaoyi.cloud.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class AccountBalanceDTO {
	
	private Long acountId;
	private BigDecimal balance;
	private String CurrencySign;
	
	public Long getAcountId() {
		return acountId;
	}
	public void setAcountId(Long acountId) {
		this.acountId = acountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCurrencySign() {
		return CurrencySign;
	}
	public void setCurrencySign(String currencySign) {
		CurrencySign = currencySign;
	}

	

	

}
