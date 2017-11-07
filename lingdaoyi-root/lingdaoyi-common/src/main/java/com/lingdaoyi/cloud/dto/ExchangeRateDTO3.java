package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.util.Map;


/**
 * 汇率转换支持币种实时汇率DTO
 * 
 * @author jack
 */
public class ExchangeRateDTO3 implements Serializable {

	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;
	private CurrencyResult result;

	public ExchangeRateDTO3() {
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

	public CurrencyResult getResult() {
		return result;
	}

	public void setResult(CurrencyResult result) {
		this.result = result;
	}

	public class CurrencyResult {
		private String currency;
		private String name;
		private Map<String, CurrencyVlue> list;

		public String getCurrency() {
			return currency;
		}

		public String getName() {
			return name;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, CurrencyVlue> getList() {
			return list;
		}

		public void setList(Map<String, CurrencyVlue> list) {
			this.list = list;
		}

		public class CurrencyVlue {
			private String name;
			private String rate;
			private String updatetime;
			private String currencySort;
			private String countryFlag;
			private String lowerLimit;
			private String currencySign;
			public String getName() {
				return name;
			}

			public String getRate() {
				return rate;
			}

			public String getUpdatetime() {
				return updatetime;
			}

			public void setName(String name) {
				this.name = name;
			}

			public void setRate(String rate) {
				this.rate = rate;
			}

			public void setUpdatetime(String updatetime) {
				this.updatetime = updatetime;
			}

			public String getCountryFlag() {
				return countryFlag;
			}

			public void setCountryFlag(String countryFlag) {
				this.countryFlag = countryFlag;
			}

			public String getCurrencySort() {
				return currencySort;
			}

			public void setCurrencySort(String currencySort) {
				this.currencySort = currencySort;
			}

			public String getLowerLimit() {
				return lowerLimit;
			}

			public void setLowerLimit(String lowerLimit) {
				this.lowerLimit = lowerLimit;
			}

			public String getCurrencySign() {
				return currencySign;
			}

			public void setCurrencySign(String currencySign) {
				this.currencySign = currencySign;
			}
		}
	}
}
