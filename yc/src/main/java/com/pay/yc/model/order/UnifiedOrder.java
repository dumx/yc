package com.pay.yc.model.order;

import com.google.common.primitives.Longs;
import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class  UnifiedOrder extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1720589341953118029L;

    /**
     * 订单创建时间
     */
    private Date createTime;

    //用户唯一标识
    private String openId;

    /**
     * 订单号
     */
    @NotNull
    @Length(max = 32)
    @Column(length = 32)
    private String orderNo;


    private Long userId;



    @Column(length = 32)
    private Integer seatNo;

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
     *订单标题
     */
    @Length(max = 128)
    @Column(length = 128)
    private String subject;

    /**
     * 交易状态<br/>(SUCCESS:成功;FAIL:失败;WATING:等待;REFUND_SUCCESS:退还成功)
     * 微信：根据result_code判断，SUCCESS/FAIL；<br />
     * 支付宝：根据trade_status判断。WAIT_BUYER_PAY/TRADE_CLOSED/TRADE_SUCCESS/
     * TRADE_FINISHED
     */
    @Enumerated(EnumType.STRING)
    private PaymentTradeStatus status = PaymentTradeStatus.WATING;

    /**
     * 订单是否完成状态
     */
    private String state;

    /**
     * platform支付平台(IOS和ANDROID)
     */
    private String platform;

    /**
     * 完成时间
     */
    @Column
    private Date finishTime;

    /**
     *订单类型
     */
    @Column
    private String orderType;


    /**
     * 开始日期(取年月日)
     */
    @Column
    private Date beginTime;

    /**
     * 结束日期(取年月日)
     */
    @Column
    private Date endTime;

//    /**
//     * 开始小时
//     */
//    @Column
//    private Integer beginHour;
//
//    /**
//     * 结束小时
//     */
//    @Column
//    private Integer endHour;



}
