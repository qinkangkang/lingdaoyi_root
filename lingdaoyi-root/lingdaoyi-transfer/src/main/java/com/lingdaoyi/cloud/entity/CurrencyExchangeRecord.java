package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "currency_exchange_record")
public class CurrencyExchangeRecord {

	// 主键ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 账户ID
	@Column(name = "account_id")
	private Long accountId;

	// 账户币种
	@Column(name = "account_currency_name")
	private String accountCurrencyName;

	// 账户币种缩写
	@Column(name = "account_currency_sort")
	private String accountCurrencySort;

	// 账户兑换金额
	@Column(name = "account_exchange_sum")
	private BigDecimal accountExchangeSum;

	// 账户兑换金额
	@Column(name = "account_currency_sign")
	private String accountCurrencySign;

	// 账户国旗URL
	@Column(name = "account_flag_url")
	private String accountFlagUrl;

	// 兑换币种
	@Column(name = "exchange_currency_name")
	private String exchangeCurrencyName;

	// 兑换币种缩写
	@Column(name = "exchange_currency_sort")
	private String exchangeCurrencySort;

	// 兑换金额
	@Column(name = "exchange_currency_sum")
	private BigDecimal exchangeCurrencySum;

	
	@Column(name = "exchange_currency_sign")
	private String exchangeCurrencySign;

	// 兑换国家国旗URL
	@Column(name = "exchange_flag_url")
	private String exchangeFlagUrl;

	// 汇率比例
	@Column(name = "exchange_rate_ratio")
	private String exchangeRateRatio;

	// 兑换时间
	@Column(name = "exchange_time")
	private Date exchangeTime;

	// 创建时间
	@Column(name = "gmt_create")
	private Date gmtCreate;

	// 修改时间
	@Column(name = "gmt_modified")
	private Date gmtModified;

	// 0 表示未删除 1 表示删除
	@Column(name = "is_deleted")
	private Integer isDeleted;

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

	public BigDecimal getAccountExchangeSum() {
		return accountExchangeSum;
	}

	public void setAccountExchangeSum(BigDecimal accountExchangeSum) {
		this.accountExchangeSum = accountExchangeSum;
	}

	public String getAccountCurrencySign() {
		return accountCurrencySign;
	}

	public void setAccountCurrencySign(String accountCurrencySign) {
		this.accountCurrencySign = accountCurrencySign;
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

	public BigDecimal getExchangeCurrencySum() {
		return exchangeCurrencySum;
	}

	public void setExchangeCurrencySum(BigDecimal exchangeCurrencySum) {
		this.exchangeCurrencySum = exchangeCurrencySum;
	}

	public String getExchangeCurrencySign() {
		return exchangeCurrencySign;
	}

	public void setExchangeCurrencySign(String exchangeCurrencySign) {
		this.exchangeCurrencySign = exchangeCurrencySign;
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

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}