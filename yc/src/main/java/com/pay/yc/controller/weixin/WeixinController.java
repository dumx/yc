package com.pay.yc.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @TODO 微信授权
* @author dumingxin
* @Date 2018/11/3 22:31
*/
@RestController
@RequestMapping(value = "/wx")
@Slf4j
public class WeixinController {
    private static final String openidUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    //获取 code
//    private static final String openUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=http://fish.zwkjsoft.com/index.html&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    private static final String checkTokenUrl = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";


    private static final String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    //token 相关
    static final long EXPIRATIONTIME = 432_000_000;     // 5天
    static final String SECRET = "P@ssw02d";            // JWT密码


//    @Value(value = "${webapp.url}")
//    private String indexUrl;

    @GetMapping(value = "/checkToken")
    public Object checkToken(HttpServletRequest request,@RequestParam String openId,@RequestParam String accessToken) {
        ResponseEntity<String> openResponse = restTemplate.getForEntity(String.format(checkTokenUrl, accessToken, openId), String.class);
        try {
            if (openResponse.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject jsonObject = new Gson().fromJson(openResponse.getBody(), JSONObject.class);
                String code = jsonObject.getString("errcode");
                log.info("检查 Token 是否有效:======================================：" + code);

                return code;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @GetMapping(value = "/refreshToken")
    public Object refreshToken(HttpServletRequest request,@RequestParam String refreshToken) {
        ResponseEntity<String> openResponse = restTemplate.getForEntity(String.format(refreshTokenUrl,"wx95e739344708f031",refreshToken), String.class);
        try {
            if (openResponse.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject jsonObject = new Gson().fromJson(openResponse.getBody(), JSONObject.class);
                String accessToken = jsonObject.getString("refreshToken");
                log.info("刷新accessToken:======================================：" + accessToken);
                return accessToken;
            }else{

            }
        } catch (Exception e) {
        }
        return null;
    }

    @PostMapping(value = "/saveMobile")
    public ResultDTO<?> saveMobile(@RequestBody Map map) {
        String mobile=map.get("mobile").toString();
        String openId=map.get("openId").toString();
        User user=this.userRepository.findByOpenId(openId);
        if(user!=null){
            user.setMobile(mobile);
            User u=this.userRepository.save(user);
            return ResultDTO.success(u);
        }else{
            return ResultDTO.failure();
        }
    }

    @GetMapping(value = "/checkMobile")
    public Boolean checkMobile(@RequestParam String openId) {
        User user=this.userRepository.findByOpenId(openId);
        if(user.getMobile() !=null && !user.getMobile().equals("")){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 从微信拉取用户信息
     * @param request
     * @param accessToken
     * @param openId
     * @return
     */
    @GetMapping(value = "/userInfo")
    public Object userInfo(HttpServletRequest request,
                           @RequestParam("accessToken") String accessToken,
                           @RequestParam("openId") String openId) {
        ResponseEntity<String> userInfoResponse = restTemplate.getForEntity(String.format(userInfoUrl, accessToken, openId), String.class);
        Map map = new HashMap();
        try {
            if (userInfoResponse.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject jsonObject = new Gson().fromJson(userInfoResponse.getBody(), JSONObject.class);
                String errcode = jsonObject.getString("errcode");
                String errmsg = jsonObject.getString("errmsg");
                log.info("errcode:======================================：" + errcode);
                log.info("errmsg:======================================：" + errmsg);
                if(errcode==null){
                    String jsonString= new String(userInfoResponse.getBody().getBytes("ISO-8859-1"), "UTF-8");
                    JSONObject json=new Gson().fromJson(jsonString, JSONObject.class);
                    String nickname = json.getString("nickname");
                    String headimgurl = json.getString("headimgurl");
                    log.info("openId:======================================：" + openId);
                    log.info("accessToken:======================================：" + accessToken);
                    map.put("nickname",nickname);
                    map.put("headImg",headimgurl);

                }else{
                    log.info("获取用户错误信息:======================================：" + errmsg);
                    map.put("errcode",errcode);
                    map.put("errmsg",errmsg);
                }
                return map;
            }
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 进行微信授权,授权成功后将系统 token 返回给前台
     * @param request
     * @param code
     * @return
     */
    @GetMapping(value = "/oauth")
    public Object oauth2API(HttpServletRequest request, @RequestParam String code) {
        log.info("code======================================：" + code);
        ResponseEntity<String> openResponse = restTemplate.getForEntity(String.format(openidUrl, "wx95e739344708f031", "adb059bedcc64b759350a5014b2907ed", code), String.class);
        try {
            if (openResponse.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject jsonObject = new Gson().fromJson(openResponse.getBody(), JSONObject.class);
                String access_token = jsonObject.getString("access_token");
                String refreshToken = jsonObject.getString("refresh_token");
                log.info("access_token======================================：" + access_token);
                log.info("refreshToken======================================：" + refreshToken);
                String openId = jsonObject.getString("openid");
                log.info("openId======================================：" + openId);
                User user = userRepository.findByOpenId(openId);
                Map map = new HashMap();
                if (user == null) {
                    log.info("保存新授权用户信息======================================");
                    User userModel = new User();
                    userModel.setOpenId(openId);
                    User u=this.userRepository.save(userModel);
                    map.put("accessToken", access_token);
                    map.put("refreshToken", refreshToken);
                    map.put("openId", openId);
                    map.put("id", u.getId());
                    //获取系统 token
                    String ycToken=this.getToken(u.getId());
                    map.put("token", ycToken);
                    map.put("mobile",u.getMobile());
                    log.info("生成本系统新 token====================================:"+ycToken);
                } else {
                    log.info("该用户已授权直接返回====================================");
                    map.put("openId", user.getOpenId());
                    map.put("accessToken", access_token);
                    map.put("refreshToken", refreshToken);
                    map.put("id",user.getId());
                    //获取系统 token
                    String ycToken=this.getToken(user.getId());
                    map.put("token", ycToken);
                    map.put("mobile",user.getMobile());
                    log.info("生成本系统新 token====================================:"+ycToken);
                }
                return map;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取本系统 token
     * @param openId
     * @return
     */
    public String getToken(Long userId) {
//        User model=this.userRepository.findOneById(uid);
        String newToken = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", "")
                // 用户名写入标题
                .setSubject(userId.toString())
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        log.info("获取新的token成功：{}", newToken);
        return newToken;
    }

    /**
     * 微信 token 验证
     */
    private String TOKEN = "yuchi";

    @GetMapping("/wxverify")
    public String wxverify(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echostr) {

        //排序
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String myString = sha1(sortString);
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("签名校验通过");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            System.out.println("签名校验失败");
            return "";
        }
    }

    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



}
