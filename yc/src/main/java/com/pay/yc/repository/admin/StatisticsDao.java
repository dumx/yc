package com.pay.yc.repository.admin;

import java.util.List;
import java.util.Map;

public interface StatisticsDao {

    /**
     * 根据用户获取首页图表信息
     * @param userId
     * @return
     */
    List<Map<String,Object>> getChartList(Long userId);
}
