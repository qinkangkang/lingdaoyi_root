package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_bankcard")
public class AccountBankCard {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;// 账户id
	
	@Column(name = "account_approvedBankId")
	private Long approvedBankId;//支持的银行卡id

	@Column(name = "bank_card")
	private String bankCard;// 银行卡号

	@Column(name = "id_card")
	private String idCard;// 身份证号

	@Column(name = "real_name")
	private String realName;// 真实姓名

	private String mobile;// 银行预留手机号

	@Column(name = "bank_code")
	private String bankCode;// 银行编码

	@Column(name = "bank_name")
	private String bankName;// 银行名称
	
	@Column(name = "bank_image")
	private String bankImage;// 银行名称
	

	/**
	 * Api key
	 */
	@Column(name = "api_key")
	private String apiKey;

	private Integer status;// 状态（1启用，0禁用）

	@Column(name = "gmt_create")
	private Date gmtCreate;// 创建时间

	@Column(name = "gmt_modified")
	private Date gmtModified;// 修改时间

	private String card_type;//银行卡类型;1: 借记卡，2: 储蓄卡;3:其他

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

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	
	
	

	public String getBankImage() {
		return bankImage;
	}

	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public Long getApprovedBankId() {
		return approvedBankId;
	}

	public void setApprovedBankId(Long approvedBankId) {
		this.approvedBankId = approvedBankId;
	}

	
	
	
}