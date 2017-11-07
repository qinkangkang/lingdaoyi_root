package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_exchangeRate")
public class AccountExchangerate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 原本的货币
	 */
	@Column(name = "org_currency")
	private String orgCurrency;

	/**
	 * 要转成的货币
	 */
	@Column(name = "trans_currency")
	private String transCurrency;

	/**
	 * 汇率
	 */
	@Column(name = "exchangeRateValue")
	private BigDecimal exchangeratevalue;

	/**
	 * 当前时间
	 */
	@Column(name = "gmt_create")
	private Date gmtCreate;

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取原本的货币
	 *
	 * @return org_currency - 原本的货币
	 */
	public String getOrgCurrency() {
		return orgCurrency;
	}

	/**
	 * 设置原本的货币
	 *
	 * @param orgCurrency
	 *            原本的货币
	 */
	public void setOrgCurrency(String orgCurrency) {
		this.orgCurrency = orgCurrency;
	}

	/**
	 * 获取要转成的货币
	 *
	 * @return trans_currency - 要转成的货币
	 */
	public String getTransCurrency() {
		return transCurrency;
	}

	/**
	 * 设置要转成的货币
	 *
	 * @param transCurrency
	 *            要转成的货币
	 */
	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}

	/**
	 * 获取汇率
	 *
	 * @return exchangeRateValue - 汇率
	 */
	public BigDecimal getExchangeratevalue() {
		return exchangeratevalue;
	}

	/**
	 * 设置汇率
	 *
	 * @param exchangeratevalue
	 *            汇率
	 */
	public void setExchangeratevalue(BigDecimal exchangeratevalue) {
		this.exchangeratevalue = exchangeratevalue;
	}

	/**
	 * 获取当前时间
	 *
	 * @return gmt_create - 当前时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * 设置当前时间
	 *
	 * @param gmtCreate
	 *            当前时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AccountExchangerate [id=" + id + ", orgCurrency=" + orgCurrency + ", getGmtCreate=" + gmtCreate + "]";
	}
}