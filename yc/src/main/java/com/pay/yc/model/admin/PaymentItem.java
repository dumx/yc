package com.pay.yc.model.admin;

import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 金额变动记录
 */
@Getter
@Setter
@Entity
public class PaymentItem extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 代付详情号
     */
    private Long tradeDetailId;

    /**
     * 实际金额变动
     */
    private Long amount;

    /**
     * 发生金额
     */
    private Long occAmount;

    /**
     * 当前余额
     */
    private Long balance;

    private Long UserId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private State state;

    public static enum Type {
        /**
         * 充值
         */
        CHARGE,
        /**
         * 代付
         */
        PAY,
        /**
         * 代付返还
         */
        RETURNPAY
    }

    public static enum State{
        /**
         * 成功
         */
        SUCCESS,
        /**
         * 失败
         */
        ERROR
    }
}
