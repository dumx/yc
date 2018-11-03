package com.pay.yc.repository.order;

import com.pay.yc.model.order.UnifiedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @TODO
* @author dumingxin
* @Date 2017/11/21 10:36 
*/
public interface OrderRepositoryExecutor extends JpaRepository<UnifiedOrder, Long>,JpaSpecificationExecutor<UnifiedOrder> {

}
