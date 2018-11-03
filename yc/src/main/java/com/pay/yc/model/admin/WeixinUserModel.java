package com.pay.yc.model.admin;

import com.pay.yc.common.result.AbstractModel;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;

/**
 * Created by yjl on 2017/8/2.
 * 微信用户
 */
public abstract class WeixinUserModel extends AbstractModel {
    //微信信息
    @Length(max = 64)
    @Column(length=64)
    private String openid;

    /**
     * 微信昵称
     */
    @Column(length = 64)
    private String nickname;

    /**
     * 性别
     */
    @Column(length = 1)
    private String sex;

    /**
     * 语言
     */
    @Length(max = 32)
    @Column(length = 32)
    private String language;

    /**
     * 省市
     */
    @Length(max = 32)
    @Column(length = 32)
    private String province;

    /**
     * 城市
     */
    @Length(max = 32)
    @Column(length = 32)
    private String city;

    /**
     * 微信头像
     */
    @Length(max = 256)
    @Column(length = 256)
    private String portrait;
}
