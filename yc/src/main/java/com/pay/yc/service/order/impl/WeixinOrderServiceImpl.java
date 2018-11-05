package com.pay.yc.service.order.impl;

import com.hazelcast.util.MD5Util;
import com.pay.yc.bean.WeixinCreateOrderBean;
import com.pay.yc.bean.WeixinParamBean;
import com.pay.yc.bean.WxPayPrePayBean;
import com.pay.yc.bean.WxPayUnifiedOrderBean;
import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.util.CustomRuntimeException;
import com.pay.yc.common.util.JsonUtils;
import com.pay.yc.common.util.order.UniformOrderGeneratorUtil;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.constants.Constants;
import com.pay.yc.constants.PaymentConstant;
import com.pay.yc.model.order.WeixinUnifiedOrder;
import com.pay.yc.paysign.MD5;
import com.pay.yc.repository.order.WeixinUnifiedOrderRepository;
import com.pay.yc.service.order.WeixinOrderService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
* 微信支付统一下单实现方法
* @author dumingxin
* @Date 2017/11/15 16:21
*/
@Slf4j
@Service
@Transactional
public class WeixinOrderServiceImpl implements WeixinOrderService {

    @Autowired
    private WeixinUnifiedOrderRepository weixinUnifiedOrderRepository;


    @Value(value = "${pay.weixin.notifyurl}")
    private String notifyurl;

    @Value(value = "${pay.weixin.key}")
    private String key;

    @Value(value = "${pay.weixin.tradeType}")
    private String tradeType;

    /**
     * 微信预下单
     *
     * @param wxParamBean
     * @return
     */
    @Override
    public WeixinUnifiedOrder create(
            final WeixinParamBean wxParamBean,
            final WxPayConfig wxPayConfig) {

        final WeixinUnifiedOrder order = new WeixinUnifiedOrder();
        order.setAppId(wxPayConfig.getAppId());
        //需要使用统一订单生成器
        order.setOrderNo(wxParamBean.getOrderNo());
        //金额
        order.setTotalFee(wxParamBean.getTotalFee());
        //标题
        order.setSubject(wxParamBean.getSubject());
        //微信公众号内支付需传openId
        order.setOpenId(wxParamBean.getOpenId());
        order.setNonceStr(UniformOrderGeneratorUtil.getRandomStringByLength(32));
        order.setSpbill_create_ip(wxParamBean.getSpbillCreateIp());

        //签名信息在Service中添加。
        order.setType(PaymentOrderType.WEIXIN);
        order.setStatus(PaymentTradeStatus.WATING);//创建订单默认状态为waiting

        // 调用微信服务器获取预支付信息
        final WxPayPrePayBean prePayBean = this.fetchPrePayBean(order, wxPayConfig);
        if (prePayBean == null) {
            throw new CustomRuntimeException("prePayId.fetch.fail", "从微信支付服务器获取PrePayId失败");
        }
        // 设置APP用签名相关信息
        order.setPrepayId(prePayBean.getPrepay_id());
        order.setNonceStr(prePayBean.getNonce_str());
        order.setTimestamp(String.valueOf(new Date().getTime() / 1000));
        final String sign = this.generateSignForApp(
                order.getTimestamp(),
                prePayBean);
        order.setSign(sign);

        //如果该订单号交易流水已存在,则不新建交易流水
        WeixinUnifiedOrder weixinUnifiedOrder=this.weixinUnifiedOrderRepository.findOneByOrderNo(wxParamBean.getOrderNo());
        if(weixinUnifiedOrder==null){
            return this.weixinUnifiedOrderRepository.save(order);
        }else{
            weixinUnifiedOrder.setSign(sign);
            weixinUnifiedOrder.setPrepayId(prePayBean.getPrepay_id());
            weixinUnifiedOrder.setNonceStr(UniformOrderGeneratorUtil.getRandomStringByLength(32));
            weixinUnifiedOrder.setTimestamp(String.valueOf(new Date().getTime() / 1000));
            return this.weixinUnifiedOrderRepository.save(weixinUnifiedOrder);

        }
    }

    /**
     * 退款
     *
     * @param alipay
     * @return
     */
//    @Override
//    public void refundWeixinOrder(final WeixinUnifiedOrder alipay, final WxPayConfig wxPayConfig) {
//
//        // 生成退款请求XML
//        final String refundXml = this.generateRefundXml(alipay, wxPayConfig);
//        // 向微信服务器发送退款请求
//        String responseXML;
//        try {
//            responseXML = ClientCustomSSL.doRefund(
//                    PaymentConstant.WX_REFUND_ORDER_URL,
//                    this.paymentProperties.getWeixin().getLocal_cert_url(),
//                    refundXml,
//                    wxPayConfig.getMchId());
//        } catch (final Exception e) {
//            e.printStackTrace();
//            throw new CustomRuntimeException("refund.fail", "调用退款接口失败");
//        }
//        if (!this.isRequestSuccess(responseXML)) {
//            throw new CustomRuntimeException("refund.fail", "调用退款接口失败");
//        }
//
//        alipay.setStatus(PaymentTradeStatus.REFUND_SUCCESS);
//        this.weixinUnifiedOrderRepository.save(alipay);
//    }

