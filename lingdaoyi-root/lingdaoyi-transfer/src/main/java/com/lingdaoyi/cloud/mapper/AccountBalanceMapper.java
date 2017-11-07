package com.lingdaoyi.cloud.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.AccountBalance;




@Mapper
public interface AccountBalanceMapper  extends BaseMapper<AccountBalance>{

	
	@Select("select * from account_balance where account_id=#{account_id}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountBalanceMapper.AccountBalanceResultMap")
	AccountBalance selectByAccountId(Long account_id);

}
