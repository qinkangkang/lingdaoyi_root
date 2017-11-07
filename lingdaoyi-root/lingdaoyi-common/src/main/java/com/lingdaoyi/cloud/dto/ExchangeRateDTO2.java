package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

/**
 * 汇率转换支持币种实时汇率DTO
 * 
 * @author jack
 */
public class ExchangeRateDTO2 implements Serializable {

	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;
	private Object result;

	public ExchangeRateDTO2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	
}
