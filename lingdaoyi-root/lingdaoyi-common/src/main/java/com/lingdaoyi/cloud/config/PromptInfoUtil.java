package com.lingdaoyi.cloud.config;

import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.utils.exception.ServiceException;

/**
 * 提示文案工具类，将项目中所有的提示文案统一放置属性文件中
 * 
 * @author jinsey
 *
 */
public class PromptInfoUtil {

	private static final Logger logger = LoggerFactory.getLogger(PromptInfoUtil.class);

	private static Map<String, Configuration> promptInfoMap = Maps.newHashMap();

	public static final String EMC_Login = "login";

	public static final String EMC_Account = "account";

	public static final String EMC_Error_Code = "erroeCode";

	public static final String EMC_Error_Msg = "erroeMsg";

	public static final String EMC_Auth_Msg = "authMsg";

	public static final String EMC_System_Msg = "systemMsg";

	public static final String EMC_Goods_Msg = "goodsMsg";

	public static final String EMC_Order_Msg = "orderMsg";

	public static final String EMC_Sponsor_Msg = "sponsorMsg";

	public static final String EMC_Transfer_Msg = "transfer";

	public static void init() {

		Configurations configs = new Configurations();
		try {

			Configuration config = configs.properties("classpath:/promptInfo/account/emc_login.properties");
			promptInfoMap.put(EMC_Login, config);

			config = configs.properties("classpath:/promptInfo/account/emc_account.properties");
			promptInfoMap.put(EMC_Account, config);

			config = configs.properties("classpath:/promptInfo/error/emc_erroe_code.properties");
			promptInfoMap.put(EMC_Error_Code, config);

			config = configs.properties("classpath:/promptInfo/error/emc_erroe_msg.properties");
			promptInfoMap.put(EMC_Error_Msg, config);

			config = configs.properties("classpath:/promptInfo/auth/emc_auth_msg.properties");
			promptInfoMap.put(EMC_Auth_Msg, config);

			config = configs.properties("classpath:/promptInfo/system/emc_system_msg.properties");
			promptInfoMap.put(EMC_System_Msg, config);

			config = configs.properties("classpath:/promptInfo/system/emc_goods_msg.properties");
			promptInfoMap.put(EMC_Goods_Msg, config);

			config = configs.properties("classpath:/promptInfo/system/emc_order_msg.properties");
			promptInfoMap.put(EMC_Order_Msg, config);

			config = configs.properties("classpath:/promptInfo/system/emc_sponsor_msg.properties");
			promptInfoMap.put(EMC_Sponsor_Msg, config);

			config = configs.properties("classpath:/promptInfo/transfer/emc_transfer_msg.properties");
			promptInfoMap.put(EMC_Transfer_Msg, config);

		} catch (ConfigurationException cex) {
			throw new ServiceException("初始化提示信息工具类出错：" + cex.getMessage(), cex);
		}
	}

	public static String getPrompt(String prop, String key) {
		Configuration config = promptInfoMap.get(prop);
		return config.getString(key, StringUtils.EMPTY);
	}

}