package com.pay.yc.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("xml")
public class WxPayUnifiedOrderBean {

    // 应用ID
    private String appid;
    // 商户号
    @XStreamAlias("mch_id")
    private String mch_id;
    // 设备号("非必须)
    @XStreamAlias("device_info")
    private String device_info = "WEB";
    // 随机字符串
    @XStreamAlias("nonce_str")
    private String nonce_str;
    // 签名
    private String sign;
    // 商品描述
    private String body;
    // 商品详情(非必须)
    private String detail;
    // 附加数据(非必须)
    private String attach;
    // 商户订单号(商户系统内部的订单号)
    @XStreamAlias("out_trade_no")
    private String out_trade_no;
    // 货币类型(非必须)
    private String fee_type = "CNY";
    // 总金额(单位:分)
    @XStreamAlias("total_fee")
    private int	total_fee;
    // 终端IP
    @XStreamAlias("spbill_create_ip")
    private String spbill_create_ip;
    // 交易起始时间(非必须)
    @XStreamAlias("time_start")
    private String time_start;
    // 交易结束时间(非必须)
    @XStreamAlias("time_expire")
    private String time_expire;
    // 商品标记(非必须)
    @XStreamAlias("goods_tag")
    private String goods_tag;
    // 通知地址
    @XStreamAlias("notify_url")
    private String notify_url;
    // 交易类型
    private String trade_type = "APP";
    // 指定支付方式(非必须)(no_credit--指定不能使用信用卡支付)
    @XStreamAlias("limit_pay")
    private String limit_pay;
}
