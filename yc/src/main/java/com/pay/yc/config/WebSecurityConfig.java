package com.pay.yc.config;

import com.pay.yc.common.filter.JWTLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.pay.yc.common.filter.JWTAuthenticationFilter;

/**
 * @author dumx
 * 2017年8月1日 下午11:41:05
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HubProperties hubProperties() {
        return new HubProperties();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                .antMatchers("/").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/v2/api-docs").permitAll()


                //token 过期，重新获取token
                .antMatchers("/authenticate/getNewToken").permitAll()
                //微信支付回调
                .antMatchers("/w/notifies/weixin").permitAll()
                //微信授权接口权限
                .antMatchers("/wx/**").permitAll()
                //导出权限
                .antMatchers("/wdo/export/**").permitAll()

                //给静态文件（图片）放行
                .antMatchers("/static/**").permitAll()
                // 所有 /login 的POST请求 都放行
                .antMatchers(HttpMethod.POST, "/login").permitAll() //登录接口
                // 添加权限检测
                .antMatchers("/hello").hasAuthority("AUTH_WRITE")
                // 角色检测
                .antMatchers("/world").hasRole("ADMIN")
//                .antMatchers("/t/**").hasAuthority("USER")
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/webjars/**");
    }

    //
//    @Autowired
//    private UserDetailsService userService;
//    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth)
//            throws Exception {
//    	  // 使用自定义身份验证组件
//        auth.authenticationProvider(new MobileAuthenticationProvider());
//    }
//
//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return userService;
//    }

}
