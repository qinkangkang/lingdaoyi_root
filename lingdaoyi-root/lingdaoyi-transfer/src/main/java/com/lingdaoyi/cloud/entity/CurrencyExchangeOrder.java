package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "currency_exchange_order")
public class CurrencyExchangeOrder  {
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;// 账户id
	
	@Column(name = "ordernum")
	private String ordernum;
	
	@Column(name = "from_currency")
	private String fromCurrency;
	
	@Column(name = "from_amount")
	private BigDecimal fromAmount;
	
	@Column(name = "to_currency")
	private String toCurrency;
	
	@Column(name = "to_amount")
	private BigDecimal toAmount;
	
	@Column(name = "rate")
	private BigDecimal rate;
	
	@Column(name = "exchange_time")
	private Date exchangeTime;
	
	@Column(name = "sponsor_id")
	private Long sponsorId;
	
	@Column(name = "old_balance")
	private BigDecimal oldBalance;
	
	@Column(name = "new_balance")
	private BigDecimal newBalance;
	
	@Column(name = "fee")
	private BigDecimal fee;//手续费
	
	@Column(name = "order_type")
	private Integer orderType;
	
	@Column(name = "gmt_create")
	private Date gmtCreate; // 创建时间

	@Column(name = "gmt_modified")
	private Date gmtModified; // 修改时间

	@Column(name = "is_deleted")
	private Integer isDeleted; // 逻辑删除（1删除，0未删除）

	public Long getId() {
		return id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public Long getSponsorId() {
		return sponsorId;
	}

	public BigDecimal getOldBalance() {
		return oldBalance;
	}

	public BigDecimal getNewBalance() {
		return newBalance;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public void setSponsorId(Long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public void setOldBalance(BigDecimal oldBalance) {
		this.oldBalance = oldBalance;
	}

	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	
}
