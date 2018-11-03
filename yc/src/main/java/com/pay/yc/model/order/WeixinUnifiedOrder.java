package com.pay.yc.model.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.result.AbstractModel;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * 订单(重写微信支付订单,包含预支付订单)
 * @author: dumx    
 * @date:   2017年11月13日 下午8:16:25   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
@Entity
public class WeixinUnifiedOrder extends AbstractModel {

	private static final long serialVersionUID = 1L;


    @NotNull
    @Length(max = 32)
    @Column(length = 32)
    private String appId;

    /**
     * 支付平台订单交易号。 微信(transaction_id)最大长度为32；支付宝(trade_no)最大程度为64
     */
    @Length(max = 32)
    @Column(length = 32, nullable = true)
    private String tradeNo;

    /**
     * 订单号
     */
    @NotNull
    @Length(max = 32)
    @Column(length = 32)
    private String orderNo;

    /**
     * 支付金额(单位分)
     * 微信(total_fee)是以分为单位；支付宝(total_amount)是单位是元，保留两位小数
     * 在此统一保存为分单位
     */
    @Range(min = 1)
    private Long totalFee;

    /**
     * 订单类型(WEIXIN:微信支付;ALIPAY:支付宝支付)
     */
    @Enumerated(EnumType.STRING)
    private PaymentOrderType type;

    /**
     * 交易状态<br/>(SUCCESS:成功;FAIL:失败;WATING:等待;REFUND_SUCCESS:退还成功)
     * 微信：根据result_code判断，SUCCESS/FAIL；<br />
     * 支付宝：根据trade_status判断。WAIT_BUYER_PAY/TRADE_CLOSED/TRADE_SUCCESS/
     * TRADE_FINISHED
     */
    @Enumerated(EnumType.STRING)
    private PaymentTradeStatus status = PaymentTradeStatus.WATING;

    /**
     * 支付完成时间。微信(time_end)yyyyMMddHHmmss；支付宝(gmt_close)yyyy-MM-yc HH:mm:ss
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnd;

    /**
     *订单标题
     */
    @Length(max = 128)
    @Column(length = 128)
    private String subject;


    /**
     * 微信专用
     */
    @Length(max = 32)
    @Column(length = 32)
    private String nonceStr;

    /**
     * 微信专用
     * 预付号
     */
    @Length(max = 64)
    @Column(length = 64)
    private String prepayId;

    /**
     * 微信专用
     * 时间字符串，获取时间/1000
     */
    @Length(max = 32)
    @Column(length = 32)
    private String timestamp;

    /**
     * 微信专用
     * 微信分配的商户号
     */
    @Length(max = 32)
    @Column(length = 32)
    private String spbill_create_ip;

    /**
     * 签名
     */
    @NotNull
    @Length(max = 256)
    @Column(length = 256)
    private String sign;
    
    /**
     * 原始订单通知json  返回的原始信息。
     */
    @Column(length = 2048)
    private String raw;


    
 

}
