package com.pay.yc.controller.admin;

import com.pay.yc.convertor.UserConvertor;
import com.pay.yc.dto.admin.UserDTO;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"测试"})
@RestController
@RequestMapping("/t")
public class TestRestController {
	@Autowired
	private UserRepository UserRepository;
	@Autowired 
	private UserConvertor UserConvertor;
	/**
	 * 获取用户列表列表
	 * @return List<UserDTO>
	 */
	 @RequestMapping(value = {"/list" }, method = {RequestMethod.GET })
	 @PreAuthorize("hasRole('ROLE_ADMIN') AND hasRole('ROLE_COMMONLY')")
	 public List<UserDTO> list(){
		 List<User> models=this.UserRepository.findAll();
		 return this.UserConvertor.toListDTO(models);
	 }
}
