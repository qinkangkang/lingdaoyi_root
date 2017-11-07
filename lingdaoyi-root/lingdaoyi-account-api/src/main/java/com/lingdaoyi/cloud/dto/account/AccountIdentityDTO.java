package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountIdentityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isAuth = false;

	private boolean idPhoto = false;// 证件照

	private boolean laborContract = false;// 劳动合同

	private boolean emailStatus = false;// 电子邮件

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String realName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String certificateNo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String certificateType;// 证件类型

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date validityPeriod;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String area;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailedAddress;

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public boolean isIdPhoto() {
		return idPhoto;
	}

	public void setIdPhoto(boolean idPhoto) {
		this.idPhoto = idPhoto;
	}

	public boolean isEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(boolean emailStatus) {
		this.emailStatus = emailStatus;
	}

	public boolean isLaborContract() {
		return laborContract;
	}

	public void setLaborContract(boolean laborContract) {
		this.laborContract = laborContract;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Date getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Date validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

}
