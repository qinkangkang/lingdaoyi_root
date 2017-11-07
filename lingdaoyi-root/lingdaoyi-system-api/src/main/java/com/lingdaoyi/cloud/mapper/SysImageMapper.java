package com.lingdaoyi.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lingdaoyi.cloud.baseMapper.BaseMapper;
import com.lingdaoyi.cloud.entity.SysImage;
import com.lingdaoyi.cloud.sql.SimpleSelectInLangDriver;

@Mapper
public interface SysImageMapper extends BaseMapper<SysImage> {

	
	@Select("SELECT * FROM sys_image WHERE id IN (#{ids}) and status = 1")
	@Lang(SimpleSelectInLangDriver.class)
	List<SysImage> findimageList(@Param("ids") List ids);
	
}
