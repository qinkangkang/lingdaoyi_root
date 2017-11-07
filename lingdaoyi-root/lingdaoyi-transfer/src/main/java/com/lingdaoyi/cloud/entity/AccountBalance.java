package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "account_balance")
public class AccountBalance  {
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;// 账户id

	@Column
	private BigDecimal balance;//账户余额
	
	@Column(name = "gmt_create")
	private Date gmtCreate; // 创建时间

	@Column(name = "gmt_modified")
	private Date gmtModified; // 修改时间

	@Column(name = "is_deleted")
	private String isDeleted; // 逻辑删除（1删除，0未删除）
	
	@Column(name = "locking_money")
	private BigDecimal lockingMoney; // 逻辑删除（1删除，0未删除）
	
	@Column(name="currency_name")
	private String currencySign;//货币名称
	
	public BigDecimal getLockingMoney() {
		return lockingMoney;
	}

	public void setLockingMoney(BigDecimal lockingMoney) {
		this.lockingMoney = lockingMoney;
	}

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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

}
