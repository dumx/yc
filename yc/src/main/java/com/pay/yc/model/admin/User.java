package com.pay.yc.model.admin;


import com.pay.yc.common.result.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;

@Getter
@Setter
@Entity
public class User extends AbstractModel implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 微信公众号用户唯一标识
     */
    private String openId;

    /**
     * 手机号
     */
    @Length(max = 11)
    @Column(length = 11)
    private String mobile;

    /**
     * 用户登录名
     */
    @Length(max = 64)
    @Column(length = 64)
    private String username;

    /**
     * 密码
     */
    @Length(max = 64)
    @Column
    private String password;


    /**
     * 用户类型(0是超管/1普通用户)
     */
    private int userType = 1;


    //是否绑定
    @Column
    private Integer binded=0;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 关联角色表
     */
    /*@ManyToMany(fetch = FetchType.EAGER)
    private List<RoleModel> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        List<RoleModel> roles = this.getRoles();
        for (RoleModel role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return auths;
    }*/

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
