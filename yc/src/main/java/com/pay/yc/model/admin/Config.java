package com.pay.yc.model.admin;


import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * 配置管理
 */
@Getter
@Setter
@Entity
public class Config extends AbstractModel {

    private static final long serialVersionUID = 1L;


    /**
     * 名称
     */
    @Column
    private String name;

    @Column
    private Integer totalFee;

    /**
     * 开始日期(取年月日)
     */
    @Column
    private Date dayTime;

    /**
     * 开始小时
     */
    @Column
    private Integer beginHour;

    /**
     * 结束小时
     */
    @Column
    private Integer endHour;

    /**
     * 排序
     */
    @Column
    private Integer sort;

    /**
     * 描述
     */
    @Column
    private String remark;

    /**
     * 配置类型
     */
    @Column
    private String type;

    /**
     * 背景图链接(暂无用)
     */
    @Column
    private String link;



}
