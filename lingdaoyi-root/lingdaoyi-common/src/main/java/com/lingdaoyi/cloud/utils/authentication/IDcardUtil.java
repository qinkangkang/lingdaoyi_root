package com.lingdaoyi.cloud.utils.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.IdCardDTO;
import com.lingdaoyi.cloud.utils.HttpUtils;

/**
 * 身份证实名认证工具类
 * 
 * @author jack
 *
 */
public class IDcardUtil {
	private static String IDCARD_HOST = null;
	private static String IDCARD_PATH = null;
	private static String IDCARD_GET = null;
	private static String IDCARD_APPCODE = null;
	public static void init() {
		IDCARD_HOST = PropertiesUtil.getProperty("auth.idcard.host", PropertiesUtil.authConfig);
		IDCARD_PATH = PropertiesUtil.getProperty("auth.idcard.path", PropertiesUtil.authConfig);
		IDCARD_GET = PropertiesUtil.getProperty("auth.method.get", PropertiesUtil.authConfig);
		IDCARD_APPCODE = PropertiesUtil.getProperty("auth.idcard.appcode", PropertiesUtil.authConfig);
	}
	/**
	 * @param cardNo
	 *            身份证号
	 * @param realName
	 *            姓名
	 * @return
	 */
	public static IdCardDTO checkIDcard(String cardNo, String realName) {
		Gson gson = new Gson();
		IdCardDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + IDCARD_APPCODE);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("cardNo", cardNo);
		querys.put("realName", realName);
		String dataString = "";
		try {
			HttpResponse response = HttpUtils.doGet(IDCARD_HOST, IDCARD_PATH, IDCARD_GET, headers, querys);
			dataString = EntityUtils.toString(response.getEntity());
			dto = gson.fromJson(dataString, IdCardDTO.class);
			// System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
			dto.setReason(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.idcard.500"));
			dto.setResult(null);
		}
		return dto;
	}
}
