package com.pay.yc.common.result;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.common.util.JsonUtils;

/**
 * 登录拦截错误验证（暂无用）
 * @author dumingxin
 *
 */
@Component
public class LocalAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger log = LoggerFactory.getLogger(LocalAuthenticationFailureHandler.class);
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		ResultError error = null;
		if (!(exception instanceof UsernameNotFoundException) && !(exception instanceof BadCredentialsException)) {
			if (exception instanceof LockedException) {
				error = new ResultError("401", "帐户已锁定.", (String) null);
			} else if (exception instanceof DisabledException) {
				error = new ResultError("401", "帐户已禁用.", (String) null);
			} else if (exception instanceof AccountExpiredException) {
				error = new ResultError("401", "帐户已过期.", (String) null);
			}else{
				error = new ResultError("401", exception.getMessage(), (String) null);
			}
		} else {
			error = new ResultError("401", exception.getMessage(), (String) null);
		}

		if (error != null) {
			if (log.isInfoEnabled()) {
				log.info("用户登录失败，" + error.getErrmsg());
			}

			ResultDTO rs = ResultDTO.failure(new ResultError[]{error});
			response.setStatus(200);
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(JsonUtils.toJson(rs));
			writer.flush();
			writer.close();
		} else {
			if (log.isInfoEnabled()) {
				log.info("用户登录失败", exception);
			}

			response.sendError(401, "认证失败: " + exception.getMessage());
		}

	}
	
}
