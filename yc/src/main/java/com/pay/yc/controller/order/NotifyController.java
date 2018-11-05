package com.pay.yc.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.pay.yc.bean.WeixinNotifyBean;
import com.pay.yc.bean.WeixinNotifyResponse;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.util.JsonUtils;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.constants.PaymentConstant;
import com.pay.yc.model.order.WeixinUnifiedOrder;
import com.pay.yc.repository.order.WeixinUnifiedOrderRepository;
import com.pay.yc.service.order.WeixinNotifyService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Api(tags = {"微信、支付宝等支付平台支付通知API"})
@RequestMapping(value = "/w/notifies", method = RequestMethod.POST)
public class NotifyController {


    @Autowired
    private WeixinNotifyService weixinNotifyService;

    @Autowired
    private WeixinUnifiedOrderRepository wexinUnifiedOrderRepository;

    @Value(value = "${pay.weixin.appid}")
    private String appid;
    @Value(value = "${pay.weixin.mchid}")
    private String mchid;
    @Value(value = "${pay.weixin.secret}")
    private String secret;

    @ApiOperation(value = "微信异步通知")
    @RequestMapping(value = "/weixin", produces = "application/xml")
    @ResponseBody
    public String weixinNodify(@RequestBody final WeixinNotifyBean weixinNotifyBean) {
        log.info("收到微信支付回调========================================"+weixinNotifyBean.getResultCode());
        if (NotifyController.log.isInfoEnabled()) {
            NotifyController.log.info("weixin nodify is arrival. out_trade_no is {} ", weixinNotifyBean.getOutTradeNo());
        }
        final XStream xStreamResult = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        final WeixinNotifyResponse returnBean = new WeixinNotifyResponse();

        // 获取预支付订单
        WeixinUnifiedOrder order = wexinUnifiedOrderRepository.findOneByOrderNo(weixinNotifyBean.getOutTradeNo());
        if (order == null) {
            returnBean.setReturn_code("FAIL");
            returnBean.setReturn_msg("支付失败");
            return xStreamResult.toXML(returnBean);
        }
        if (order.getStatus() == PaymentTradeStatus.SUCCESS) {
            log.error("已支付成功的微信支付订单，再次回调!!!");
            return "OK";
        }
        // 获取微信appid等配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(appid);
        wxPayConfig.setMchId(mchid);
        wxPayConfig.setSecret(secret);


        xStreamResult.processAnnotations(WeixinNotifyResponse.class);
        //返回消息中是否支付失败。
        if (weixinNotifyBean.getErrCode() != null) {
            NotifyController.log.info("支付失败.", NotifyController.class.getSimpleName(), weixinNotifyBean.getErrCode());
            returnBean.setReturn_code("FAIL");
            returnBean.setReturn_msg("支付失败");
            return xStreamResult.toXML(returnBean);
        }
        log.info("微信回调参数:----------------------------"+weixinNotifyBean.toString());
        log.info("微信回调参数:----------------------------"+ JsonUtils.toJson(weixinNotifyBean));
        //验证签名
        final String returnSign = this.weixinNotifyService.generateReturnSign(weixinNotifyBean, wxPayConfig);
        if (!returnSign.equals(weixinNotifyBean.getSign())) {
            log.info("微信返回的签名----------------------:"+weixinNotifyBean.getSign());
            log.info("系统根据支付成功返回信息生成的签名----------------------:"+ returnSign);
            NotifyController.log.info("支付返回信息生成的签名与微信返回签名不符.", NotifyController.class.getSimpleName(),
                    "支付返回信息生成的签名与微信返回签名不符");
            returnBean.setReturn_code("FAIL");
            returnBean.setReturn_msg("签名失败");
            return xStreamResult.toXML(returnBean);
        }

        this.weixinNotifyService.toUpdateNotifyModel(weixinNotifyBean);
        returnBean.setReturn_code("SUCCESS");
        returnBean.setReturn_msg("OK");
        log.error(xStreamResult.toXML(returnBean));
        return xStreamResult.toXML(returnBean);
    }


    @ApiOperation(value = "测试 支付宝异步通知")
    @RequestMapping(value = "/alipay_test")
    public Object alipayConback_test(@RequestBody String alipayNotifyBean) {
        log.info("测试 支付宝回调通知!" + alipayNotifyBean);
        return "success";
    }

    private boolean isPaySuccess(String status) {
        return (PaymentConstant.TRADE_FINISHED.equals(status) || PaymentConstant.TRADE_SUCCESS.equals(status));
    }
}
