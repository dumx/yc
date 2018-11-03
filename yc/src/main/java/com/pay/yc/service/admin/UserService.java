package com.pay.yc.service.admin;

import java.util.List;
import java.util.Set;

import com.pay.yc.model.admin.User;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 
 * @author dumx
 * 2017年8月1日 下午9:15:40
 */
public interface UserService extends UserDetailsService {
	List<User> getAll();
//	Set<String> reSetWhiteList();
}
