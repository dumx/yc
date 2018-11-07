package com.pay.yc.convertor.order;

import com.pay.yc.common.result.AbstractConvertor;
import com.pay.yc.constants.Constants;
import com.pay.yc.dto.order.UnifiedOrderDTO;
import com.pay.yc.model.order.UnifiedOrder;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;

/**
* 统一下单
* @author dumingxin
* @Date 2017/11/17 13:50
*/
@Component
public class UnifiedOrderConvertor extends AbstractConvertor<UnifiedOrder, UnifiedOrderDTO> {


    public UnifiedOrder toModel(UnifiedOrderDTO dto, UnifiedOrder model) {
        if(model == null) {
            model = new UnifiedOrder();
        }
        model.setStatus(dto.getStatus());
        model.setState(dto.getState());
        model.setTotalFee(dto.getTotalFee());
        model.setSubject(dto.getSubject());
        model.setCreateTime(dto.getCreateTime());
        model.setFinishTime(dto.getFinishTime());
        //订单号生成规则(19位)
//        model.setOrderNo(UniformOrderGeneratorUtil.getOrderNum()+UniformOrderGeneratorUtil.getcurrTime()+UniformOrderGeneratorUtil.getRandomNumStringByLength(5));
        //订单号生成规则(25位,年月日时分秒+8位用户ID(1802051731364632700000010)+5位随机数)
//        model.setOrderNo(UniformOrderGeneratorUtil.getOrderNum()+haoAddOne(dto.getUserId().toString())+UniformOrderGeneratorUtil.getRandomNumStringByLength(5));
        model.setOrderNo(dto.getOrderNo());
        //支付平台
        model.setPlatform(dto.getPlatform());
        //订单创建时间
        model.setCreateTime(new Date());
        return model;
    }
    public UnifiedOrderDTO toDTO(UnifiedOrder model, boolean arg1) {
        if(model == null) {
            return null;
        }
        UnifiedOrderDTO dto = UnifiedOrderDTO.build();

        dto.setId(model.getId());
        dto.setTotalFee(model.getTotalFee());
        dto.setSubject(model.getSubject());
        dto.setCreateTime(model.getCreateTime());
        dto.setFinishTime(model.getFinishTime());
        dto.setOrderNo(model.getOrderNo());
        dto.setUserId(model.getUserId());
        dto.setState(model.getState());
        dto.setStatus(model.getStatus());
        return dto;
    }

    /**
     * 固定千万级用户量
     */
    public static String haoAddOne(String userId){
        Integer nineNo = Integer.parseInt(userId);
        nineNo++;
        DecimalFormat df = new DecimalFormat(Constants.STR_FORMAT);//千万级用户量
        return df.format(nineNo);
    }
}
