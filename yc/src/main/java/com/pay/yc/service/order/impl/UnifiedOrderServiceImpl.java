package com.pay.yc.service.order.impl;

import com.pay.yc.constants.Constants;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import com.pay.yc.service.order.UnifiedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dumingxin
 * @TODO
 * @Date 2017/11/21 10:36
 */
@Service
@Transactional
public class UnifiedOrderServiceImpl implements UnifiedOrderService {
    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    //更新订单是否完成状态
    public UnifiedOrder updateStatus(String orderNo, Long userId) {
        UnifiedOrder model = unifiedOrderRepository.findOneByOrderNo(orderNo);
        if (!model.getUserId().equals(userId)) {
            throw new IllegalArgumentException("操作不属于本人的订单");
        }
        model.setState(Constants.COMPLETED); //已完成
        model.setFinishTime(new Date());
        return unifiedOrderRepository.save(model);
    }

    @Override
    public List<Map<String, Object>> findByStateAndTeachingTime(String state, String teachingTime, Integer startTime, Integer endTime, Integer offset, Integer limit) {
        String sql = "select uo.id,uo.user_id,uo.coach_id,uo.`subject`,uu.registrationid as userRid,cu.registrationid as coachRid from unified_order uo " +
                "left join `user` uu on uu.id=uo.user_id " +
                "left join `user` cu on cu.id=uo.coach_id " +
                "where uo.state=? and uo.teaching_time=? " +
                (startTime == null ? "" : "and uo.start_time=? ") +
                (endTime == null ? "" : "and uo.end_time=? ") +
                "order by uo.create_date desc ";
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[]{state, teachingTime, startTime != null ? startTime : endTime});
        return list;
    }
}
