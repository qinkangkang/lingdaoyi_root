package com.lingdaoyi.cloud.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.lingdaoyi.cloud.config.PropertiesUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class DesUtil3 {

	public static String KEY = PropertiesUtil.getProperty("system.key", PropertiesUtil.systemConfig);

	private static String KEY_ALGORITHM = null;

	private static String DEFAULT_CIPHER_ALGORITHM = null;

	public static void init() {
		KEY_ALGORITHM = PropertiesUtil.getProperty("system.keydes", PropertiesUtil.systemConfig);
		DEFAULT_CIPHER_ALGORITHM = PropertiesUtil.getProperty("system.defaultca", PropertiesUtil.systemConfig);
	}

	public static String encryptThreeDESECB(String src, String key) throws Exception {
		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		byte[] b = cipher.doFinal(src.getBytes());

		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");

	}

	// 3DESECB解密,key必须是长度大于等于 3*8 = 24 位
	public static String decryptThreeDESECB(String src, String key) throws Exception {
		// --通过base64,将字符串转成byte数组
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytesrc = decoder.decodeBuffer(src);
		// --解密的key
		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// --Chipher对象解密
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		byte[] retByte = cipher.doFinal(bytesrc);

		return new String(retByte);
	}

	// public static void main(String[] args) throws Exception {
	//// System.out.println(decryptThreeDESECB("p8sKEHXmqDOOJA2VSCbkpA==","EC4CC475-3DA3-45A6-B947-6282FD45F282"));
	//// String s = "hello world";
	//// System.out.println("原生："+s);
	//// System.out.println("加密:"+encryptThreeDESECB(s,KEY));
	//// System.out.println("解密："+decryptThreeDESECB(encryptThreeDESECB(s,KEY),KEY));
	//
	// User user=new User("张三", "男", 18);
	//
	// JsonMapper jm= new JsonMapper();
	//
	// String json = jm.toJson(user);
	//
	// System.out.println("对象json："+json);
	//
	// json=encryptThreeDESECB(json,KEY);
	//
	// System.out.println("加密:"+json);
	//
	// json=decryptThreeDESECB(json,KEY);
	//
	// System.out.println("解密："+json);
	//
	// user=jm.fromJson(json, User.class);
	//
	// System.out.println("姓名："+user.getName()+"，性别："+user.getSex()+"，年龄："+user.getAge());
	//
	// }

}
