package com.pay.yc.paysign;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 *
 */
public class SignatureHelper {
	private static Log log = LogFactory.getLog(SignatureHelper.class);

	private static PrivateKey _PRIVATE_KEY;

	private static PublicKey _PLATFORM_PUBLICE_KEY;

	public static void setPrivateKey(PrivateKey PrivateKey) {
		_PRIVATE_KEY = PrivateKey;
	}

	public static void setPlatformPubliceKey(PublicKey PlatformPubliceKey) {
		_PLATFORM_PUBLICE_KEY = PlatformPubliceKey;
	}

	public static void main(String[] args) throws Exception {
		String payload = "测试数据验证加解密";
		// 数据加密
		String encPayloadBase64 = SignatureHelper.encrypt2Base64(payload);
		System.out.println("加密数据： " + encPayloadBase64);
		String signatureBase64 = SignatureHelper.sign2Base64(payload);
		System.out.println("签名数据： " + signatureBase64);


		// 解密验证数据
		byte[] plainBytes = SignatureHelper.decrypt2Bytes(encPayloadBase64);
		System.out.println("解密明文数据： " + new String(plainBytes, StandardCharsets.UTF_8));

		boolean flg = SignatureHelper.verifyByBase64(plainBytes, signatureBase64);
		System.out.println("验签结果： " + (flg ? "成功" : "失败"));
	}

	/**
	 * 针对机构公钥对明文数据、上送签名，进行验签比对
	 *
	 * @param plainBytes
	 * @param signatureBase64
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyByBase64(byte[] plainBytes, String signatureBase64) throws Exception {
		try {
			// byte[] contentBytes = plainConent.getBytes(charset);
			byte[] signatureBytes = Base64.decode(signatureBase64);
			return RSAHelper.verify(plainBytes, _PLATFORM_PUBLICE_KEY, signatureBytes);
		}
		catch(Exception e) {
			log.error(e, e);
			throw new Exception("验证签名异常");
		}
	}

	/**
	 * 针对机构公钥对明文数据进行加密，转Base64码
	 *
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encrypt2Base64(String content) throws Exception {
		try {
			byte[] encrptyBytes = RSAHelper.encrypt(_PLATFORM_PUBLICE_KEY, content.getBytes(StandardCharsets.UTF_8));
			return Base64.encode(encrptyBytes);
		}
		catch(Exception e) {
			log.error(e, e);
			throw new Exception("加密报文异常 ");
		}
	}

	/**
	 * 使用平台私钥对数据进行签名，转成Base64码
	 *
	 * @param plainContent
	 * @return
	 * @throws Exception
	 */
	public static String sign2Base64(String plainContent) throws Exception {
		try {
			byte[] signature = RSAHelper.sign(_PRIVATE_KEY, plainContent.getBytes(StandardCharsets.UTF_8));
			return Base64.encode(signature);
		}
		catch(Exception e) {
			log.error(e, e);
			throw new Exception("数据签名异常");
		}
	}

	/**
	 * 使用平台私钥解密被加密的数据
	 *
	 * @param encryptedBase64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt2Bytes(String encryptedBase64) throws Exception {
		try {
			byte[] encrptyBytes = Base64.decode(encryptedBase64);
			return RSAHelper.decrypt2Bytes(_PRIVATE_KEY, encrptyBytes);
		}
		catch(Exception e) {
			log.error(e, e);
			throw new Exception("业务报文无法解码 ");
		}
	}
}
