package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
/**
 * 身份证认证DTO
 * @author jack
 *
 */
public class IdCardDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String error_code;
	private String reason;
	private IdCardResult result;
	public IdCardDTO() {
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
	
	public IdCardResult getResult() {
		return result;
	}
	public void setResult(IdCardResult result) {
		this.result = result;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public class IdCardResult	{
		private String realname;
		private String idcard;
		private boolean isok;
		private IdCardInfor IdCardInfor;
		
		public String getRealname() {
			return realname;
		}

		public void setRealname(String realname) {
			this.realname = realname;
		}

		public String getIdcard() {
			return idcard;
		}

		public void setIdcard(String idcard) {
			this.idcard = idcard;
		}

		public boolean isIsok() {
			return isok;
		}

		public void setIsok(boolean isok) {
			this.isok = isok;
		}

		public IdCardInfor getIdCardInfor() {
			return IdCardInfor;
		}

		public void setIdCardInfor(IdCardInfor idCardInfor) {
			IdCardInfor = idCardInfor;
		}

		public IdCardResult() {
			super();
			// TODO Auto-generated constructor stub
		}

		public class IdCardInfor{
			private String area;
			private String sex;
			private String birthday;
			public IdCardInfor() {
				super();
				// TODO Auto-generated constructor stub
			}
			public String getArea() {
				return area;
			}
			public void setArea(String area) {
				this.area = area;
			}
			public String getSex() {
				return sex;
			}
			public void setSex(String sex) {
				this.sex = sex;
			}
			public String getBirthday() {
				return birthday;
			}
			public void setBirthday(String birthday) {
				this.birthday = birthday;
			}
			
		}
	}
}
