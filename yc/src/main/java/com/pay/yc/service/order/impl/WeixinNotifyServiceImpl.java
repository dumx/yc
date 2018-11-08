package com.pay.yc.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hazelcast.util.MD5Util;
import com.pay.yc.bean.WeixinNotifyBean;
import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.util.CustomRuntimeException;
import com.pay.yc.common.util.JsonUtils;
import com.pay.yc.common.util.order.RestHelper;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.constants.Constants;
import com.pay.yc.model.admin.User;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.model.order.WeixinUnifiedOrder;
import com.pay.yc.repository.admin.UserRepository;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import com.pay.yc.repository.order.WeixinUnifiedOrderRepository;
import com.pay.yc.service.order.WeixinNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 微信支付回调
 * @author: dumx    
 * @date:   2017年11月15日 上午10:04:43   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
@Slf4j
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

    @Autowired
    private UserRepository userRepository;

    @Value(value = "${pay.weixin.key}")
    private String key;

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
        uifiedOrder.setState(Constants.COMPLETED);

        final String retMessage = this.setPayStatusToStack(model);
        //更新业务订单表支付信息
        UnifiedOrder u=this.unifiedOrderRepository.save(uifiedOrder);
        //更新微信预下单支付信息
        weixinUnifiedOrderRepository.save(model);


        //更新门禁系统时间
        this.updateDoorInfo(u);

        if ("success".equals(retMessage)) {
            model.setTimeEnd(new Date());
        } else {
            //TODO 循环调用通知方法。
        }
        return model;
    }

    /**
     * 更新门禁时间
     * @returnu
     */
    public String updateDoorInfo(UnifiedOrder u){
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
        HashMap map = new HashMap();
        map.put("apiid", "bl14fd434cbe055eac");
        map.put("apikey", "e44ee2af7f9aaa857aaa501e65e9dcb6");
        String tokenStr = httpUrlConnection("https://api.parkline.cc/api/token", map);
        JSONObject tokenMap = new Gson().fromJson(tokenStr, JSONObject.class);
        String token = tokenMap.getString("access_token");
        log.info("更新门禁系统信息获取token:-----------------"+token);
        HashMap<String, String> bind = new HashMap<>();
        bind.put("token", token);
        bind.put("typeid", "203");
        User user=this.userRepository.findByOpenId(u.getOpenId());
        bind.put("tel", user.getMobile());
        bind.put("devid", "215093");
        bind.put("lockid", "01");
        bind.put("startdate", formatDay.format(u.getBeginTime()));
        log.info("更新门禁系统信息开始时间:-----------------"+formatDay.format(u.getBeginTime()));
        bind.put("enddate", formatDay.format(u.getEndTime()));
        log.info("更新门禁系统信息结束时间:-----------------"+formatDay.format(u.getEndTime()));
        bind.put("starttime", formatHour.format(u.getBeginTime()));
        log.info("更新门禁系统信息开始小时:-----------------"+formatHour.format(u.getBeginTime()));
        bind.put("endtime", formatHour.format(u.getEndTime()));

        log.info("更新门禁系统信息结束小时:-----------------"+formatHour.format(u.getEndTime()));
        String bindResult = httpUrlConnection("https://api.parkline.cc/api/facecgi", bind);
        log.info("更新门禁系统信息:-----------------"+bindResult);
        Map m=new HashMap();
        m.put("bindResult",bindResult);
        m.put("mobile",user.getMobile());
        m.put("binded",user.getBinded());
        m.put("bindToken",token);
        return bindResult;
    }

    private static String httpUrlConnection(String pathurl, HashMap<String, String> hm) {

        String result = null;
        try {
            URL url = new URL(pathurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("referer", "http://u66whn.natappfree.cc");//后台填写的授权接入地址，必须包含http或https协议
            conn.setRequestMethod("POST");
            PrintWriter pw = new PrintWriter(conn.getOutputStream());
            pw.print(getParams(hm));
            pw.flush();
            pw.close();
            if (conn.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(conn
                        .getInputStream(), "utf-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                responseReader.close();
                result = sb.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getParams(Map<String, String> paramValues) {
        String params = "";
        String beginLetter = "";
        Set<String> key = paramValues.keySet();
        try {
            for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
                String s = (String) it.next();
                if (params.equals("")) {
                    params += beginLetter + s + "="
                            + URLEncoder.encode(paramValues.get(s), "UTF-8");
                } else {
                    params += "&" + s + "="
                            + URLEncoder.encode(paramValues.get(s), "UTF-8");
                }
            }
        } catch (Exception e) {

        }


        return params;
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

        signSrc.append("bank_type=").append(WeixinNotifyBean.getBankType()).append("&");
        signSrc.append("cash_fee=").append(WeixinNotifyBean.getCashFee()).append("&");
        signSrc.append("fee_type=").append(WeixinNotifyBean.getFeeType()).append("&");
        if (WeixinNotifyBean.getIsSubscribe() != null) {
            signSrc.append("is_subscribe=").append(WeixinNotifyBean.getIsSubscribe()).append("&");
        }
        signSrc.append("mch_id=").append(WeixinNotifyBean.getMchId()).append("&");
        signSrc.append("nonce_str=").append(WeixinNotifyBean.getNonceStr()).append("&");
        signSrc.append("openid=").append(WeixinNotifyBean.getOpenid()).append("&");
        signSrc.append("out_trade_no=").append(WeixinNotifyBean.getOutTradeNo()).append("&");
        signSrc.append("result_code=").append(WeixinNotifyBean.getResultCode()).append("&");
        signSrc.append("return_code=").append(WeixinNotifyBean.getReturnCode()).append("&");
//        if(WeixinNotifyBean.getReturnMsg()!=null){
//            signSrc.append("return_msg=").append(WeixinNotifyBean.getReturnMsg()).append("&");
//        }
        signSrc.append("time_end=").append(WeixinNotifyBean.getTimeEnd()).append("&");
        signSrc.append("total_fee=").append(WeixinNotifyBean.getTotalFee()).append("&");
        signSrc.append("trade_type=").append(WeixinNotifyBean.getTradeType()).append("&");
        signSrc.append("transaction_id=").append(WeixinNotifyBean.getTransactionId()).append("&");
        //设置密钥
//        signSrc.append("key=").append(wxPayConfig.getSecret());
        signSrc.append("key=").append(key);
        final String returnSignInfo = signSrc.toString();
        return MD5Util.toMD5String(returnSignInfo).toUpperCase();
    }
}
