package com.lingdaoyi.cloud.entity.account;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_country")
public class AccountCountry {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "countryName")
	private String countryname;

	@Column(name = "cityName")
	private String cityname;

	private Integer code;

	@Column(name = "currencyName")
	private String currencyname;

	@Column(name = "currencySign")
	private String currencysign;

	@Column(name = "currencySort")
	private String currencysort;

	private Long sort;

	@Column(name = "gmt_create")
	private Date gmtCreate;

	@Column(name = "gmt_modified")
	private Date gmtModified;

	@Column(name = "is_deleted")
	private Integer isDeleted;
	
	@Column(name = "countryFlag")
	private String countryFlag;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}


	public String getCurrencyname() {
		return currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}

	public String getCurrencysign() {
		return currencysign;
	}

	public void setCurrencysign(String currencysign) {
		this.currencysign = currencysign;
	}

	public String getCurrencysort() {
		return currencysort;
	}

	public void setCurrencysort(String currencysort) {
		this.currencysort = currencysort;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCountryFlag() {
		return countryFlag;
	}

	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}
	
}