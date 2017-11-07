package com.lingdaoyi.cloud.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.Person;
import com.lingdaoyi.cloud.entity.sys.SysSms;

@Mapper
public interface SysSmsMapper extends BaseMapper<SysSms> {

	@Select("select send_time from sys_sms t where t.send_phone=#{accountPhone} and sms_type=#{smsType}")
	List<Date> findByPhone(@Param("accountPhone") String accountPhone, @Param("smsType") Integer smsType);
}
