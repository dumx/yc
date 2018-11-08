package com.pay.yc.dto.admin;


import com.pay.yc.common.result.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 配置管理
 */
@Getter
@Setter
public class ConfigDTO extends AbstractDTO {



    private String name;

    private Integer totalFee;

    /**
     * 开始日期(取年月日)
     */
    private Date beginTime;

    /**
     * 开始日期(取年月日)
     */
    private Date endTime;

//    /**
//     * 开始小时
//     */
//    private Integer beginHour;
//
//    /**
//     * 结束小时
//     */
//    private Integer endHour;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String remark;

    /**
     * 配置类型
     */
    private String type;

    /**
     * 背景图链接(暂无用)
     */
    private String link;



}
