package com.pay.yc.convertor;

import com.pay.yc.common.result.AbstractConvertor;
import com.pay.yc.dto.admin.UserDTO;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.PaymentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserConvertor extends AbstractConvertor<User, UserDTO> {


    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    public User toModel(UserDTO dto, User userModel) {
        if(userModel==null){
            userModel =new User();
            userModel.setUsername(dto.getUsername());
            userModel.setCreatedDate(new Date());
            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
            String hashPass = encode.encode(dto.getPassword());
            userModel.setPassword(hashPass);

        }else{
            userModel.setUsername(dto.getUsername());
            userModel.setPassword(dto.getPassword());
        }

        return userModel;
    }

    public UserDTO toDTO(User model, boolean arg1) {
        if (model == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setMobile(model.getMobile());
        dto.setCreateTime(model.getCreatedDate());
//        PaymentAccount paymentAccount = paymentAccountRepository.findTopByUserId(model.getId());
//        double b=paymentAccount.getBalance().longValue() * 1.0  /100;
//        BigDecimal balacne=new BigDecimal(b);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
//        balacne=balacne.setScale(2, BigDecimal.ROUND_HALF_UP);
//        dto.setBalance(balacne);
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
