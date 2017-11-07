package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
/**
 * 印刷文字扫描关键数据DTO
 * @author jack
 *
 */
public class OcrIdCardDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String address;
	private String config_str;
	
	private String name;
	private String nationality;
	private String num;
	private String sex;
	private String birth;
	private boolean success;
	
	private String start_date;
	private String end_date;
	private String issue;
	public OcrIdCardDataDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConfig_str() {
		return config_str;
	}
	public void setConfig_str(String config_str) {
		this.config_str = config_str;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
