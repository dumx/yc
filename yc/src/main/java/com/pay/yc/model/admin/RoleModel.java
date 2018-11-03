package com.pay.yc.model.admin;


import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

/**
 * 用户角色表
 * @author dumx
 * 2017年8月16日 上午11:06:26
 */
@Getter
@Setter
@Entity(name = "role")
public class RoleModel extends AbstractModel  implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	private String roleName;
	@Override
	public String getAuthority() {
		return roleName;
	}
}
