package com.pay.yc.repository.admin;

import com.pay.yc.model.admin.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;


/**
* @TODO
* @author dumingxin
* @Date 2018/2/28 17:48
*/
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    RoleModel findAllByRoleName(String name);
}
