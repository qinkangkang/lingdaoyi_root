package com.lingdaoyi.cloud.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lingdaoyi.cloud.utils.NullToEmptySerializer;

public class PayTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payTypeurl;
	
	private Integer value = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getPayTypeurl() {
		return payTypeurl;
	}

	public void setPayTypeurl(String payTypeurl) {
		this.payTypeurl = payTypeurl;
	}

}
