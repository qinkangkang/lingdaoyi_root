package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.CurrencyExchangeRecord;

@Mapper
public interface CurrencyExchangeRecordMapper extends BaseMapper<CurrencyExchangeRecord> {

	@Select("select * from currency_exchange_record c where c.account_id=#{accountId}") // mybatis的
	@ResultMap("com.lingdaoyi.cloud.mapper.CurrencyExchangeRecordMapper.CurrencyExchangeRecordMap")
	List<CurrencyExchangeRecord> getExchangeRecordListByAccountId(@Param("accountId") Long accountId);
}