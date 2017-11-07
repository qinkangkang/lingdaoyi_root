package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class SponsorWithdrawDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long sponsorId;
	
	private long accountId;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String reign;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String workTime;

	public long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getReign() {
		return reign;
	}

	public void setReign(String reign) {
		this.reign = reign;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

}
