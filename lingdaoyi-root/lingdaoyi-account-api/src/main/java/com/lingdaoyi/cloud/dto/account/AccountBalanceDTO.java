package com.lingdaoyi.cloud.dto.account;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class AccountBalanceDTO {
	
	private Long acount_id;
	private BigDecimal balance;
	
	public Long getAcount_id() {
		return acount_id;
	}
	public void setAcount_id(Long acount_id) {
		this.acount_id = acount_id;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	

	

}
