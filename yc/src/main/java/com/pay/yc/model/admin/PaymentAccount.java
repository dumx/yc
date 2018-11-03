package com.pay.yc.model.admin;

import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class PaymentAccount extends AbstractModel {

    /**
     * 账户余额
     */
    @Column
    private Long balance;

    /**
     * 商户id
     */
    @Column
    private Long UserId;
}
