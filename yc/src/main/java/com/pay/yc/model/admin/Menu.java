package com.pay.yc.model.admin;


import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author loren
 * 2018年1月19日 下午3:45:24
 * 普通用户
 */
@Getter
@Setter
//@Entity
public class Menu extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column
    private Long parentId;

    /**
     * 名称
     */
    @Column
    private String title;

    /**
     * 链接
     */
    @Column
    private String link;

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
     * 图标样式
     */
    @Column
    private String icon;

}
