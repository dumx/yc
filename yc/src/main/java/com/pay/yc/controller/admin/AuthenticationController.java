package com.pay.yc.controller.admin;

import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 暂时测试
 * @author dumx
 * 2017年9月5日 下午3:35:48
 */
@Api(tags = {"token刷新API"})
@RestController
@RequestMapping("/authenticate")
@Slf4j
public class AuthenticationController {
	@Autowired
	private UserRepository UserRepository;

	static final long EXPIRATIONTIME = 432_000_000;     // 5天
	static final String SECRET = "P@ssw02d";            // JWT密码
    @ApiOperation(value="获取新的token", notes="获取新的token",httpMethod="GET")
    @RequestMapping(value="/getNewToken",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDTO<?> getNewToken(@RequestParam Long uid) {
    	 User model=this.UserRepository.findOneById(uid);
    	 String newToken = Jwts.builder()
	                // 保存权限（角色）
	                .claim("authorities", getRoleList(model))
	                // 用户名写入标题
	                .setSubject(uid.toString())
	                // 有效期设置
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	                // 签名设置
					.signWith(SignatureAlgorithm.HS256, SECRET)
					.compact();
    	log.info("获取新的token成功：{}", newToken);
    	return ResultDTO.success(newToken);
    }

	//获取当前登录用户角色并保存到 token中
	public static StringBuilder getRoleList(User model){
		StringBuilder roleList=new StringBuilder();
		/*for(int i=0;i<model.getRoles().size();i++)
		{
			if(i==model.getRoles().size()-1)//当循环到最后一个的时候 就不添加逗号,
			{
				roleList.append(model.getRoles().get(i).getRoleName());
			}
			else {
				roleList.append(model.getRoles().get(i).getRoleName());
				roleList.append(",");
			}

		}*/
		return roleList;
	}

}
