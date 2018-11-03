package com.pay.yc.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("xml")
public class WeixinNotifyResponse {

    // 返回状态码
    private String return_code;
    // 返回信息
    private String return_msg;

}
