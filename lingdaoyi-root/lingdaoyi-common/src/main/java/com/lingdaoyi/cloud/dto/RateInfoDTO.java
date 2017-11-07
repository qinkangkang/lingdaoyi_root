package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

/**
 * 汇率转换支持币种实时汇率DTO
 * 
 * @author jack
 */
public class RateInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;
	private RateResult result;

	public RateInfoDTO() {
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
	
	public RateResult getResult() {
		return result;
	}

	public void setResult(RateResult result) {
		this.result = result;
	}

	public class RateResult{
		private String from;
		private String to;
		private String fromname;
		private String toname;
		private String updatetime;
		private String rate;
		private String camount;
		public String getFrom() {
			return from;
		}
		public String getTo() {
			return to;
		}
		public String getFromname() {
			return fromname;
		}
		public String getToname() {
			return toname;
		}
		public String getUpdatetime() {
			return updatetime;
		}
		public String getRate() {
			return rate;
		}
		public String getCamount() {
			return camount;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public void setFromname(String fromname) {
			this.fromname = fromname;
		}
		public void setToname(String toname) {
			this.toname = toname;
		}
		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}
		public void setRate(String rate) {
			this.rate = rate;
		}
		public void setCamount(String camount) {
			this.camount = camount;
		}
		
	}
}
