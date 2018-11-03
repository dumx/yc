package com.pay.yc.common.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * token验证相关返回模板
 * @author dumx
 * 2017年9月4日 下午4:23:43
 */
public class JSONResult {
	 public static String fillResultString(Integer status, String message, Object result,Object userModel) throws JSONException{
	        JSONObject jsonObject = new JSONObject(){{
	            put("status", status);
	            put("message", message);
	            put("result", result);
	            put("userInfo", JsonUtils.toJson(userModel));
	        }};

	        return jsonObject.toString();
	    }
}
