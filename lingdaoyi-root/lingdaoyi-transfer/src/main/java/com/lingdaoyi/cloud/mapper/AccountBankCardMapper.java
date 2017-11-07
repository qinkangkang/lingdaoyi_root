package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.AccountBankCard;

@Mapper
public interface AccountBankCardMapper extends BaseMapper<AccountBankCard> {

	@Select("select id,bank_card,bank_code,status,card_type,bank_name,real_name,status,card_type,bank_image from account_bankcard where account_id=#{accountId} and status=1")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountBankCardMapper.AccountBankCardMap")
	List<AccountBankCard> selectBankCardByAccountId(@RequestParam("accountId") Long accountId);
	
	@Select("select id,account_id,bank_card,bank_code,status,card_type,bank_name,real_name,status,card_type,bank_image from account_bankcard where id=#{id}")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountBankCardMapper.AccountBankCardMap")
	AccountBankCard selectAccountBankCardById(@RequestParam("id") Long accountId);

}
