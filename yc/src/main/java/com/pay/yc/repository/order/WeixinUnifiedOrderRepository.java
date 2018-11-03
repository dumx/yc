package com.pay.yc.repository.order;

import com.pay.yc.model.order.WeixinUnifiedOrder;
import org.springframework.data.repository.Repository;

public interface WeixinUnifiedOrderRepository extends Repository<WeixinUnifiedOrder, Long>, WeixinUnifiedOrderRepositoryCustom {

    WeixinUnifiedOrder findOneByOrderNo(final String orderNo);

    WeixinUnifiedOrder findOneById(final Long id);

    WeixinUnifiedOrder save(final WeixinUnifiedOrder model);

//    WeixinUnifiedOrder save(final UnifiedOrder model);
    
    //下订单(包含统一下单)
//    WeixinUnifiedOrder save(final WeixinUnifiedOrder model);

    void delete(final Long id);

    String findStatusByTradeNo(final String outTradeNo);

}