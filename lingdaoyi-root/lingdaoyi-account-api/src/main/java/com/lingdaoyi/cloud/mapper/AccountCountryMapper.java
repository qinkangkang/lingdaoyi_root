package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountCountry;

@Mapper
public interface AccountCountryMapper extends BaseMapper<AccountCountry> {

	@Select("select * from account_country where id=#{id}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountCountryMapper.BaseResultMap")
	AccountCountry findByAccountCountryId(@Param("id") long id);
	
	@Select("select * from account_country where 1=1")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountCountryMapper.BaseResultMap")
	List<AccountCountry> getAccountCountryList();
	
	@Select("select * from account_country where currencySort=#{currencySort}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountCountryMapper.BaseResultMap")
	AccountCountry selectCountryByCurrencySort(@Param("currencySort")String currencySort);

}
