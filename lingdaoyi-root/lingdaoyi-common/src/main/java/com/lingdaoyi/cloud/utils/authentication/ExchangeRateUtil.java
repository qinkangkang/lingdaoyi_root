package com.lingdaoyi.cloud.utils.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.ExchangeRateDTO;
import com.lingdaoyi.cloud.utils.HttpUtils;

/**
 * 汇率工具类
 * 
 * @author jack
 *
 */
public class ExchangeRateUtil {
	private static String EXCHANGERATE_GET = null;
	// private static final String POST= "POST";

	private static String EXCHANGERATE_HOST = null; // appurl

	private static String EXCHANGERATE_CURRENCYLIST = null; // 支持币种列表
	private static String EXCHANGERATE_EXCHANGERATE = null; // 汇率转换
	private static String EXCHANGERATE_BANK10 = null; // 10大银行实时汇率
	private static String EXCHANGERATE_CHINABANK = null; // 中国银行实时汇率

	private static String EXCHANGERATE_APPCODE = null; // 汇率appcode

	public static void init() {
		EXCHANGERATE_HOST = PropertiesUtil.getProperty("auth.exchangerate.host", PropertiesUtil.authConfig);
		EXCHANGERATE_CURRENCYLIST = PropertiesUtil.getProperty("auth.exchangerate.path.currencylist",
				PropertiesUtil.authConfig);
		EXCHANGERATE_EXCHANGERATE = PropertiesUtil.getProperty("auth.exchangerate.path.exchangerate",
				PropertiesUtil.authConfig);
		EXCHANGERATE_BANK10 = PropertiesUtil.getProperty("auth.exchangerate.path.bank10", PropertiesUtil.authConfig);
		EXCHANGERATE_CHINABANK = PropertiesUtil.getProperty("auth.exchangerate.path.chinabank",
				PropertiesUtil.authConfig);
		EXCHANGERATE_GET = PropertiesUtil.getProperty("auth.method.get", PropertiesUtil.authConfig);
		EXCHANGERATE_APPCODE = PropertiesUtil.getProperty("auth.exchangerate.appcode", PropertiesUtil.authConfig);
	}

	/**
	 * 支持汇率币种列表
	 */
	public static ExchangeRateDTO queryCurrencyList() {
		Gson gson = new Gson();
		ExchangeRateDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		HttpResponse response = null;
		String dataString = "";
		try {
			response = HttpUtils.doGet(EXCHANGERATE_HOST, EXCHANGERATE_CURRENCYLIST, EXCHANGERATE_GET, headers, querys);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO.class);
		} catch (Exception e) {
			// e.printStackTrace();
			dto = new ExchangeRateDTO();
			dto.setShowapi_res_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setShowapi_res_error(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.currencylist.500"));
			dto.setShowapi_res_body(null);
		}

		return dto;
	}

	/**
	 * 汇率转换
	 * 
	 * @param fromCode
	 *            源货币类型
	 * @param money
	 *            转换的金额（元）
	 * @param toCode
	 *            目标货币类型
	 * @return
	 */
	public static ExchangeRateDTO exchangeRateConversion(String fromCode, String money, String toCode) {
		Gson gson = new Gson();
		ExchangeRateDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		// querys.put("fromCode", "GBP");
		// querys.put("money", "100");
		// querys.put("toCode", "EUR");
		querys.put("fromCode", fromCode);
		querys.put("money", money);
		querys.put("toCode", toCode);
		HttpResponse response = null;
		String dataString = "";

		try {
			response = HttpUtils.doGet(EXCHANGERATE_HOST, EXCHANGERATE_EXCHANGERATE, EXCHANGERATE_GET, headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO.class);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			// e.printStackTrace();
			dto = new ExchangeRateDTO();
			dto.setShowapi_res_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setShowapi_res_error(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.conversion.500"));
			dto.setShowapi_res_body(null);
		}
		return dto;
	}

	/**
	 * 10大银行实时汇率表
	 * 
	 * @param bankCode
	 *            银行编码， 工商银行：ICBC ， 中国银行：BOC ， 农业银行：ABCHINA ， 交通银行：BANKCOMM ，
	 *            建设银行：CCB ， 招商银行：CMBCHINA ， 光大银行：CEBBANK ， 浦发银行：SPDB ， 兴业银行：CIB ，
	 *            中信银行：ECITIC
	 * @return
	 */
	public static ExchangeRateDTO exchangeRateBank10(String bankCode) {
		Gson gson = new Gson();
		ExchangeRateDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("bankCode", bankCode);
		HttpResponse response = null;
		String dataString = "";

		try {
			response = HttpUtils.doGet(EXCHANGERATE_HOST, EXCHANGERATE_BANK10, EXCHANGERATE_GET, headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO.class);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			// e.printStackTrace();
			dto = new ExchangeRateDTO();
			dto.setShowapi_res_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setShowapi_res_error(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.bank10.500"));
			dto.setShowapi_res_body(null);
		}
		return dto;
	}

	/**
	 * 中国银行的实时汇率表
	 * 
	 * @param code
	 *            此参数可以为null 需要查询的货币缩写。比如人民币是CNY，美元是USD，如果不输入，则返回全部。
	 * @return
	 */
	public static ExchangeRateDTO exchangeRateChina(String code) {
		Gson gson = new Gson();
		ExchangeRateDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + EXCHANGERATE_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("code", code);
		HttpResponse response = null;
		String dataString = "";
		try {
			response = HttpUtils.doGet(EXCHANGERATE_HOST, EXCHANGERATE_CHINABANK, EXCHANGERATE_GET, headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, ExchangeRateDTO.class);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			// e.printStackTrace();
			dto = new ExchangeRateDTO();
			dto.setShowapi_res_error(
					PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.exchangerate.chinabank.500"));
			dto.setShowapi_res_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setShowapi_res_body(null);
		}
		return dto;
	}

}
