package com.pay.yc.repository.admin;

import com.pay.yc.model.admin.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yjl on 2017/8/7.
 */
@Transactional
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {



}
