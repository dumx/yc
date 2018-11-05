package com.pay.yc.dto.order;

import com.pay.yc.common.result.dto.AbstractDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WeixinUnifiedOrderDTO extends AbstractDTO {


    @ApiModelProperty(value="支付平台订单交易号",position = 1)
    @Length(max = 64)
    private String appid;

    @ApiModelProperty(value="商户号",position = 5)
    private String partnerid;


    @ApiModelProperty(value="微信预付号",position = 6)
    private String prepayid;

    @ApiModelProperty(value="扩展字段",position = 7)
    private String wxPackage;

    @ApiModelProperty(value="nonceStr",position = 4)
    private String nonceStr;



    @ApiModelProperty(value="微信签名时间戳",position = 7)
    private String timestamp;

//
    @ApiModelProperty(value=" 商户订单号。 微信(out_trade_no)最大长度为32；支付宝(out_trade_no)最大程度为64",position = 2)
    @NotNull
    @Length(max = 32)
    private String orderNo;
    @ApiModelProperty(value="sign",position = 3)
    private String sign;






}
