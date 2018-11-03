package com.pay.yc.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.pay.yc.service.auth.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * JWT路由接口拦截
 * @author dumx
 * 2017年9月4日 下午4:20:51
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		 Authentication authentication = TokenAuthenticationService
	                .getAuthentication((HttpServletRequest)request);

	        SecurityContextHolder.getContext()
	                .setAuthentication(authentication);
	        filterChain.doFilter(request,response);

	}

}
