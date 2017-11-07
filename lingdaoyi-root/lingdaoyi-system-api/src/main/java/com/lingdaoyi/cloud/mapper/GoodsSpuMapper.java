package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.GoodsSpu;
import com.lingdaoyi.cloud.sql.GoodsSql;

@Mapper
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {

	@SelectProvider(type=GoodsSql.class,method="findGoodsSpuList")
	@ResultMap("com.lingdaoyi.cloud.mapper.GoodsSpuMapper.GoodsSpuResultMap") 
	List<GoodsSpu> findGoodsSpuList( @Param("sponsorId")long sponsorId, 
			@Param("countryId")long countryId ,
			@Param("searchKey")String searchKey);

}
