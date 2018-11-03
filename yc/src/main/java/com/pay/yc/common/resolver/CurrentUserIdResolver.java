package com.pay.yc.common.resolver;

import com.pay.yc.common.annotation.CurrentUserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by yjl on 2017/8/22 0022.
 * 根据注释 填充 User
 */
public class CurrentUserIdResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUserId.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    	Object userId = webRequest.getAttribute("currentUserId", RequestAttributes.SCOPE_REQUEST);
        if (userId != null) {
            return userId;
        }else {
            userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        try{
            return Long.parseLong(userId.toString());
        }catch (Exception e){
            throw new SessionAuthenticationException("未登录");
        }

    }
}
