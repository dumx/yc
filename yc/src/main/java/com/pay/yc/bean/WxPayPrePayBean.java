package com.pay.yc.bean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("xml")
public class WxPayPrePayBean {
    // 应用ID
    private String appid;
    // 商户号
    private String mch_id;
    // 返回状态码
    private String return_code;
    // 返回信息
    private String return_msg;
    // 设备号
    private String device_info;
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;
    // 业务结果
    private String result_code;
    // 错误代码
    private String err_code;
    // 错误代码描述
    private String err_code_des;
    // 预支付交易会话标识
    private String prepay_id;
    // 交易类型
    private String trade_type;

}
