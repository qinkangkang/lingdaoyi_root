package com.lingdaoyi.cloud.entity.account;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_ticket")
public class AccountTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;

	private Integer type;

	private String ticket;

	@Column(name = "liveTime")
	private Date livetime;

	@Column(name = "gmt_create")
	private Date gmtCreate;

	@Column(name = "gmt_modified")
	private Date gmtModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getLivetime() {
		return livetime;
	}

	public void setLivetime(Date livetime) {
		this.livetime = livetime;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
}