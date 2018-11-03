package com.pay.yc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@Slf4j
public class UserIDAuditorBean implements AuditorAware<Long> {
    public Long getCurrentAuditor() {
        try{
            Object obj =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Long.parseLong(obj.toString());
        }catch (Exception e){
            log.warn("-----数据正在更新-----!"+e.getMessage());
            return null;
        }
    }
}
