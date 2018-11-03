package com.pay.yc.service.order;

import com.pay.yc.model.order.UnifiedOrder;

import java.util.List;
import java.util.Map;

/**
* 订单管理
* @author dumingxin
* @Date 2017/11/21 10:33 
*/
public interface UnifiedOrderService {


    /**
     * 更新状态(是否完成)
     * @param orderNo
     * @param userId
     * @return
     */
    UnifiedOrder updateStatus(String orderNo, Long userId);


    /**
     * 根据状态和开始时间获取订单列表
     * @param state
     * @param teachingTime
     * @param startTime
     * @param endTime
     * @param offset
     * @param limit
     * @return
     */
    List<Map<String,Object>> findByStateAndTeachingTime(String state, String teachingTime, Integer startTime, Integer endTime,Integer offset, Integer limit);
}
