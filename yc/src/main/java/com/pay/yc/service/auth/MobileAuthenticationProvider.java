package com.pay.yc.service.auth;

import com.pay.yc.common.util.CustomRuntimeException;
import com.pay.yc.model.admin.User;
import com.pay.yc.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 自定义验证
 * @author dumx
 * 2017年9月4日 下午4:20:04
 */
@Component
@Configuration
public class MobileAuthenticationProvider implements AuthenticationProvider {
	 	@Autowired
	    private UserService UserService;

	    @Override
	    public Authentication authenticate(Authentication authentication)
	            throws AuthenticationException {
	    	String username=authentication.getName();
	    	String password=authentication.getCredentials().toString().trim();
	    	User user=(User) UserService.loadUserByUsername(username);
	    		if (username==null) {
				throw new CustomRuntimeException("用户不存在或用户名手机号错误！");
			}
	    		//BCryptPasswordEncoder密码加密匹配
			BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
			//数据库中存储的密码
			String dbPassword=user.getPassword();
			//后台登录匹配密码
			boolean isOrNo=encode.matches(password,dbPassword);
			if(isOrNo==false){
				throw new CustomRuntimeException("密码错误!");
			}
	    	  // 生成令牌
	    	  // 这里设置权限和角色
//            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add( new GrantedAuthorityImpl("ROLE_ADMIN") );
//            authorities.add( new GrantedAuthorityImpl("AUTH_WRITE") );
//            Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
//            return auth;
            Collection<? extends GrantedAuthority> grantedAuthority=user.getAuthorities();
	    	return new UsernamePasswordAuthenticationToken(user, password,grantedAuthority);
	    }

	    @Override
	    public boolean supports(Class<?> authentication) {
	        return true;
	    }
}
