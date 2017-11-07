package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountExchangerateDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private BigDecimal orgMoney;
	private BigDecimal transferMoney;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orgCurrency;// 当前货币
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transCurrency;// 要转换的货币

	private BigDecimal exchangeratevalue;// 当前汇率
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gmtCreate;// 当前时间
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getOrgMoney() {
		return orgMoney;
	}

	public void setOrgMoney(BigDecimal orgMoney) {
		this.orgMoney = orgMoney;
	}

	public BigDecimal getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(BigDecimal transferMoney) {
		this.transferMoney = transferMoney;
	}

	public String getOrgCurrency() {
		return orgCurrency;
	}

	public void setOrgCurrency(String orgCurrency) {
		this.orgCurrency = orgCurrency;
	}

	public String getTransCurrency() {
		return transCurrency;
	}

	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}

	public BigDecimal getExchangeratevalue() {
		return exchangeratevalue;
	}

	public void setExchangeratevalue(BigDecimal exchangeratevalue) {
		this.exchangeratevalue = exchangeratevalue;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

}