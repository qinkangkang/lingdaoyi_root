package com.lingdaoyi.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountInfo;

@Mapper
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {
	
	@Select("select * from account_info a where a.account_id=#{accountId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountInfoMapper.BaseResultMap")
	AccountInfo findByAccountId(@Param("accountId") long accountId);
}
