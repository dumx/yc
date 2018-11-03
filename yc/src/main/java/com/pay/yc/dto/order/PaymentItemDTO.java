package com.pay.yc.dto.order;

import com.pay.yc.common.result.dto.AbstractDTO;
import com.pay.yc.model.admin.PaymentItem;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 金额变动记录
 */
@Getter
@Setter
public class PaymentItemDTO extends AbstractDTO {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

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
    private PaymentItem.Type type;

    @Enumerated(EnumType.STRING)
    private PaymentItem.State state;

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
