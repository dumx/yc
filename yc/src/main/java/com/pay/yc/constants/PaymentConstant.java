package com.pay.yc.constants;

/**
 * 常量的定义
 * @version 1.0
 * @see
 */
public class PaymentConstant {
    //交易结束，不可退款
    public final static String TRADE_FINISHED = "TRADE_FINISHED";
    //交易支付成功
    public final static String TRADE_SUCCESS = "TRADE_SUCCESS";
    //交易创建，等待买家付款
    public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    //未付款交易超时关闭，或支付完成后全额退款
    public final static String TRADE_CLOSED = "TRADE_CLOSED";
    //微信交易类型，默认值为APP
    public final static String TRADE_TYPE = "APP";
    //微信生成预订单接口。
    public final static String WX_PREPARE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信退款接口。
    public final static String WX_REFUND_ORDER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //签名packageValue
    public static String PACKAGE_VALUE="Sign=WXPay";


}
