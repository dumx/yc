package com.pay.yc.dto.admin;


import com.pay.yc.common.result.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDTO extends AbstractDTO {

    /**
     * 用户商户编号
     */
    private String UserNo;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    //余额
    private BigDecimal balance;

    /**
     * 手机号
     */
    private String mobile;



    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 商户等级
     */
    private String level;



    /**
     * 未到账金额
     */
    /*@Column
    private BigDecimal waitBalance;*/

    /**
     * 状态
     * 1:正常:2:禁用，2:黑名单,3:正在申请，4：注销
     */
    private int status = 1;

    /**
     * 启用时间
     */
    private LocalDateTime applyTime;

    /**
     * 审核通过时间
     */
    private LocalDateTime reviewTime;

    /**
     * 用户类型(0是超管/1商户)
     */
    private int userType = 1;

    /**
     * AliPay上游账号接入资料
     */
    private int aliPayConfigId;

    /**
     * WeChatPay
     */
    private int weChatConfigId;

    /**
     * 商户公钥
     */
    private String publicKey;

    /**
     * 商户回调地址
     */
    private String notifyUrl;

    private List<Long> alipayConfigList;

    private Integer rate;




}
