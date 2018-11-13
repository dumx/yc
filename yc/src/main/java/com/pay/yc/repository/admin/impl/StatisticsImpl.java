package com.pay.yc.repository.admin.impl;

import com.pay.yc.repository.admin.StatisticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StatisticsImpl implements StatisticsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getChartList(Long userId) {
        if (userId != null) {
            String sql = "select DATE_FORMAT(create_time,'%Y-%m-%d') as date," +
                    "count(*) orderCount " +
                    " FROM unified_order " +
                    " where status = 'SUCCESS'" +
                    " group by DATE_FORMAT(create_time,'%Y-%m-%d')";
            List<Map<String, Object>> pay = jdbcTemplate.queryForList(sql, userId);
            return pay;
        } else {
            String sql = "select DATE_FORMAT(create_time,'%Y-%m-%d') as date," +
                    "count(*) orderCount " +
                    " FROM unified_order " +
                    " where status = 'SUCCESS'" +
                    " group by DATE_FORMAT(create_time,'%Y-%m-%d')";
            List<Map<String, Object>> pay = jdbcTemplate.queryForList(sql);
            return pay;
        }
//        if (userId != null) {
//            String sql = "select DATE_FORMAT(created_date,'%Y-%m-%d') as date," +
//                    "sum(if(type='CHARGE',occ_amount,0)) sumcharge," +
//                    "sum(if(type='PAY' or type='RETURNPAY',occ_amount,0)) sumpay from payment_item" +
//                    " where merchant_id=?" +
//                    " group by DATE_FORMAT(created_date,'%Y-%m-%d')";
//            List<Map<String, Object>> pay = jdbcTemplate.queryForList(sql, userId);
//            return pay;
//        } else {
//            String sql = "select DATE_FORMAT(created_date,'%Y-%m-%d') as date," +
//                    "sum(if(type='CHARGE',occ_amount,0)) sumcharge," +
//                    "sum(if(type='PAY' or type='RETURNPAY',occ_amount,0)) sumpay from payment_item" +
//                    " group by DATE_FORMAT(created_date,'%Y-%m-%d')";
//            List<Map<String, Object>> pay = jdbcTemplate.queryForList(sql);
//            return pay;
//        }
    }
}
