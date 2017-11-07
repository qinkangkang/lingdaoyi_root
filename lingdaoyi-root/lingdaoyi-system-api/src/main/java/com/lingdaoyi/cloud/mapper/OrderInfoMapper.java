package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.OrderInfo;
import com.lingdaoyi.cloud.sql.OrderSql;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

	@SelectProvider(type=OrderSql.class,method="findOrderList")
	@ResultMap("com.lingdaoyi.cloud.mapper.OrderInfoMapper.OrderInfotMap") 
	List<OrderInfo> findOrderList( @Param("status")int status, @Param("accountId")String accountId);

}
