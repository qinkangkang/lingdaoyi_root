package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class SponsorDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private long sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorBrief;

	private String[] sponsorImage = new String[] {};

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gps;

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

	public String getSponsorBrief() {
		return sponsorBrief;
	}

	public void setSponsorBrief(String sponsorBrief) {
		this.sponsorBrief = sponsorBrief;
	}

	public String[] getSponsorImage() {
		return sponsorImage;
	}

	public void setSponsorImage(String[] sponsorImage) {
		this.sponsorImage = sponsorImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
	
}
