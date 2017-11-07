package com.lingdaoyi.cloud.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountBankCardDTO {
	
	
	private Long bankCardId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankCardNO;// 银行卡卡号

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankName;// 银行名称

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankCardUserName;// 银行卡使用人名称
	private Integer status;// 是否可用,（1启用，0禁用）
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankCardImage;// 银行卡图标

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cardType;// 银行卡类别;1: 借记卡，2: 储蓄卡
	
	
	public Long getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(Long bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getBankCardNO() {
		return bankCardNO;
	}

	public void setBankCardNO(String bankCardNO) {
		this.bankCardNO = bankCardNO;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardUserName() {
		return bankCardUserName;
	}

	public void setBankCardUserName(String bankCardUserName) {
		this.bankCardUserName = bankCardUserName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getBankCardImage() {
		return bankCardImage;
	}

	public void setBankCardImage(String bankCardImage) {
		this.bankCardImage = bankCardImage;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
