package com.pay.yc.repository.order;

import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.model.order.UnifiedOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UnifiedOrderRepository extends Repository<UnifiedOrder, Long>{
   UnifiedOrder save(final UnifiedOrder model);


   UnifiedOrder findByOrderNo(String orderNo);

   //获取未支付订单,按订单创建时间排序
   Page<UnifiedOrder> findByUserIdAndStatusOrderByCreateTimeDesc(Long uid, PaymentTradeStatus status, Pageable pageable);


   //获取已完成和未完成订单
   Page<UnifiedOrder> findByUserIdAndStateAndStatusOrderByCreateTimeDesc(Long uid, String state, PaymentTradeStatus status, Pageable pageable);

   //管理端
   //获取未支付订单,按订单创建时间排序
   Page<UnifiedOrder> findByStateAndStatusOrderByCreateTimeDesc(String state,PaymentTradeStatus status, Pageable pageable);

   /**
    * 根据状态和结束时间获取订单列表
    * @param state
    * @param teachingTime
    * @param startTime
    * @param pageable
    * @return
    */
   Page<UnifiedOrder> findByStateAndCreateTimeAndFinishTimeOrderByCreateTimeDesc(String state, String teachingTime,int startTime, Pageable pageable);

   //根据订单号查询订单,更改订单状态
   UnifiedOrder findOneByOrderNo(String orderNo);

   //查看订单详情
   UnifiedOrder findOneById(Long id);

   List<UnifiedOrder> findAll();

}