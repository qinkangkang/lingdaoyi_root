package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.util.Map;
/**
 * 银行卡认证DTO
 * @author jack
 *
 */
public class BankCardDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String reason; //认证是否通过（认证通过/认证不通过）
	private BankCardResult result;
	private String ordersign;
	public BankCardDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public BankCardResult getResult() {
		return result;
	}
	public void setResult(BankCardResult result) {
		this.result = result;
	}
	public String getOrdersign() {
		return ordersign;
	}
	public void setOrdersign(String ordersign) {
		this.ordersign = ordersign;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public class BankCardResult{
		private String bankcard;
		private Information information;
		
		public String getBankcard() {
			return bankcard;
		}

		public void setBankcard(String bankcard) {
			this.bankcard = bankcard;
		}

		public Information getInformation() {
			return information;
		}

		public void setInformation(Information information) {
			this.information = information;
		}

		public BankCardResult() {
			super();
			// TODO Auto-generated constructor stub
		}

		public class Information{
			private String abbreviation;
			private String bankimage;
			private String bankname;
			private String bankurl;
			private String cardname;
			private String cardtype;
			private String enbankname;
			private boolean isLuhn;
			private Integer iscreditcard;
			private String servicephone;
			public Information() {
				super();
				// TODO Auto-generated constructor stub
			}
			public String getAbbreviation() {
				return abbreviation;
			}
			public void setAbbreviation(String abbreviation) {
				this.abbreviation = abbreviation;
			}
			public String getBankimage() {
				return bankimage;
			}
			public void setBankimage(String bankimage) {
				this.bankimage = bankimage;
			}
			public String getBankname() {
				return bankname;
			}
			public void setBankname(String bankname) {
				this.bankname = bankname;
			}
			public String getBankurl() {
				return bankurl;
			}
			public void setBankurl(String bankurl) {
				this.bankurl = bankurl;
			}
			public String getCardname() {
				return cardname;
			}
			public void setCardname(String cardname) {
				this.cardname = cardname;
			}
			public String getCardtype() {
				return cardtype;
			}
			public void setCardtype(String cardtype) {
				this.cardtype = cardtype;
			}
			public String getEnbankname() {
				return enbankname;
			}
			public void setEnbankname(String enbankname) {
				this.enbankname = enbankname;
			}
			public boolean isLuhn() {
				return isLuhn;
			}
			public void setLuhn(boolean isLuhn) {
				this.isLuhn = isLuhn;
			}
			public Integer getIscreditcard() {
				return iscreditcard;
			}
			public void setIscreditcard(Integer iscreditcard) {
				this.iscreditcard = iscreditcard;
			}
			public String getServicephone() {
				return servicephone;
			}
			public void setServicephone(String servicephone) {
				this.servicephone = servicephone;
			}
			
			
		}
		
	}
}
