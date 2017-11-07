package com.lingdaoyi.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountIdentity;
@Mapper
public interface AccountIdentityMapper extends BaseMapper<AccountIdentity> {
	
	
	@Select("SELECT * FROM account_identity i WHERE i.account_id = #{accountId} AND i.is_deleted = 0")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountIdentityMapper.BaseResultMap")
	AccountIdentity selectByAccontId(@Param("accountId") Long accountId);
	
	
	@Select("SELECT * FROM account_identity i WHERE i.real_name = #{realName} AND i.certificate_no = #{num} i.is_deleted = 0")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountIdentityMapper.BaseResultMap")
	AccountIdentity findByNameAndNum(@Param("realName") String realName, @Param("num") String num);

}