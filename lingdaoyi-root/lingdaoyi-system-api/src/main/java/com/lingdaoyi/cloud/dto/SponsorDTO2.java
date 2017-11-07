package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class SponsorDTO2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long sponsorId;
	
	private Integer sponsorType;

	public long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public Integer getSponsorType() {
		return sponsorType;
	}

	public void setSponsorType(Integer sponsorType) {
		this.sponsorType = sponsorType;
	}
	
}
