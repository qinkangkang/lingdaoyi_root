package com.lingdaoyi.cloud.utils.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO2;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO3;
import com.lingdaoyi.cloud.dto.RateInfoDTO;
import com.lingdaoyi.cloud.utils.HttpUtils;

/**
 * 汇率工具类
 * 
 * @author jack
 *
 */
public class ExchangeRateUtil2 {
	private static String EXCHANGERATE_GET2 = null;
	// private static final String POST= "POST";

	private static String EXCHANGERATE_HOST2 = null; // appurl

	private static String EXCHANGERATE_CURRENCYLIST2 = null; // 币种列表
	private static String EXCHANGERATE_EXCHANGERATE2 = null; // 汇率转换
	private static String EXCHANGERATE_CURRENCYSINGLE = null; // 单个币种汇率查询

	private static String EXCHANGERATE_APPCODE2 = null; // 汇率appcode

	public static void init() {
		EXCHANGERATE_HOST2 = PropertiesUtil.getProperty("auth.exchangerate.host2", PropertiesUtil.authConfig);
		EXCHANGERATE_CURRENCYLIST2 = PropertiesUtil.getProperty("auth.exchangerate.path.currencylist2",
				PropertiesUtil.authConfig);
		EXCHANGERATE_EXCHANGERATE2 = PropertiesUtil.getProperty("auth.exchangerate.path.exchangerate2",
				PropertiesUtil.authConfig);
		EXCHANGERATE_CURRENCYSINGLE = PropertiesUtil.getProperty("auth.exchangerate.path.currencysingle",
				PropertiesUtil.authConfig);

		EXCHANGERATE_GET2 = PropertiesUtil.getProperty("auth.method.get", PropertiesUtil.authConfig);
		EXCHANGERATE_APPCODE2 = PropertiesUtil.getProperty("auth.exchangerate.appcode2", PropertiesUtil.authConfig);
	}

	/**
	 * 支持汇率币种列表
	 */
	public static ExchangeRateDTO2 currencyList2() {
		Gson gson = new Gson();
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE2);
		Map<String, String> querys = new HashMap<String, String>();
		ExchangeRateDTO2 dto = null;
		String dataString = "";
		try {
			HttpResponse response = HttpUtils.doGet(EXCHANGERATE_HOST2, EXCHANGERATE_CURRENCYLIST2, EXCHANGERATE_GET2,
					headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO2.class);
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			dto = new ExchangeRateDTO2();
			dto.setStatus(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.currencylist.500"));
		}
		return dto;
	}

	/**
	 * 汇率转换
	 * 
	 * @param from
	 *            源币种
	 * @param amount
	 *            数量
	 * @param to
	 *            转换币种
	 * @return
	 */
	public static RateInfoDTO exchangeRate(String from, String amount, String to) {
		Gson gson = new Gson();
		RateInfoDTO dto = null;
		String dataString = "";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE2);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("amount", amount);
		querys.put("from", from);
		querys.put("to", to);
		try {
			HttpResponse response = HttpUtils.doGet(EXCHANGERATE_HOST2, EXCHANGERATE_EXCHANGERATE2, EXCHANGERATE_GET2,
					headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, RateInfoDTO.class);
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			dto = new RateInfoDTO();
			dto.setStatus(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.error.500"));
		}
		return dto;
	}

	/**
	 * 单个货币汇率查询接口
	 * 
	 * @param currency
	 * @return
	 */
	public static ExchangeRateDTO3 currencysingle(String currency) {
		Gson gson = new Gson();
		ExchangeRateDTO3 dto = null;
		String dataString = "";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE2);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("currency", currency);
		try {
			HttpResponse response = HttpUtils.doGet(EXCHANGERATE_HOST2, EXCHANGERATE_CURRENCYSINGLE, EXCHANGERATE_GET2,
					headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO3.class);
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			dto = new ExchangeRateDTO3();
			dto.setStatus(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.error.500"));
		}
		return dto;
	}
}
