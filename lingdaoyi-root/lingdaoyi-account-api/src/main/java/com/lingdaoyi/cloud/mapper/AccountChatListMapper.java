package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountChatList;

@Mapper
public interface AccountChatListMapper extends BaseMapper<AccountChatList> {

	@Select("SELECT * FROM account_chat_list WHERE EXISTS (SELECT 1 FROM (SELECT to_account_id, max(transfer_date) AS transfer_date FROM account_chat_list WHERE account_id = #{accountId} GROUP BY to_account_id ) x WHERE x.to_account_id = account_chat_list.to_account_id AND x.transfer_date = account_chat_list.transfer_date)")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountChatListMapper.BaseResultMap")
	List<AccountChatList> getLatelyFriendsList(@Param("accountId") Long accountId);

	/*@Select("SELECT DISTINCT(to_account_id), FROM account_chat_list WHERE account_id=#{accountId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountChatListMapper.BaseResultMap")
	List<Long> getToAccountIDList(@Param("accountId") Long accountId);

	@Select("select * from account_chat_list where account_id=#{accountId} AND to_account_id={toAccountId}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountChatListMapper.BaseResultMap")
	List<AccountChatList> getLatelyFriendsList(@Param("accountId") Long accountId,
			@Param("toAccountId") Long toAccountId);*/

}
