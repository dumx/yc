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
     * 状态
     * 1:正常:2:禁用，2:黑名单,3:正在申请，4：注销
     */
    private int status = 1;




    /**
     * 用户类型(0是超管/1商户)
     */
    private int userType = 1;

    private String openId;



}
