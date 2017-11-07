package com.lingdaoyi.cloud.utils.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.BankCardDTO;
import com.lingdaoyi.cloud.encrypt.other.StringUtils;
import com.lingdaoyi.cloud.utils.HttpUtils;

/**
 * 银行卡二、三、四元素认证
 * 
 * @author jack
 *
 */
public class BankCardUtil {
	private static String BANK_HOST = null;
	private static String BANK_PATH = null;
	private static String GET_METHOD = null;
	private static String BANK_APPCODE = null;

	public static void init() {
		BANK_HOST = PropertiesUtil.getProperty("auth.bank.host", PropertiesUtil.authConfig);
		BANK_PATH = PropertiesUtil.getProperty("auth.bank.path", PropertiesUtil.authConfig);
		GET_METHOD = PropertiesUtil.getProperty("auth.method.get", PropertiesUtil.authConfig);
		BANK_APPCODE = PropertiesUtil.getProperty("auth.bank.appcode", PropertiesUtil.authConfig);
	}

	/**
	 * 
	 * @param bankcard
	 *            银行卡卡号（必填）
	 * @param Mobile
	 *            手机号码（选填）
	 * @param cardNo
	 *            身份证号码（选填）
	 * @param realName
	 *            姓名（选填）
	 * @return
	 */
	public static BankCardDTO checkBankcard(String bankcard, String Mobile, String cardNo,
			String realName) {
		Gson gson = new Gson();
		BankCardDTO dto = null;
		if (StringUtils.isBlank(bankcard)) {
			dto = new BankCardDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setReason("bankcard" + PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Msg, "emc.msg.201"));
			return dto;
		}
		if (StringUtils.isBlank(Mobile) && StringUtils.isBlank(cardNo) && StringUtils.isBlank(realName)) {
			dto = new BankCardDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.201"));
			dto.setReason(
					"Mobile、cardNo、realName等可选参数最少1个不能为null！");
			return dto;
		}
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + BANK_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("Mobile", Mobile);
		querys.put("bankcard", bankcard);
		querys.put("cardNo", cardNo);
		querys.put("realName", realName);

		String dataString = "";

		try {
			HttpResponse response = HttpUtils.doGet(BANK_HOST, BANK_PATH, GET_METHOD, headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, BankCardDTO.class);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			dto = new BankCardDTO();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setReason(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.bank.500"));
			dto.setResult(null);
			return dto;
		}
		return dto;
	}
}
