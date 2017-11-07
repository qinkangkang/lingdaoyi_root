package com.lingdaoyi.cloud.utils.init;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.utils.sms.SmsUtil;

public class Constant {

	// 敏感词文件
	public static final String BAD_WORD_FILE = "classpath:wordfilter.txt";

	// 项目文件根路径
	public static String RootPath;

	// 系统默认的语言
	public static final String defaultLanguage = "zh_CN";

	// 英文语言
	public static final String englishLanguage = "en_US";

	// 短信验证码缓存Key
	public static final String SmsCheckCodeCacheKey = "SmsCheckCode";

	// 登录验证码缓存Key
	public static final String SmsLoginPwdKey = "SmsLoginPwd";

	// WEB端地址
	public static String H5Url = null;

	// 用户默认头像
	public static String defaultHeadImgUrl = null;

	// EMC LOGO
	public static String czyhInterfaceLogoImgUrl = null;

	public static void init() {
		if (SystemUtils.IS_OS_WINDOWS) {
			RootPath = PropertiesUtil.getProperty("system.windowsRootPath",PropertiesUtil.systemConfig);
		} else {
			RootPath = PropertiesUtil.getProperty("system.linuxRootPath",PropertiesUtil.systemConfig);
		}

		defaultHeadImgUrl = new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig)).append(PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig))
				.append("/foms/customerHeadimg.jpg").toString();

		czyhInterfaceLogoImgUrl = new StringBuilder(PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig)).append(PropertiesUtil.getProperty("system.imageRootPath",PropertiesUtil.systemConfig))
				.append("/foms/czyhInterfaceLogo.jpg").toString();

		// 设置WEB端地址
		H5Url = PropertiesUtil.getProperty("system.fileServerUrl",PropertiesUtil.systemConfig);

		// 设置短信发送开关
		if (PropertiesUtil.getProperty("sms.checkCodeLoginSwitch",PropertiesUtil.smsConfig).toString().equals("1")) {
			SmsUtil.setCheckCodeLoginSwitch(true);
		} else {
			SmsUtil.setCheckCodeLoginSwitch(false);
		}
		
		if (PropertiesUtil.getProperty("sms.checkCodeRegisterSwitch",PropertiesUtil.smsConfig).toString().equals("1")) {
			SmsUtil.setCheckCodeRegisterSwitch(true);
		} else {
			SmsUtil.setCheckCodeRegisterSwitch(false);
		}
		
		if (PropertiesUtil.getProperty("sms.checkCodeModifyLoginPwdSwitch",PropertiesUtil.smsConfig).toString().equals("1")) {
			SmsUtil.setCheckCodeModifyLoginPwdSwitch(true);
		} else {
			SmsUtil.setCheckCodeModifyLoginPwdSwitch(false);
		}
		
		if (PropertiesUtil.getProperty("sms.checkCodeModifyPayPwdSwitch",PropertiesUtil.smsConfig).toString().equals("1")) {
			SmsUtil.setCheckCodeModifyPayPwdSwitch(true);
		} else {
			SmsUtil.setCheckCodeModifyPayPwdSwitch(false);
		}
		

	}

	public String getH5EventUrl() {
		return new StringBuilder().append(H5Url).append("/#/event-basic?id=").toString();
	}

	public String getH5MerchantUrl() {
		return new StringBuilder().append(H5Url).append("/#/merchant?id=").toString();
	}

	public String getH5ArticleUrl() {
		return new StringBuilder().append(H5Url).append("/#/article-detail?id=").toString();
	}

}