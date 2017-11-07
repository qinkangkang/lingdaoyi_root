package com.lingdaoyi.cloud.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
/**
 * 不要使用重载，即不要使用方法名相同参数不同的方法，以避免发生诡异问题
 * @author maxiao
 *
 */

public class GoodsSql {
	
	//方法使用了@Param注解的话，那么相应findPersonByIdSql方法必须接受Map<String, Object>做为参数
	public String findGoodsSpuList(Map<String, Object> para) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from goods_spu where 1=1 ");
		if(para.get("sponsorId")!=null && Long.parseLong(para.get("sponsorId").toString())!=0L) {
			sql.append(" and sponsor_id =").append(para.get("sponsorId"));
		}
		if(para.get("countryId")!=null) {
			sql.append(" and country_id =").append(para.get("countryId"));
		}
		if(para.get("searchKey")!=null) {
			sql.append(" and name like ").append("'%").append(para.get("searchKey")).append("%'");
		}
		sql.append(" order by updateTime");
		return sql.toString();
	}
	
	

}
