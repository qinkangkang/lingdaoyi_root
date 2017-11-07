package com.lingdaoyi.cloud.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.account.AccountTicket;

@Mapper
public interface AccountTicketMapper extends BaseMapper<AccountTicket> {

	@Select("select * from account_ticket where account_id=#{accountId} and type=1")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountTicketMapper.BaseResultMap")
	AccountTicket findTicketByAccountId(@Param("accountId") long accountId);

	@Select("select * from account_ticket where ticket=#{ticket} and type=1")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountTicketMapper.BaseResultMap")
	AccountTicket findTicketAndType(@Param("ticket") String ticket, @Param("type") Integer type);

	@Delete("delete from account_ticket where account_id=#{accountId} and type=#{clientType} ")
	void clearTicket(@Param("accountId") Long accountId, @Param("clientType") Integer clientType);

	@Select("select * from account_ticket where account_id=#{accountId} and type=1")
	AccountTicket updateAccountTicket(@Param("accountId") long accountId);
}
