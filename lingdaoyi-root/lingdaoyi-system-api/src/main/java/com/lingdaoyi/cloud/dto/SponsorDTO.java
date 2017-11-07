package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class SponsorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long sponsorId;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorUrl;

	public String getSponsorUrl() {
		return sponsorUrl;
	}

	public void setSponsorUrl(String sponsorUrl) {
		this.sponsorUrl = sponsorUrl;
	}

	public long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(long sponsorId) {
		this.sponsorId = sponsorId;
	}

}
