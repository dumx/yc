package com.pay.yc.service.order;

import com.pay.yc.bean.WeixinNotifyBean;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.model.order.WeixinUnifiedOrder;

public interface WeixinNotifyService {

    WeixinUnifiedOrder toUpdateNotifyModel(final WeixinNotifyBean weixinNotifyBean);

    /**
     * 生成返回微信端的sign
     */
    String generateReturnSign(final WeixinNotifyBean weixinNotifyBean, final WxPayConfig wxPayConfig);
}
