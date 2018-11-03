package com.pay.yc.paysign;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;



/**
 * MD5加密
 * @author: dumx    
 * @date:   2017年11月14日 上午9:27:40   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
public class MD5 {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(final byte[] b) {
        final StringBuilder resultSb = new StringBuilder();
        for (final byte aB : b) {
            resultSb.append(MD5.byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(final byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        final int d1 = n / 16;
        final int d2 = n % 16;
        return MD5.hexDigits[d1] + MD5.hexDigits[d2];
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(final String origin) {
        String resultString = null;
        try {
            resultString = origin;
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(resultString.getBytes("UTF-8"));
            resultString = MD5.byteArrayToHexString(md.digest());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }


    //aliMD5签名
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, final String key, final String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(MD5.getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, final String sign, final String key, final String input_charset) {
        text = text + key;
        final String mysign = DigestUtils.md5Hex(MD5.getContentBytes(text, input_charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(final String content, final String charset) {
        if ((charset == null) || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
