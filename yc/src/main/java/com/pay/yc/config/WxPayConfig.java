package com.pay.yc.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * @author: dumx    
 * @date:   2017年11月14日 上午9:23:05   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class WxPayConfig implements Serializable {

    private static final long serialVersionUID = 3551144040822338352L;

    /**
     * AppId
     */
    private String appId;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * secret
     */
    private String secret;

}
