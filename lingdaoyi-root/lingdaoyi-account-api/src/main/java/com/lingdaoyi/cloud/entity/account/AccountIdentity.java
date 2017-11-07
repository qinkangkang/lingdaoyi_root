package com.lingdaoyi.cloud.entity.account;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_identity")
public class AccountIdentity {
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "account_id")
	private Long accountId;

	/**
	 * 证件类型（0、身份证）
	 */
	@Column(name = "certificate_type")
	private Integer certificateType;

	/**
	 * 真实姓名
	 */
	@Column(name = "real_name")
	private String realName;

	/**
	 * 证件号
	 */
	@Column(name = "certificate_no")
	private String certificateNo;

	/**
	 * 性别
	 */
	@Column(name = "sex")
	private String sex;

	/**
	 * 出生日期
	 */
	@Column(name = "birth")
	private Date birth;

	/**
	 * 民族
	 */
	@Column(name = "nationality")
	private String nationality;
	/**
	 * 证件照片审核状态
	 */
	@Column(name = "audit_status")
	private Integer auditStatus;

	/**
	 * 有效期起始时间
	 */
	@Column(name = "start_date")
	private Date startDate;

	/**
	 * 有效期结束时间
	 */
	@Column(name = "end_date")
	private Date endDate;

	/**
	 * 签发机关
	 */
	@Column(name = "issue")
	private String issue;

	/**
	 * 证件地址
	 */
	@Column(name = "address")
	private String address;

	/**
	 * 详细地址
	 */
	@Column(name = "detailed_address")
	private String detailedAddress;

	/**
	 * 创建时间
	 */
	@Column(name = "gmt_create")
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	@Column(name = "gmt_modified")
	private Date gmtModified;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(name = "is_deleted")
	private Integer isDeleted;

	/**
	 * 密匙
	 */
	private String enkey;

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

	public Integer getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(Integer certificateType) {
		this.certificateType = certificateType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
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

	public String getEnkey() {
		return enkey;
	}

	public void setEnkey(String enkey) {
		this.enkey = enkey;
	}

}