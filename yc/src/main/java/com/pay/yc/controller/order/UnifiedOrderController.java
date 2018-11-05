package com.pay.yc.controller.order;

import com.pay.yc.bean.WeixinParamBean;
import com.pay.yc.common.enumpub.PaymentOrderType;
import com.pay.yc.common.enumpub.PaymentTradeStatus;
import com.pay.yc.common.result.dto.PageResultDTO;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.common.util.order.UniformOrderGeneratorUtil;
import com.pay.yc.config.WxPayConfig;
import com.pay.yc.convertor.order.UnifiedOrderConvertor;
import com.pay.yc.dto.order.UnifiedOrderDTO;
import com.pay.yc.dto.order.WeixinUnifiedOrderDTO;
import com.pay.yc.model.order.UnifiedOrder;
import com.pay.yc.model.order.WeixinUnifiedOrder;
import com.pay.yc.repository.order.UnifiedOrderRepository;
import com.pay.yc.service.order.UnifiedOrderService;
import com.pay.yc.service.order.WeixinOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 支付预下单
 *
 * @version V1.0
 * 注意：禁止外泄以及用于其他的商业目的
 * @author: dumx
 * @date: 2017年11月14日 下午3:56:57
 */
@Slf4j
@RestController
@Api(tags = {"支付预订单管理API"})
@RequestMapping(value = "/order/unifiedOrders")
public class UnifiedOrderController {

    @Autowired
    private WeixinOrderService weixinOrderService;


    @Autowired
    private UnifiedOrderConvertor unifiedOrderConvertor;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private UnifiedOrderService unifiedOrderService;




    @Value(value = "${pay.weixin.appid}")
    private String appid;
    @Value(value = "${pay.weixin.mchid}")
    private String mchid;

    //API商户秘钥在商户平台设置
    @Value(value = "${pay.weixin.secret}")
    private String secret;


    @ApiOperation(value = "微信预支付请求，创建微信订单。")
    @RequestMapping(value = "/weixin", method = RequestMethod.POST)
    public ResultDTO<WeixinUnifiedOrderDTO> generateWeixinUnifiedOrder(@RequestBody final WeixinParamBean wxParamBean) {
        //获取appid/商户号等信息
        final WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(appid);
        wxPayConfig.setMchId(mchid);
        wxPayConfig.setSecret(secret);

        // 创建业务订单
        UnifiedOrder unifiedOrder=new UnifiedOrder();
        String orderNo=UniformOrderGeneratorUtil.getOrderNum()+UniformOrderGeneratorUtil.getRandomNumStringByLength(5);
        //需要使用统一订单生成器
        unifiedOrder.setOrderNo(orderNo);
        //金额
        unifiedOrder.setTotalFee(wxParamBean.getTotalFee());
        //标题
        unifiedOrder.setSubject(wxParamBean.getSubject());
        //微信公众号内支付需传openId
        unifiedOrder.setOpenId(wxParamBean.getOpenId());

        //签名信息在Service中添加。
        unifiedOrder.setType(PaymentOrderType.WEIXIN);
        unifiedOrder.setStatus(PaymentTradeStatus.WATING);//创建订单默认状态为waiting
        unifiedOrder.setCreateTime(new Date());
        this.unifiedOrderRepository.save(unifiedOrder);

        wxParamBean.setOrderNo(orderNo);
        final WeixinUnifiedOrder model = this.weixinOrderService.create(wxParamBean, wxPayConfig);
        // 生成返回结果
        final WeixinUnifiedOrderDTO dto = new WeixinUnifiedOrderDTO();
        dto.setId(model.getId());
        dto.setAppid(model.getAppId());
        dto.setPartnerid(wxPayConfig.getMchId());
        dto.setPrepayid(model.getPrepayId());
        dto.setOrderNo(model.getOrderNo());
//        dto.setWxPackage("Sign=WXPay");
        dto.setNonceStr(model.getNonceStr());
        dto.setTimestamp(model.getTimestamp());
        dto.setSign(model.getSign());


        log.info("请求微信签名成功!签名:{}", dto.getSign());
        return ResultDTO.success(dto);
    }







    /**
     * 获取已完成和未完成订单(在已支付的前提下)
     *
     * @param userId
     * @param pageable
     * @return
     */
    @ApiOperation(value = "获取已完成和未完成订单(已支付)", notes = "获取已完成和未完成订单(已支付)", httpMethod = "GET")
    @RequestMapping(value = "/getIsCompleteOrder", method = RequestMethod.GET)
    public PageResultDTO<UnifiedOrderDTO> getIsCompleteOrder(@RequestParam Long userId, @RequestParam String state, Pageable pageable) {
        Page<UnifiedOrder> model = this.unifiedOrderRepository.findByUserIdAndStateAndStatusOrderByCreateTimeDesc(userId, state, PaymentTradeStatus.SUCCESS, pageable);
        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
        log.info("获取订单,用户:{}", userId);
        return result;
    }

    /**
     * 获取未支付订单
     *
     * @param userId
     * @param pageable
     * @return
     */
    @ApiOperation(value = "获取待支付订单", notes = "获取待支付订单", httpMethod = "GET")
    @RequestMapping(value = "/getUnPayOrderList", method = RequestMethod.GET)
    public PageResultDTO<UnifiedOrderDTO> getOrderList(@RequestParam Long userId, Pageable pageable) {
        Page<UnifiedOrder> model = this.unifiedOrderRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, PaymentTradeStatus.WATING, pageable);
        PageResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
        log.info("获取订单,用户:{}", userId);
        return result;
    }


    /**
     * 查看订单详情
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "查看订单详情", notes = "查看订单详情", httpMethod = "GET")
    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET)
    public ResultDTO<UnifiedOrderDTO> getOrderDetail(@RequestParam Long orderId) {
        UnifiedOrder model = this.unifiedOrderRepository.findOneById(orderId);
        ResultDTO<UnifiedOrderDTO> result = this.unifiedOrderConvertor.toResultDTO(model);
        log.info("查看订单详情,订单编号:{}", orderId);
        return result;
    }

    /**
     * 更新状态(是否完成)
     *
     * @param orderId 订单编号
     * @param uid     用户ID
     * @return
     */
    @ApiOperation(value = "更新订单状态(是否完成)", notes = "更新订单状态(是否成)", httpMethod = "GET")
    @RequestMapping(value = "/updateState", method = RequestMethod.GET)
    public ResultDTO<?> updateState(@RequestParam final String orderId, @RequestParam Long uid) {
        this.unifiedOrderService.updateStatus(orderId, uid);
        log.info("更新订单状态，用户id:{},订单id{}", uid, orderId);
        return ResultDTO.success();
    }

}
