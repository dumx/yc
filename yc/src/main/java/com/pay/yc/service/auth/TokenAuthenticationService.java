package com.pay.yc.service.auth;

import com.pay.yc.common.util.JSONResult;
import com.pay.yc.model.admin.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 生成token和验证token
 * @author dumx
 * 2017年9月4日 下午4:19:26
 */
@Slf4j
public class TokenAuthenticationService {
	static final long EXPIRATIONTIME = 432_000_000;     // 5天
	public static final String SECRET = "P@ssw0222d";            // JWT密码
	public static final String TOKEN_PREFIX = "Bearer";        // Token前缀
	static final String HEADER_STRING = "Authentication";// 存放Token的Header Key

	public static void addAuthentication(HttpServletResponse response, User model) throws JSONException {

	// 生成JWT
		String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities",getRoleList(model))
                // 用户名写入标题
                .setSubject(model.getOpenId().toString())
                // 有效期设置
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();

		// 将 JWT 写入 body
        try {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
//            response.getOutputStream().println(JSONResult.fillResultString(0, "", JWT,model));
            response.getWriter().println(JSONResult.fillResultString(0, "", JWT,model));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(HEADER_STRING);
        //判断token否过期或者合法  true=合法，false=不合法或者过期
        boolean tokenInfo = false;
        if(token!=null){
        	tokenInfo=TokenAuthenticationService.parserJavaWebToken(token);
        }
		if (token != null && tokenInfo==true) {
            // 解析 Token
			 Claims  claims = Jwts.parser()
                    // 验签
					.setSigningKey(SECRET)
                    // 去掉 Bearer
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody();
            //获取用户编号
            String user = claims.getSubject();
            // 得到 权限（角色）
            List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            // 返回验证令牌
            return user != null ?
					new UsernamePasswordAuthenticationToken(user, null, authorities) :
			null;
		}
		return null;
	}
	
	//解析Token，同时也能验证Token，当验证失败返回false
    public static boolean parserJavaWebToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return true;
        } catch (Exception e) {
//        	log.info("token已过期：{}", e.getMessage());
            return false;
        }
    }

	//token过期重新生成token
	private static String createNewToken(String uid){
		 String newToken = Jwts.builder()
	                // 保存权限（角色）
	                .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
	                // 用户名写入标题
	                .setSubject(uid)
	                // 有效期设置
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	                // 签名设置
					.signWith(SignatureAlgorithm.HS256, SECRET)
					.compact();
		 
		 return newToken;
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
		System.out.println(roleList);
		return roleList;
	}

}
