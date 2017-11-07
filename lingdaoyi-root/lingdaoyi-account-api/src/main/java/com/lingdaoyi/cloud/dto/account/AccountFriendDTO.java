package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountFriendDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;


	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
    private Integer accountId;//客户ID

   
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
    private Integer toAccountId;//朋友ID


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getAccountId() {
		return accountId;
	}


	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}


	public Integer getToAccountId() {
		return toAccountId;
	}


	public void setToAccountId(Integer toAccountId) {
		this.toAccountId = toAccountId;
	}
	
	
	
}