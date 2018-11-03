package com.pay.yc.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.NONE)
public class WeixinNotifyBean {

    // 返回状态码
    @XmlElement(name = "return_code")
    private String returnCode;
    // 返回信息
    @XmlElement(name = "return_msg")
    private String returnMsg;
    // 应用ID
    @XmlElement(name = "appid")
    private String appid;
    // 商家数据包
    @XmlElement(name = "attach")
    private String attach;
    // 付款银行
    @XmlElement(name = "bank_type")
    private String bankType;
    // 现金支付金额
    @XmlElement(name = "cash_fee")
    private int cashFee;
    // 现金支付货币类型
    @XmlElement(name = "cash_fee_type")
    private String cashFeeType;
    // 设备号
    @XmlElement(name = "device_info")
    private String deviceInfo;
    // 错误代码
    @XmlElement(name = "err_code")
    private String errCode;
    // 错误代码描述
    @XmlElement(name = "err_code_des")
    private String errCodeDes;
    // 货币种类
    @XmlElement(name = "fee_type")
    private String feeType;
    // 是否关注公众账号
    @XmlElement(name = "is_subscribe")
    private String isSubscribe;
    // 商户号
    @XmlElement(name = "mch_id")
    private String mchId;
    // 随机字符串
    @XmlElement(name = "nonce_str")
    private String nonceStr;
    // 用户表示
    @XmlElement(name = "openid")
    private String openid;
    // 商户订单号
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    // 业务结果
    @XmlElement(name = "result_code")
    private String resultCode;
    // 签名
    @XmlElement(name = "sign")
    private String sign;
    // 支付完成时间
    @XmlElement(name = "time_end")
    private String timeEnd;
    // 总金额
    @XmlElement(name = "total_fee")
    private String totalFee;
    // 交易类型
    @XmlElement(name = "trade_type")
    private String tradeType;
    // 微信支付订单号
    @XmlElement(name = "transaction_id")
    private String transactionId;

}
