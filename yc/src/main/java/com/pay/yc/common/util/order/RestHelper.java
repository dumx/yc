package com.pay.yc.common.util.order;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.pay.yc.common.util.JsonUtils;


/**
 * 微信支付专用
 * @author: dumx    
 * @date:   2017年11月15日 上午10:58:11   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
public class RestHelper {

    /**
     * 判断rest请求的结果是否正确
     *
     * @param map
     */
    @SuppressWarnings("rawtypes")
    public static boolean isSuccess(final Map map) {
        return ((map.get("status") != null) && "success".equals(map.get("status").toString()));
    }

    /**
     * 生成请求体
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static HttpEntity<String> generateEntity(final Map params) {
        final HttpEntity<String> formEntity;
        if (params == null) {
            formEntity = new HttpEntity<>("");
        } else {
            final HttpHeaders headers = new HttpHeaders();
            final MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            formEntity = new HttpEntity<>(JsonUtils.toJson(params), headers);
        }
        return formEntity;
    }

    /**
     * 生成请求体
     *
     * @param params
     * @return
     */
    public static HttpEntity<String> generateEntity(final String params) {
        final HttpEntity<String> formEntity;
        if (StringUtils.isBlank(params)) {
            formEntity = new HttpEntity<>("");
        } else {
            final HttpHeaders headers = new HttpHeaders();
            final MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            formEntity = new HttpEntity<>(params, headers);
        }
        return formEntity;
    }

    /**
     * 生成请求体
     *
     * @return
     */
    public static HttpEntity<String> generateEntity() {
        return RestHelper.generateEntity("");
    }
}
