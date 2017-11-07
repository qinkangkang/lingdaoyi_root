package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountCareerExtDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String industry;// 职业行业

	private List<AccountCareerDTO> list = Lists.newArrayList();

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public List<AccountCareerDTO> getList() {
		return list;
	}

	public void setList(List<AccountCareerDTO> list) {
		this.list = list;
	}

}
