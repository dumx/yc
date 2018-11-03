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
public class WeixinCreateOrderBean {

    // 应用ID
    @XmlElement(name = "appid")
    private String appid;
    // 商品描述
    @XmlElement(name = "body")
    private String body;
    // 商品详情
    @XmlElement(name = "detail")
    private String detail;
    // 指定支付方式
    @XmlElement(name = "limit_pay")
    private String limit_pay;
    // 商户号
    @XmlElement(name = "mch_id")
    private String mch_id;
    // 随机字符串
    @XmlElement(name = "nonce_str")
    private String nonce_str;
    // 通知地址
    @XmlElement(name = "notify_url")
    private String notify_url;
    //退款单号，退款时传值。
    @XmlElement(name = "out_refund_no")
    private String out_refund_no;
    // 商户订单号
    @XmlElement(name = "out_trade_no")
    private String out_trade_no;
    //退款金额，默认全额退款，退款时传值。
    @XmlElement(name = "refund_fee")
    private Integer refund_fee;
    // 终端IP
    @XmlElement(name = "spbill_create_ip")
    private String spbill_create_ip;
    // 交易起始时间
    @XmlElement(name = "time_start")
    private String time_start;
    // 总金额
    @XmlElement(name = "total_fee")
    private int total_fee;
    // 交易类型
    @XmlElement(name = "trade_type")
    private String trade_type;
    // 操作员
    @XmlElement(name = "op_user_id")
    private String op_user_id;
    // 签名
    @XmlElement(name = "sign")
    private String sign;
}
