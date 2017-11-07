package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountCareer;

public interface AccountCareerMapper extends BaseMapper<AccountCareer> {

	@Select("select * from account_career a where a.parent_id=#{parentid}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountCareerMapper.BaseResultMap")
	List<AccountCareer> selectByparentId(@Param("parentid") Long parentid);
	
	
}