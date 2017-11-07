package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountLevelExtDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer currentLevel;
	
	private List<AccountLevelDTO> list = Lists.newArrayList();

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	public List<AccountLevelDTO> getList() {
		return list;
	}

	public void setList(List<AccountLevelDTO> list) {
		this.list = list;
	}

	
}
