package com.pay.yc.service.order.impl;

import com.hazelcast.util.MD5Util;
import com.pay.yc.bean.WeixinNotifyBean;
import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.util.CustomRuntimeException;
import com.pay.yc.common.util.JsonUtils;
import com.pay.yc.common.util.order.RestHelper;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.constants.Constants;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.model.order.WeixinUnifiedOrder;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import com.pay.yc.repository.order.WeixinUnifiedOrderRepository;
import com.pay.yc.service.order.WeixinNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

/**
 * 微信支付回调
 * @author: dumx    
 * @date:   2017年11月15日 上午10:04:43   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
@Service
@Transactional
public class WeixinNotifyServiceImpl implements WeixinNotifyService {

    private final ParameterizedTypeReference<String> retStrType =
            new ParameterizedTypeReference<String>() {
    };
    @Autowired
    private WeixinUnifiedOrderRepository weixinUnifiedOrderRepository;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public WeixinUnifiedOrder toUpdateNotifyModel(final WeixinNotifyBean weixinNotifyBean) {
        final String orderNo = weixinNotifyBean.getOutTradeNo();
        final WeixinUnifiedOrder model = this.weixinUnifiedOrderRepository.findOneByOrderNo(orderNo);
        // 获得业务订单
        final UnifiedOrder uifiedOrder =
                this.unifiedOrderRepository.findByOrderNo(weixinNotifyBean.getOutTradeNo());
        if (null == model) {
            throw new CustomRuntimeException("404", String.format("订单不存在", orderNo));
        }
        model.setTradeNo(weixinNotifyBean.getTransactionId());
        model.setStatus("SUCCESS".equals(weixinNotifyBean.getResultCode()) ? PaymentTradeStatus.SUCCESS
                : PaymentTradeStatus.FAIL);
        model.setTimeEnd(new Date());
        model.setRaw(JsonUtils.toJson(weixinNotifyBean));
        //新增微信交易流水表字段
        model.setOrderNo(weixinNotifyBean.getOutTradeNo());
        model.setSign(weixinNotifyBean.getSign());

        //更新系统业务订单表
        uifiedOrder.setStatus(PaymentTradeStatus.SUCCESS);
        uifiedOrder.setType(PaymentOrderType.WEIXIN);
        uifiedOrder.setFinishTime(new Date());
        uifiedOrder.setState(Constants.NOTCOMPLETED);

        final String retMessage = this.setPayStatusToStack(model);
        //更新业务订单表支付信息
        this.unifiedOrderRepository.save(uifiedOrder);
        //更新微信预下单支付信息
        weixinUnifiedOrderRepository.save(model);

        if ("success".equals(retMessage)) {
            model.setTimeEnd(new Date());
        } else {
            //TODO 循环调用通知方法。
        }
        return model;
    }

    public String setPayStatusToStack(final WeixinUnifiedOrder model) {
        String retMessage = "fail";
        // 调用目标服务("" 回调地址)
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://116.62.12.100:8080")
                .queryParam("tradeStatus", model.getStatus())
                .queryParam("stackOrderNo", model.getTradeNo());
        final ResponseEntity<String> entity;
        try {
            entity = this.restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    RestHelper.generateEntity(),
                    this.retStrType);
        } catch (final RestClientException e) {
            e.printStackTrace();
            return "fail";
        }
        retMessage = entity.getBody();
        return retMessage;
    }

    /**
     * 生成返回微信端的sign
     *
     * @param WeixinNotifyBean
     * @return
     */
    @Override
    public String generateReturnSign(final WeixinNotifyBean WeixinNotifyBean, final WxPayConfig wxPayConfig) {
        // 生成签名  顺序已经按照key的大小排序了(必须按ASCII码的顺序排序)。
        final StringBuffer signSrc = new StringBuffer();
        signSrc.append("appid=").append(WeixinNotifyBean.getAppid()).append("&");
        if (WeixinNotifyBean.getAttach() != null) {
            signSrc.append("attach=").append(WeixinNotifyBean.getAttach()).append("&");
        }
        signSrc.append("bank_type=").append(WeixinNotifyBean.getBankType()).append("&");
        signSrc.append("cash_fee=").append(WeixinNotifyBean.getCashFee()).append("&");
        if (WeixinNotifyBean.getCashFeeType() != null) {
            signSrc.append("cash_fee_type=").append(WeixinNotifyBean.getCashFeeType()).append("&");
        }
        if (WeixinNotifyBean.getDeviceInfo() != null) {
            signSrc.append("device_info=").append(WeixinNotifyBean.getDeviceInfo()).append("&");
        }
        if (WeixinNotifyBean.getErrCode() != null) {
            signSrc.append("err_code=").append(WeixinNotifyBean.getErrCode()).append("&");
        }
        if (WeixinNotifyBean.getErrCodeDes() != null) {
            signSrc.append("err_code_des=").append(WeixinNotifyBean.getErrCodeDes()).append("&");
        }
        if (WeixinNotifyBean.getFeeType() != null) {
            signSrc.append("fee_type=").append(WeixinNotifyBean.getFeeType()).append("&");
        }
        if (WeixinNotifyBean.getIsSubscribe() != null) {
            signSrc.append("is_subscribe=").append(WeixinNotifyBean.getIsSubscribe()).append("&");
        }
        signSrc.append("mch_id=").append(WeixinNotifyBean.getMchId()).append("&");
        signSrc.append("nonce_str=").append(WeixinNotifyBean.getNonceStr()).append("&");
        signSrc.append("openid=").append(WeixinNotifyBean.getOpenid()).append("&");
        signSrc.append("out_trade_no=").append(WeixinNotifyBean.getOutTradeNo()).append("&");
        signSrc.append("result_code=").append(WeixinNotifyBean.getResultCode()).append("&");
        signSrc.append("return_code=").append(WeixinNotifyBean.getReturnCode()).append("&");
        if(WeixinNotifyBean.getReturnMsg()!=null){
            signSrc.append("return_msg=").append(WeixinNotifyBean.getReturnMsg()).append("&");
        }
        signSrc.append("time_end=").append(WeixinNotifyBean.getTimeEnd()).append("&");
        signSrc.append("total_fee=").append(WeixinNotifyBean.getTotalFee()).append("&");
        signSrc.append("trade_type=").append(WeixinNotifyBean.getTradeType()).append("&");
        signSrc.append("transaction_id=").append(WeixinNotifyBean.getTransactionId()).append("&");
        //设置密钥
        signSrc.append("key=").append(wxPayConfig.getSecret());
        final String returnSignInfo = signSrc.toString();
        return MD5Util.toMD5String(returnSignInfo).toUpperCase();
    }
}
