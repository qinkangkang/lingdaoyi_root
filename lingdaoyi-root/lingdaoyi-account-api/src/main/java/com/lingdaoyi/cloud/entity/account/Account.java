package com.lingdaoyi.cloud.entity.account;

import java.util.Date;
import javax.persistence.*;

public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_name")
	private String loginName;

	@Column(name = "head_url")
	private String headUrl;

	@Column(name = "is_auth")
	private Integer isAuth;

	@Column(name = "real_name")
	private String realName;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "currency_name")
	private String currencyName;

	@Column(name = "identity_id")
	private Long identityId;

	@Column(name = "email")
	private String email;

	@Column(name = "labor_url")
	private String laborUrl;

	@Column(name = "account_level")
	private Integer accountLevel;

	@Column(name = "login_password")
	private String loginPassword;

	@Column(name = "pay_password")
	private String payPassword;

	private String transferkey;

	@Column(name = "fingerprint_pay")
	private Integer fingerprintPay;

	@Column(name = "account_type")
	private Integer accountType;

	@Column(name = "status")
	private Integer status;

	@Column(name = "client_type")
	private Integer clientType;

	@Column(name = "gmt_create")
	private Date gmtCreate;

	@Column(name = "gmt_modified")
	private Date gmtModified;

	@Column(name = "is_deleted")
	private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Long getIdentityId() {
		return identityId;
	}

	public void setIdentityId(Long identityId) {
		this.identityId = identityId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLaborUrl() {
		return laborUrl;
	}

	public void setLaborUrl(String laborUrl) {
		this.laborUrl = laborUrl;
	}

	public Integer getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(Integer accountLevel) {
		this.accountLevel = accountLevel;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getTransferkey() {
		return transferkey;
	}

	public void setTransferkey(String transferkey) {
		this.transferkey = transferkey;
	}

	public Integer getFingerprintPay() {
		return fingerprintPay;
	}

	public void setFingerprintPay(Integer fingerprintPay) {
		this.fingerprintPay = fingerprintPay;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
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