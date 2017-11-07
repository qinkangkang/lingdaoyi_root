package com.lingdaoyi.cloud.dto;

import java.io.Serializable;
import java.util.List;

public class AccountRecordByMonthDTO implements Serializable {
	
	private  String month;
	
	private  List<AccountRecordDTO> recordList;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<AccountRecordDTO> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AccountRecordDTO> recordList) {
		this.recordList = recordList;
	}
	
	
	

}
