package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
/**
 * 印刷文字扫描DTO
 * @author jack
 *
 */
public class OcrIdCardDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Outputs outputs[];
	private boolean success;
	private String msg;
	private String error_code;
	
	public String getError_code() {
		return error_code;
	}



	public void setError_code(String error_code) {
		this.error_code = error_code;
	}



	public boolean isSuccess() {
		return success;
	}



	public void setSuccess(boolean success) {
		this.success = success;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Outputs[] getOutputs() {
		return outputs;
	}



	public void setOutputs(Outputs[] outputs) {
		this.outputs = outputs;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public OcrIdCardDTO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public class Outputs {
		private String outputLabel;
		private OutputMulti outputMulti;
		private OutputValue outputValue;
		
		
		
		public String getOutputLabel() {
			return outputLabel;
		}
		public void setOutputLabel(String outputLabel) {
			this.outputLabel = outputLabel;
		}
		public OutputMulti getOutputMulti() {
			return outputMulti;
		}
		public void setOutputMulti(OutputMulti outputMulti) {
			this.outputMulti = outputMulti;
		}
		public OutputValue getOutputValue() {
			return outputValue;
		}
		public void setOutputValue(OutputValue outputValue) {
			this.outputValue = outputValue;
		}
		public Outputs() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		public class OutputMulti implements Serializable {

			private static final long serialVersionUID = 1L;

			public OutputMulti() {
				super();
				// TODO Auto-generated constructor stub
			}
			
			
		}
		public class OutputValue {
			private Integer dataType;
			private String dataValue;
			public OutputValue() {
				super();
				// TODO Auto-generated constructor stub
			}
			public Integer getDataType() {
				return dataType;
			}
			public void setDataType(Integer dataType) {
				this.dataType = dataType;
			}
			public String getDataValue() {
				return dataValue;
			}
			public void setDataValue(String dataValue) {
				this.dataValue = dataValue;
			}
			
		}
	}
}
