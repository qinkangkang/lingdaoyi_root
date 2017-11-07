package com.lingdaoyi.cloud.utils;

import org.apache.commons.lang3.StringUtils;

import com.lingdaoyi.cloud.encrypt.DesUtil3;

public class SignUtil {

	/**
	 * 接口验证码 
	 * @param addressIP 用户ip
	 * @param clientType 
	 * @param sign
	 * @return
	 */
	public static boolean httpEncrypt(String addressIP, Integer clientType,String sign) {

		if(StringUtils.isBlank(sign)) {
			return false;
		}
		StringBuilder uploadJson = new StringBuilder();
		uploadJson.append(clientType);
		uploadJson.append(addressIP);
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24 ));
		uploadJson.append(timestamp);

		String localSign = "";
		try {
			localSign = DesUtil3.encryptThreeDESECB(uploadJson.toString(), DesUtil3.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sign.equals(localSign)){
			return true;
		}else {
			return true;
		}
	}
}
