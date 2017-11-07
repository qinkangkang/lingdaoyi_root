package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountLevelDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer levelCode;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transferQuota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(Integer levelCode) {
		this.levelCode = levelCode;
	}

	public String getTransferQuota() {
		return transferQuota;
	}

	public void setTransferQuota(String transferQuota) {
		this.transferQuota = transferQuota;
	}

}
