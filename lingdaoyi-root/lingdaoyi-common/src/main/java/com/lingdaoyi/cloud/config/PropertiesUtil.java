package com.lingdaoyi.cloud.config;

import org.springside.modules.utils.PropertiesLoader;

import com.lingdaoyi.cloud.utils.sys.sysConfig;

/**
 * @name PropertiesUtil
 * @title 操作属性文件工具类
 */
public class PropertiesUtil {

	private static PropertiesLoader propertiesLoaderSys;

	private static PropertiesLoader propertiesLoaderSms;

	private static PropertiesLoader propertiesLoaderRedis;

	private static PropertiesLoader propertiesLoaderDruidConfig;

	private static PropertiesLoader propertiesLoaderAuthConfig;

	public static final Integer systemConfig = 1;

	public static final Integer smsConfig = 2;

	public static final Integer redisConfig = 3;

	public static final Integer druidConfig = 4;

	public static final Integer authConfig = 5;

	static {
		propertiesLoaderSys = new PropertiesLoader(sysConfig.SYSTEM_SETTING_FILE);
		propertiesLoaderSms = new PropertiesLoader(sysConfig.SMS_SETTING_FILE);
		propertiesLoaderRedis = new PropertiesLoader(sysConfig.REDIS_SETTING_FILE);
		propertiesLoaderDruidConfig = new PropertiesLoader(sysConfig.DRUIDCONfIG_SETTING_FILE);
		propertiesLoaderAuthConfig = new PropertiesLoader(sysConfig.AUTHCONfIG_SETTING_FILE);
	}

	// 获取key所对应的值
	public static String getProperty(String key, Integer type) {
		if (type.equals(systemConfig)) {
			return propertiesLoaderSys.getProperty(key);
		} else if (type.equals(smsConfig)) {
			return propertiesLoaderSms.getProperty(key);
		} else if (type.equals(redisConfig)) {
			return propertiesLoaderRedis.getProperty(key);
		} else if (type.equals(druidConfig)) {
			return propertiesLoaderDruidConfig.getProperty(key);
		} else if (type.equals(authConfig)) {
			return propertiesLoaderAuthConfig.getProperty(key);
		}
		return null;

	}
}