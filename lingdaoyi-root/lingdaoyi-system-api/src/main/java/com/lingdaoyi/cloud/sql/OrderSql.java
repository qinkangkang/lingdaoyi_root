package com.lingdaoyi.cloud.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
/**
 * 不要使用重载，即不要使用方法名相同参数不同的方法，以避免发生诡异问题
 * @author maxiao
 *
 */

public class OrderSql {
	
	//方法使用了@Param注解的话，那么相应findOrderList方法必须接受Map<String, Object>做为参数
	public String findOrderList(Map<String, Object> para) { 
		StringBuilder sql = new StringBuilder();
		sql.append("select * from order_info where 1=1 ");
		if(para.get("accountId")!=null) { 
			sql.append(" and account_id =").append(para.get("accountId"));
		}
		if(para.get("status")!=null && ((Integer) para.get("status")).intValue()!=0) {
			sql.append(" and status =").append(para.get("status"));
		}else {
			sql.append(" and status <999");
		}
		sql.append(" order by createTime");
		return sql.toString();
	}
	
	

}
