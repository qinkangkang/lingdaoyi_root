package com.lingdaoyi.cloud.utils.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.encrypt.DesUtil3;
import com.lingdaoyi.cloud.utils.DictionaryUtil;
import com.lingdaoyi.cloud.utils.authentication.BankCardUtil;
import com.lingdaoyi.cloud.utils.authentication.ExchangeRateUtil;
import com.lingdaoyi.cloud.utils.authentication.ExchangeRateUtil2;
import com.lingdaoyi.cloud.utils.authentication.IDcardUtil;
import com.lingdaoyi.cloud.utils.authentication.OcrIDcardUtil;
import com.lingdaoyi.cloud.utils.sms.SmsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目初始化参数监听器
 * 
 * @author jinsey
 *
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory.getLogger(StartupListener.class);

	// @Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {

		// 将缓存的常量放入缓存中
		cacheSetCodeList(evt);
	}

	public void cacheSetCodeList(ContextRefreshedEvent evt) {
		Constant.init();
		logger.warn("系统上传文件路径置完成！");
		SmsUtil.init();
		logger.warn("短信网关初始化完成！");
		DesUtil3.init();
		logger.warn("加密工具初始化完成！");
		PromptInfoUtil.init();
		logger.warn("提示信息工具初始化完成！");
		BankCardUtil.init();
		logger.warn("银行卡认证信息工具初始化完成！");
		IDcardUtil.init();
		logger.warn("身份证认证信息工具初始化完成！");
		OcrIDcardUtil.init();
		logger.warn("身份证图片扫描工具初始化完成！");
		ExchangeRateUtil.init();
		ExchangeRateUtil2.init();
		logger.warn("汇率工具初始化完成！");
	}
}
