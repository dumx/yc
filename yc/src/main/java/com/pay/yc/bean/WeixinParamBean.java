package com.pay.yc.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeixinParamBean {

    /**
     *商户订单号,64个字符以内、只能包含字母、数字、下划线；需保证在商户端不重复
     */
    private String orderNo;

    private String openId;

    /**
     *商品标题
     */
    private String subject;

    /**
     *订单总金额，单位为分
     */
    private Long totalFee;

    /**
     *设备IP.
     */
    private String spbillCreateIp;

    /**
     *回调Url.
     */
    private String notifyUrl;

    /**
     *座位号
     */
    private Integer seatNo;

    /**
     *订单类型
     */
    private String type;

}
