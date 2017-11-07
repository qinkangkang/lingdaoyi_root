package com.lingdaoyi.cloud.utils.sms;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lingdaoyi.cloud.config.PropertiesUtil;
/*import com.lingdaoyi.cloud.config.SmsConfig;*/
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Sms短信发送工具类
 * 
 * @author jinsey
 *
 */
public class SmsUtil {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	public static final String isSms = "909090";

	/**
	 * 发送短信类型
	 */
	public static final int CheckCodeLoginSms = 100101; // 发送登陆短信验证码

	public static final int CheckCodeRegisterSms = 100102; // 发送注册验证码

	public static final int CheckCodeModifyloginPwdSms = 100103; // 发送修改登录密码验证码

	public static final int CheckCodeModifyPayPwdSms = 100104; // 发送修改支付密码验证码

	/**
	 * 短信模板必要id
	 */
	private static final String CheckCodeLoginSmsId = "SMS_35810160";

	private static final String CheckCodeRegisterSmsId = "SMS_52495249";

	private static final String CheckCodeModifyLoginPwdSmsId = "SMS_52495236";

	private static final String CheckCodeModifyPayPwdSmsId = "SMS_52810100";

	/**
	 * 初始化必要参数
	 */
	private static boolean CheckCodeLoginSwitch = true;

	private static boolean CheckCodeRegisterSwitch = true;

	private static boolean CheckCodeModifyLoginPwdSwitch = true;

	private static boolean CheckCodeModifyPayPwdSwitch = true;

	private static String SmsServerUrl = null;

	private static String Appkey = null;

	private static String Appsecret = null;

	private static String ChenkCodeSignName = null;

	private static TaobaoClient client = null;

	public static void init() {

		Appkey = PropertiesUtil.getProperty("sms.appkey", PropertiesUtil.smsConfig);
		Appsecret = PropertiesUtil.getProperty("sms.appsecret", PropertiesUtil.smsConfig);
		SmsServerUrl = PropertiesUtil.getProperty("sms.tabaoServiceUrl", PropertiesUtil.smsConfig);
		ChenkCodeSignName = PropertiesUtil.getProperty("sms.chenkCodeSignName", PropertiesUtil.smsConfig);
		client = new DefaultTaobaoClient(SmsServerUrl, Appkey, Appsecret);
	}

	/**
	 * 发送短信验证码方法
	 * 
	 * @param smsType
	 * @param phone
	 * @param smsParamMap
	 * @return
	 */
	public static SmsResult sendSms(int smsType, String phone, Map<String, String> smsParamMap) {
		SmsResult smsResult = new SmsResult();

		AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
		// 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，
		// 用户可以根据该会员ID识别是哪位会员使用了你的应用
		request.setExtend("foms");
		// 短信类型，传入值请填写normal
		request.setSmsType("normal");
		// 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
		// 示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
		String paramJson = mapper.toJson(smsParamMap);
		smsResult.setContent(paramJson);
		request.setSmsParamString(paramJson);
		// req.setSmsParamString("{\"code\":\"1234\",\"product\":\"【零到壹】\",\"item\":\"阿里大鱼\"}");
		// 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。
		// 群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
		request.setRecNum(phone);

		// 1.登陆短信验证码
		if (smsType == CheckCodeLoginSms) {
			// 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，
			// 则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
			// 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(CheckCodeLoginSmsId);
		} else if (smsType == CheckCodeRegisterSms) {
			// 2.发送注册验证码
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(CheckCodeRegisterSmsId);
		} else if (smsType == CheckCodeModifyloginPwdSms) {
			// 3.发送修改登录密码验证码
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(CheckCodeModifyLoginPwdSmsId);
		} else if (smsType == CheckCodeModifyPayPwdSms) {
			// 4.发送修改支付密码验证码
			request.setSmsFreeSignName(ChenkCodeSignName);
			request.setSmsTemplateCode(CheckCodeModifyPayPwdSmsId);
		}

		AlibabaAliqinFcSmsNumSendResponse response = null;
		try {
			response = client.execute(request);
			if (response.getResult() != null) {
				smsResult.setSuccess(response.getResult().getSuccess());
			}
			smsResult.setResponse(response.getBody());
		} catch (ApiException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		return smsResult;
	}

	public static boolean isCheckCodeLoginSwitch() {
		return CheckCodeLoginSwitch;
	}

	public static void setCheckCodeLoginSwitch(boolean checkCodeLoginSwitch) {
		CheckCodeLoginSwitch = checkCodeLoginSwitch;
	}

	public static boolean isCheckCodeRegisterSwitch() {
		return CheckCodeRegisterSwitch;
	}

	public static void setCheckCodeRegisterSwitch(boolean checkCodeRegisterSwitch) {
		CheckCodeRegisterSwitch = checkCodeRegisterSwitch;
	}

	public static boolean isCheckCodeModifyLoginPwdSwitch() {
		return CheckCodeModifyLoginPwdSwitch;
	}

	public static void setCheckCodeModifyLoginPwdSwitch(boolean checkCodeModifyLoginPwdSwitch) {
		CheckCodeModifyLoginPwdSwitch = checkCodeModifyLoginPwdSwitch;
	}

	public static boolean isCheckCodeModifyPayPwdSwitch() {
		return CheckCodeModifyPayPwdSwitch;
	}

	public static void setCheckCodeModifyPayPwdSwitch(boolean checkCodeModifyPayPwdSwitch) {
		CheckCodeModifyPayPwdSwitch = checkCodeModifyPayPwdSwitch;
	}

}