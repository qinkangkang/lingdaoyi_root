package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

public class DictionaryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long classId = 0;

    private String name;

    private String code;

    private Integer value;

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}