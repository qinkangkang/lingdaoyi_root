package com.lingdaoyi.cloud.entity.account;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_chat_list")
public class AccountChatList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "to_account_id")
	private Long toAccountId;

	@Column(name = "transfer_amount")
	private BigDecimal transferAmount;

	@Column(name = "is_transfer_assistant")
	private Integer isTransferAssistant;

	@Column(name = "assistant_desc")
	private String assistantDesc;

	@Column(name = "type")
	private Integer type;

	@Column(name = "transfer_date")
	private Date transferDate;

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

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(Long toAccountId) {
		this.toAccountId = toAccountId;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Integer getIsTransferAssistant() {
		return isTransferAssistant;
	}

	public void setIsTransferAssistant(Integer isTransferAssistant) {
		this.isTransferAssistant = isTransferAssistant;
	}

	public String getAssistantDesc() {
		return assistantDesc;
	}

	public void setAssistantDesc(String assistantDesc) {
		this.assistantDesc = assistantDesc;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
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