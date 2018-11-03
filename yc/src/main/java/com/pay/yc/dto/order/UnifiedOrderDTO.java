package com.pay.yc.dto.order;

import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.result.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
public class UnifiedOrderDTO extends AbstractDTO {

	
	   /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 交易状态<br/>(SUCCESS:成功;FAIL:失败;WATING:等待;REFUND_SUCCESS:退还成功)
     * 微信：根据result_code判断，SUCCESS/FAIL；<br />
     * 支付宝：根据trade_status判断。WAIT_BUYER_PAY/TRADE_CLOSED/TRADE_SUCCESS/
     * TRADE_FINISHED
     */
    @Enumerated(EnumType.STRING)
    private PaymentTradeStatus status = PaymentTradeStatus.WATING;


    /**
     * 订单是否完成
     */
    private String state;

    /**
     * 支付金额(单位分)
     * 微信(total_fee)是以分为单位；支付宝(total_amount)是单位是元，保留两位小数
     * 在此统一保存为分单位
     */
    @Range(min = 1)
    private Long totalFee;

    /**
     *订单标题
     */
    @Length(max = 128)
    private String subject;

    /**
     * platform支付平台(IOS和ANDROID)
     */

    private String platform;
    /**
     * 用户id
     */
    private Long UserId;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 支付方式
     * @return
     */
    private Integer payType;

    private String notifyUrl;

    private String quitUrl;

    private String appId;

    private String alipayName;

    public static UnifiedOrderDTO build(){
        return new UnifiedOrderDTO();
    }

}
