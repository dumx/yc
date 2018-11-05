package com.pay.yc;

import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class AdminDataInit implements CommandLineRunner {

    @Autowired
    private UserRepository UserRepository;

    //处理密码加密
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... arg0) throws Exception {
//		initUser();

    }

    //初始化admin管理员
    private void initUser() {
        User User = new User();
        User.setCreatedDate(new Date());
        User.setUsername("admin");
        User.setMobile("18500239596");
        User.setUserType(0);
        User.setPassword(encoder.encode("111111"));//短信验证码代替
        UserRepository.save(User);
    }

}
