package com.pay.yc.convertor;

import com.pay.yc.common.result.AbstractConvertor;
import com.pay.yc.dto.admin.ConfigDTO;
import com.pay.yc.dto.admin.UserDTO;
import com.pay.yc.model.admin.Config;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.PaymentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConfigConvertor extends AbstractConvertor<Config, ConfigDTO> {

    public Config toModel(ConfigDTO dto, Config config) {
        if(config==null){
            config =new Config();
            config.setCreatedDate(new Date());
            config.setName(dto.getName());
            config.setTotalFee(dto.getTotalFee());
//            config.setDayTime(dto.getDayTime());
//            config.setBeginHour(dto.getBeginHour());
//            config.setEndHour(dto.getEndHour());

            config.setBeginTime(dto.getBeginTime());
            config.setEndTime(dto.getEndTime());
            config.setSort(dto.getSort());
            config.setRemark(dto.getRemark());
            config.setType(dto.getType());
            config.setLink(dto.getLink());
        }else{
            config.setName(dto.getName());
            config.setTotalFee(dto.getTotalFee());
            config.setBeginTime(dto.getBeginTime());
            config.setEndTime(dto.getEndTime());
        }
        return config;
    }

    public ConfigDTO toDTO(Config model, boolean arg1) {
        if (model == null) {
            return null;
        }
        ConfigDTO dto = new ConfigDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCreateTime(model.getCreatedDate());
        dto.setTotalFee(model.getTotalFee());
        dto.setBeginTime(model.getBeginTime());
        dto.setEndTime(model.getEndTime());
        dto.setSort(model.getSort());
        dto.setRemark(model.getRemark());
        dto.setType(model.getType());
        dto.setLink(model.getLink());
        return dto;
    }

    public UserDTO toInfoDTO(User model, Object[] picType) {
        if (model == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setMobile(model.getMobile());

        dto.setCreateTime(model.getCreatedDate());
        return dto;
    }

}
