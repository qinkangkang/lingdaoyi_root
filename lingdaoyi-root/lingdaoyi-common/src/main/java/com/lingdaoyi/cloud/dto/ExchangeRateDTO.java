package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.util.Map;
/**
 * 汇率转换支持币种实时汇率DTO
 * @author jack
 */
public class ExchangeRateDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String showapi_res_code;
	private String showapi_res_error;
	private Map<String,Object>	showapi_res_body;
	public ExchangeRateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getShowapi_res_code() {
		return showapi_res_code;
	}
	public void setShowapi_res_code(String showapi_res_code) {
		this.showapi_res_code = showapi_res_code;
	}
	public String getShowapi_res_error() {
		return showapi_res_error;
	}
	public void setShowapi_res_error(String showapi_res_error) {
		this.showapi_res_error = showapi_res_error;
	}
	public Map<String,Object> getShowapi_res_body() {
		return showapi_res_body;
	}
	public void setShowapi_res_body(Map<String,Object> showapi_res_body) {
		this.showapi_res_body = showapi_res_body;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}	
