package com.pay.yc.repository.admin;

import com.pay.yc.model.admin.Config;
import com.pay.yc.model.admin.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ConfigRepository extends CrudRepository<Config, Long> {
	//获取全部配置
	List<Config> findAll();


	Config findOneById(Long id);

	List<Config> findByTypeIn(String[] type);

	Config findByName(String mobile);
	//配置列表
	Page<Config> findAll(Pageable pageable);

	Config findByType(String type);

}
	