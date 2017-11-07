package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountChatListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;

	private Long toAccountId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String assistantDesc;

	private Integer type;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date transferDate;

	private BigDecimal transferAmount = BigDecimal.ZERO;

	private Integer isTransferAssistant = 0;

	private String gmtCreate;

	private Integer isDeleted = 0;

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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
