package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.AccountRecord;
import com.lingdaoyi.cloud.entity.AccountRecordByMonth;
import com.lingdaoyi.cloud.sql.TransferSql;

@Mapper
public interface AccountRecordMapper extends BaseMapper<AccountRecord> {

	@SelectProvider(type = TransferSql.class, method = "findLastRecord")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountRecordMapper.AccountRecordResultMap")
	List<AccountRecord> findLastRecord(@Param("account_id")Long account_id, @Param("count")Integer count);

	@SelectProvider(type = TransferSql.class, method = "findTransferRecordList")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountRecordMapper.AccountRecordByMonthResultMap")
	List<AccountRecordByMonth> selectTransferRecordList(@Param("account_id") Long account_id,
			@Param("receiveAccountId") Long receiveAccountId, @Param("type") Integer type);

	@Select("select r.* from account_record  r where  r.account_id=#{account_id}  and r.type=1 and r.is_success=0  ORDER BY r.gmt_create desc limit 3")
	@ResultMap("com.lingdaoyi.cloud.mapper.AccountRecordMapper.AccountRecordResultMap")
	List<AccountRecord> findLast3Record(Long account_id);
	
	
	@Select("select DATE_FORMAT(gmt_create,'%Y-%m') as month from account_record  group by month")
	List<String> selectAllMonth();


}
