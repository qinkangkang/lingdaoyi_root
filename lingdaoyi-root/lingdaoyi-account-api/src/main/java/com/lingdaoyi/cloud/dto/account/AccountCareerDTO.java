package com.lingdaoyi.cloud.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class AccountCareerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long careerId;//

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String occupationName;

	public Long getCareerId() {
		return careerId;
	}

	public void setCareerId(Long careerId) {
		this.careerId = careerId;
	}

	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

}
