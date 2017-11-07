package com.lingdaoyi.cloud.sql;

import java.util.Map;

/**
 * 不要使用重载，即不要使用方法名相同参数不同的方法，以避免发生诡异问题
 * 
 * @author maxiao
 *
 */

public class TransferSql {

	// 方法使用了@Param注解的话，那么相应findPersonByIdSql方法必须接受Map<String, Object>做为参数
	public String findTransferRecordList(Map<String, Object> para) {
		StringBuilder sql = new StringBuilder();
		sql.append("select DATE_FORMAT(a.gmt_create,'%Y-%m') as month,a.* from account_record a where 1=1 ");
		if (para.get("type") != null) {
			sql.append(" and a.type =").append(para.get("type"));
		}
		if (para.get("account_id") != null) {
			sql.append(" and a.account_id =").append(para.get("account_id"));
		}

		if (para.get("receiveAccountId") != null) {
			sql.append(" and a.receive_account_id =").append(para.get("receiveAccountId"));
		}

		sql.append(" and a.is_success!=0 order by a.gmt_create desc");

		return sql.toString();
	}

	public String findLastRecord(Map<String, Object> para) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select r.id,r.account_id,r.type,r.pay_no,r.transfer_money,r.transfer_instructions,r.receive_account_id,r.gmt_create from account_record  r  where 1=1 ");
		if (para.get("account_id") != null) {
			sql.append(" and account_id =").append(para.get("account_id"));
		}
		sql.append(" and is_success!=0 and type=1 order by gmt_create desc");
		
		
		if (para.get("count") != null && (Integer)(para.get("count"))>0) {
			sql.append(" limit " + para.get("count"));
		}else{
			sql.append(" limit 1" );
		}
		return sql.toString();
	}

}
