package com.lingdaoyi.cloud.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountFriend;

public interface AccountFriendMapper extends BaseMapper<AccountFriend> {

	@Select("select * from account_friend where account_id=#{accountId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountFriendMapper.BaseResultMap")
	List<AccountFriend> getFriendList(@Param("accountId") Long id);

	@Select("select * from account_friend where account_id=#{accountId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountFriendMapper.BaseResultMap")
	AccountFriend findByAccountId(Long valueOf);

	@Select("select * from account_friend where account_id=#{accountId} AND to_account_id=#{friendId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountFriendMapper.BaseResultMap")
	AccountFriend findByAccountIdAndFriendId(@Param("accountId") Long accountId, @Param("friendId") long friendId);

}