    /**
     * 固定千万级用户量 生成订单随机数
     */
    public static String haoAddOne(String userId){
        Integer nineNo = Integer.parseInt(userId);
        nineNo++;
        DecimalFormat df = new DecimalFormat(Constants.STR_FORMAT);//千万级用户量
        return df.format(nineNo);
    }

    /**
     * 返回给APP端的sign
     *
     * @param timeStamp
     * @param resultBean
     * @return
     */
    private String generateSignForApp(final String timeStamp, final WxPayPrePayBean resultBean) {
        final StringBuffer signSrc = new StringBuffer();
        signSrc.append("appId=").append(resultBean.getAppid()).append("&");
        signSrc.append("nonceStr=").append(resultBean.getNonce_str()).append("&");
        signSrc.append("package=").append("prepay_id="+resultBean.getPrepay_id()).append("&");
        signSrc.append("signType=").append("MD5").append("&");
        signSrc.append("timeStamp=").append(timeStamp).append("&");
        signSrc.append("key=").append("dVrC7OGC7hKzr1zyIcfWdy0fCKES2eiO");
       
        return  MD5Util.toMD5String(signSrc.toString()).toUpperCase();
    } 

    /**
     * 通过微信服务器获取prePayBean
     *
     * @return
     */
    private WxPayPrePayBean fetchPrePayBean(final WeixinUnifiedOrder order, final WxPayConfig wxPayConfig) {
        // 构建下单bean
        final WxPayUnifiedOrderBean unifiedOrderBean = new WxPayUnifiedOrderBean();
        unifiedOrderBean.setAppid(wxPayConfig.getAppId());
        unifiedOrderBean.setMch_id(wxPayConfig.getMchId());
        unifiedOrderBean.setNonce_str(this.getNonceStr(16));
        unifiedOrderBean.setBody(order.getSubject());
        unifiedOrderBean.setOut_trade_no(order.getOrderNo());
        // TODO: 设置真实价格
        unifiedOrderBean.setTotal_fee(order.getTotalFee().intValue());   //真实价格:alipay.getTotalFee()
        unifiedOrderBean.setSpbill_create_ip(order.getSpbill_create_ip());
        unifiedOrderBean.setNotify_url(notifyurl);//回调地址
        unifiedOrderBean.setOpenid(order.getOpenId());
        unifiedOrderBean.setSign(this.generateSignForPrePayId(unifiedOrderBean, wxPayConfig.getSecret()));
        unifiedOrderBean.setTrade_type(tradeType);
        // 请求统一下单接口
        final WxPayPrePayBean bean = this.requestWx(unifiedOrderBean);

        if ("SUCCESS".equals(bean.getResult_code())) {
            return bean;
        } else {
            WeixinOrderServiceImpl.log.error(this.changeCode(bean.getReturn_msg()));
            WeixinOrderServiceImpl.log.error(this.changeCode(JsonUtils.toJson(bean)));
            return null;
        }
    }

    private String changeCode(final String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (final Exception e) {
            return "error";
        }
    }

