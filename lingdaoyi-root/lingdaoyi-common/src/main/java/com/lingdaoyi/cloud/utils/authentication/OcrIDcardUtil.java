package com.lingdaoyi.cloud.utils.authentication;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lingdaoyi.cloud.config.PromptInfoUtil;
import com.lingdaoyi.cloud.config.PropertiesUtil;
import com.lingdaoyi.cloud.dto.OcrIdCardDTO;
import com.lingdaoyi.cloud.utils.HttpUtils;

/**
 * 身份证扫图片描工具类
 * 
 * @author jack
 *
 */
public class OcrIDcardUtil {
	private static String OCRIDCARD_HOST = null;
	private static String OCRIDCARD_PATH = null;
	private static String OCRIDCARD_POST = null;
	private static String OCRIDCARD_APPCODE = null;

	public static void init() {
		OCRIDCARD_HOST = PropertiesUtil.getProperty("auth.ocridcard.host", PropertiesUtil.authConfig);
		OCRIDCARD_PATH = PropertiesUtil.getProperty("auth.ocridcard.path", PropertiesUtil.authConfig);
		OCRIDCARD_POST = PropertiesUtil.getProperty("auth.method.post", PropertiesUtil.authConfig);
		OCRIDCARD_APPCODE = PropertiesUtil.getProperty("auth.ocridcard.appcode", PropertiesUtil.authConfig);
	}

	/**
	 * 
	 * @param image
	 *            图片二进制Base64编码 必填 注意：图片不能大于1.5M
	 * @param configure
	 *            照片正面或反面（face/back）必填
	 * @return
	 */
	public static OcrIdCardDTO ocrIDcard(String image, String configure) {
		Gson gson = new Gson();
		OcrIdCardDTO dto = null;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + OCRIDCARD_APPCODE);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		String bodys = "{" + "\"inputs\": [" + "{" + "\"image\": {" + "\"dataType\": 50," + "\"dataValue\": \"" + image
				+ "\"" + " }," + " \"configure\": {" + " \"dataType\": 50," + "\"dataValue\": \"{\\\"side\\\":\\\""
				+ configure + "\\\"}\"" + "}" + "}" + "]" + "}";

		String dataString = "";

		try {
			HttpResponse response = HttpUtils.doPost(OCRIDCARD_HOST, OCRIDCARD_PATH, OCRIDCARD_POST, headers, querys,
					bodys);

			dataString = EntityUtils.toString(response.getEntity());

			dto = gson.fromJson(dataString, OcrIdCardDTO.class);

		} catch (Exception e) {
			e.printStackTrace();
			dto = new OcrIdCardDTO();
			dto.setSuccess(false);
			dto.setMsg(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Auth_Msg, "emc.auth.oceidcard.500"));
			dto.setError_code(PromptInfoUtil.getPrompt(PromptInfoUtil.EMC_Error_Code, "emc.code.500"));
		}
		return dto;
	}
}
