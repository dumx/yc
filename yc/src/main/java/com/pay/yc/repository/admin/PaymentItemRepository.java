package com.pay.yc.repository.admin;

import com.pay.yc.model.admin.PaymentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long> {
    Page<PaymentItem> findAll(Pageable pageable);


    PaymentItem findOne(Long id);

    PaymentItem save(PaymentItem paymentItem);

}