    /**
     * 请求统一下单接口
     *
     * @param unifiedOrderBean
     * @return
     */
    private WxPayPrePayBean requestWx(final WxPayUnifiedOrderBean unifiedOrderBean) {

        final XStream xStream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        xStream.autodetectAnnotations(true);
        final String postXml = xStream.toXML(unifiedOrderBean);

        // 发送请求
        final HttpHeaders headers = new HttpHeaders();
        final MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);
        final HttpEntity<String> requestEntity = new HttpEntity<>(postXml, headers);

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                PaymentConstant.WX_PREPARE_ORDER_URL, requestEntity, String.class);

        // 解析请求结果
        final XStream xStreamResult = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        xStreamResult.processAnnotations(WxPayPrePayBean.class);
        try {
            final String returnStr = new String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8");

            System.out.println(responseEntity.getBody());
            System.out.println(returnStr);
            final WxPayPrePayBean bean = (WxPayPrePayBean) xStreamResult.fromXML(returnStr);
            return bean;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 生成签名
     *
     * @param unifiedOrderBean
     * @return
     */
    private String generateSignForPrePayId(final WxPayUnifiedOrderBean unifiedOrderBean, final String secret) {

        // 生成签名  顺序已经按照key的大小排序了(必须按ASCII码的顺序排序)。
        final StringBuffer signSrc = new StringBuffer();
        signSrc.append("appid=").append(unifiedOrderBean.getAppid()).append("&");
        signSrc.append("body=").append(unifiedOrderBean.getBody()).append("&");
        signSrc.append("mch_id=").append(unifiedOrderBean.getMch_id()).append("&");
        signSrc.append("nonce_str=").append(unifiedOrderBean.getNonce_str()).append("&");
        signSrc.append("notify_url=").append(unifiedOrderBean.getNotify_url()).append("&");
        signSrc.append("openid=").append(unifiedOrderBean.getOpenid()).append("&");
        signSrc.append("out_trade_no=").append(unifiedOrderBean.getOut_trade_no()).append("&");
        signSrc.append("spbill_create_ip=").append(unifiedOrderBean.getSpbill_create_ip()).append("&");
        signSrc.append("total_fee=").append(unifiedOrderBean.getTotal_fee()).append("&");
        signSrc.append("trade_type=").append(unifiedOrderBean.getTrade_type()).append("&");
        //设置密钥
//        signSrc.append("key=").append(secret);
        signSrc.append("key=").append("dVrC7OGC7hKzr1zyIcfWdy0fCKES2eiO");
        final String signInfo = signSrc.toString();

        return MD5Util.toMD5String(signInfo).toUpperCase();
    }

    /**
     * 产生随机字符串
     *
     * @param length
     * @return 产生的随机字符串
     */
    private String getNonceStr(final int length) {
        final Random rand = new Random();
        int randIndex = 0;
        final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        String str = "";
        for (int i = 0; i < length; i++) {
            randIndex = rand.nextInt(chars.length() - 1);
            str = str + chars.substring(randIndex, randIndex + 1);
        }
        return str;
    }

    /**
     * 判断返回的XML是否是成功状态
     *
     * @param responseXML
     * @return
     */
    private boolean isRequestSuccess(final String responseXML) {
        if (StringUtils.isEmpty(responseXML)) {
            return false;
        }

        final Map<String, Object> responseMap = this.changeXmlToMap(responseXML);
        final String result_code = (String) responseMap.get("result_code");
        if (!StringUtils.isEmpty(result_code) && result_code.endsWith("SUCCESS")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 针对微信规定的预付单内容进行签名。
     *
     * @param order
     * @return
     * @throws IllegalAccessException
     */
    private String getWeixinSign(final WeixinCreateOrderBean order, final WxPayConfig wxPayConfig) {
        try {
            final ArrayList<String> list = new ArrayList<>();
            final Class cls = order.getClass();
            final Field[] fields = cls.getDeclaredFields();
            for (final Field f : fields) {
                f.setAccessible(true);
                if ((f.get(order) != null) && (f.get(order) != "")) {
                    list.add(f.getName() + "=" + f.get(order) + "&");
                }
            }
            final int size = list.size();
            final String[] arrayToSort = list.toArray(new String[size]);
            Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(arrayToSort[i]);
            }
            String result = sb.toString();
            result += "key=" + wxPayConfig.getSecret();
            result = MD5.MD5Encode(result).toUpperCase();
            return result;
        } catch (final IllegalAccessException ie) {
            ie.printStackTrace();
            throw new CustomRuntimeException("sing.generate.fail", "生成签名失败");
        }
    }

    /**
     * 转换XML->Map
     *
     * @param xmlString
     * @return
     */
    public Map<String, Object> changeXmlToMap(final String xmlString) {
        Document document;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            document = builder.parse(is);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new CustomRuntimeException("xml.to.map.fail", "微信转换XML失败");
        }
        final NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        final Map<String, Object> map = new HashMap<String, Object>();
        int i = 0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if (node instanceof Element) {
                map.put(node.getNodeName(), node.getTextContent());
            }
            i++;
        }
        return map;
    }

    /**
     * 生成退款请求XML
     *
     * @param alipay
     * @return
     */
//    private String generateRefundXml(final WeixinUnifiedOrder alipay, final WxPayConfig wxPayConfig) {
//        try {
//            final WeixinCreateOrderBean wxBeanXML = new WeixinCreateOrderBean();
//            wxBeanXML.setAppid(alipay.getAppId());
//            wxBeanXML.setMch_id(wxPayConfig.getMchId());
//            wxBeanXML.setNonce_str(alipay.getNonceStr());
//            wxBeanXML.setOut_trade_no(alipay.getHubOrderNo());
//            wxBeanXML.setOut_refund_no(alipay.getHubOrderNo());
//            wxBeanXML.setTotal_fee(alipay.getTotalFee());
//            wxBeanXML.setRefund_fee(alipay.getTotalFee());
//            wxBeanXML.setOp_user_id(wxPayConfig.getMchId());
//            // 签名针对传入参数签名
//            wxBeanXML.setSign(this.getWeixinSign(wxBeanXML, wxPayConfig));
//
//            JAXBContext context;
//            try {
//                context = JAXBContext.newInstance(WeixinCreateOrderBean.class);
//            } catch (final JAXBException je) {
//                je.printStackTrace();
//                throw new CustomRuntimeException("refund.xml.generate.fail", "生成退款请求XML失败");
//            }
//            final Marshaller mar = context.createMarshaller();
//            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            final StringWriter writer = new StringWriter();
//            mar.marshal(wxBeanXML, writer);
//            return writer.toString();
//        } catch (final Exception e) {
//            e.printStackTrace();
//            throw new CustomRuntimeException("refund.xml.generate.fail", "生成退款请求XML失败");
//        }
//    }

}
