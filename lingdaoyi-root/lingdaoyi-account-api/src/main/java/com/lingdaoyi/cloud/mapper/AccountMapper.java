package com.lingdaoyi.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.Account;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

	@Select("select * from account t where t.login_name=#{accountPhone}") // mybatis的
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountMapper.BaseResultMap")
	Account findByLoginName(@Param("accountPhone") String accountPhone);



	
	

}
