package com.pay.yc.service.order;

import com.pay.yc.bean.WeixinParamBean;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.model.order.WeixinUnifiedOrder;

/**
 * 
 * @author: dumx    
 * @date:   2017年11月14日 上午9:23:21   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
public interface WeixinOrderService {


    /**
     * 创建订单
     *
     * @return
     */
    WeixinUnifiedOrder create(final WeixinParamBean wxParamBean, final WxPayConfig wxPayConfig);


    /**
     * 退款
     *
     * @return
     */
//    void refundWeixinOrder(final WeixinUnifiedOrder alipay, final WxPayConfig wxPayConfig);


}
