package com.pay.yc.convertor;

import com.pay.yc.common.result.AbstractConvertor;
import com.pay.yc.dto.order.PaymentItemDTO;
import com.pay.yc.model.admin.PaymentItem;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class PaymentConvertor extends AbstractConvertor<PaymentItem, PaymentItemDTO> {



    public PaymentItem toModel(PaymentItemDTO dto, PaymentItem model) {
        if(model == null) {
            model = new PaymentItem();
        }
        model.setOrderNo(dto.getOrderNo());
        model.setAmount(dto.getAmount());
        model.setOccAmount(dto.getOccAmount());
        model.setBalance(dto.getBalance());
        model.setType(dto.getType());
        model.setState(dto.getState());
        model.setCreatedDate(new Date());
        return model;
    }
    public PaymentItemDTO toDTO(PaymentItem model, boolean arg1) {
        if(model == null) {
            return null;
        }
        PaymentItemDTO dto = new PaymentItemDTO();
        dto.setOrderNo(dto.getOrderNo());
        dto.setAmount(model.getAmount());
        dto.setOccAmount(model.getOccAmount());
        dto.setBalance(model.getBalance());
        dto.setType(model.getType());
        dto.setState(model.getState());
        dto.setCreateTime(model.getCreatedDate());
        return dto;
    }

}
