package com.pay.yc.repository.admin;

import com.pay.yc.model.admin.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
	//获取全部用户
	List<User> findAll();

	//根据用户名查询用户
	Optional<User> findOneByUsername(String userName);

	User findOneById(Long id);

	User findByMobile(String mobile);


	User findByOpenId(String openId);


//	Page<User> findByUsernameLikeOrMobileLikeAndStatus(String username, String mobile, int statue, Pageable pageable);

	//用户列表
	Page<User> findAll(Pageable pageable);

}
	