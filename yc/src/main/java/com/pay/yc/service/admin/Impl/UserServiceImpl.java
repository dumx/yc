package com.pay.yc.service.admin.Impl;

import java.util.*;

import com.pay.yc.common.util.CustomRuntimeException;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import com.pay.yc.service.admin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * userservice实现类
 * @author dumx
 * 2017年8月1日 下午9:18:56
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository UserRepository;

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return this.UserRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = UserRepository.findOneByUsername(username);
		if (user.isPresent()) {
			log.info("登录成功..用户:{}", username);
			//根据用户名登录
			return UserRepository.findOneByUsername(username).orElseThrow(
					() -> new UsernameNotFoundException(String.format("用户：%s 不存在！", username)));
		} else {
			log.info("登录失败..用户:{}", username);
			throw new CustomRuntimeException("400", "该用户暂未注册!");
		}

	}

//	@Override
//	public Set<String> reSetWhiteList() {
//		List whiteList = UserRepository.findWhiteList();
//		if (whiteList != null) {
//			IP_WHITE_LIST = new HashSet<>();
//			for (Object ips : whiteList) {
//				if (null != ips) {
//					String ipsStr = ips.toString();
//					if (!("").equals(ipsStr)) {
//						List<String> strList = Arrays.asList(ips.toString().split(","));
//						IP_WHITE_LIST.addAll(strList);
//					}
//				}
//			}
//		}
//		return IP_WHITE_LIST;
//	}

}
