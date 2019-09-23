package com.pay.yc.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pay.yc.common.result.dto.ResultDTO;
import com.pay.yc.model.admin.User;
import com.pay.yc.repository.admin.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    static final String SECRET = "P@ssw0222d";            // JWT密码


//    @Value(value = "${webapp.url}")
//    private String indexUrl;
    @GetMapping(value = "/getJn")
    public static String getHTTP(){
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://www.7jn.com/jiangnan.txt");
            try {
            HttpResponse response = client.execute(request);

            String result = EntityUtils.toString(response.getEntity());//可以很好的处理中文，保证中文没有乱码
            //System.out.println("得到CRM内容:"+result);
            return result;

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            return "";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            return "";
            }
}


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
            //保存手机号的同时注册门禁系统
            Map m=registerDoorSystem(u);
            return ResultDTO.success(m);
        }else{
            return ResultDTO.failure();
        }
    }



    //微信绑定门禁系统
    @GetMapping(value = "/weixinbindrDoorSystem")
    public Map weixinbindrDoorSystem(@RequestParam String mobile){
        HashMap map = new HashMap();
        map.put("apiid", "bl14fd434cbe055eac");
        map.put("apikey", "e44ee2af7f9aaa857aaa501e65e9dcb6");
        String tokenStr = httpUrlConnection("https://api.parkline.cc/api/token", map);
        JSONObject tokenMap = new Gson().fromJson(tokenStr, JSONObject.class);
        String token = tokenMap.getString("access_token");

        log.info("获取二维码token==============================:"+token);

        //获取二维码
        HashMap<String, String> bind = new HashMap<>();
        bind.put("token",token);
        bind.put("tel",mobile);
        bind.put("devid","215093");
        String bindResult = httpUrlConnection("https://api.parkline.cc/api/wxbind", bind);
        log.info("获取二维码生成链接==============================:"+bindResult);
        JSONObject jsonObject=JSONObject.parseObject(bindResult);
        String code=jsonObject.getString("code");
        String url=jsonObject.getString("url");
        //获取成功,直接绑定
//        if("0".equals(code)){
//
//            ResponseEntity<String> openResponse = restTemplate.getForEntity(String.format(url), String.class);
//            openResponse.getStatusCode();
//            openResponse.getBody();
//        }
        Map m=new HashMap();
        m.put("code",code);
        m.put("url",url);
        return m;
    }


    //注册门禁系统
    public Map registerDoorSystem(User user){
        HashMap map = new HashMap();
        map.put("apiid", "bl14fd434cbe055eac");
        map.put("apikey", "e44ee2af7f9aaa857aaa501e65e9dcb6");
        String tokenStr = httpUrlConnection("https://api.parkline.cc/api/token", map);
        JSONObject tokenMap = new Gson().fromJson(tokenStr, JSONObject.class);
        String token = tokenMap.getString("access_token");
        User userModel = new User();
        userModel.setMobile(user.getMobile());
        HashMap<String, String> add = new HashMap<>();
        add.put("token", token);
        add.put("typeid", "100");
        add.put("nickname", "鱼池用户");
        add.put("tel", user.getMobile());
        String addResult = httpUrlConnection("https://api.parkline.cc/api/facecgi", add);
        log.info(addResult);
        HashMap<String, String> bind = new HashMap<>();
        bind.put("token", token);
        bind.put("typeid", "200");
        bind.put("tel", user.getMobile());
        bind.put("devid", "215093");
        bind.put("lockid", "01");
        bind.put("startdate", "1970-10-01");
        bind.put("enddate", "1970-10-01");
        bind.put("starttime", "00:01");
        bind.put("endtime", "23:59");
        String bindResult = httpUrlConnection("https://api.parkline.cc/api/facecgi", bind);
        log.info(bindResult);
        Map m=new HashMap();
        m.put("bindResult",bindResult);
        m.put("mobile",user.getMobile());
        m.put("binded",user.getBinded());
        m.put("bindToken",token);
        return m;
    }

    private static String httpUrlConnection(String pathurl, HashMap<String, String> hm) {

        String result = null;
        try {
            URL url = new URL(pathurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("referer", "http://u66whn.natappfree.cc");//后台填写的授权接入地址，必须包含http或https协议
            conn.setRequestMethod("POST");
            PrintWriter pw = new PrintWriter(conn.getOutputStream());
            pw.print(getParams(hm));
            pw.flush();
            pw.close();
            if (conn.getResponseCode() == 200) {
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                responseReader = new BufferedReader(new InputStreamReader(conn
                        .getInputStream(), "utf-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                responseReader.close();
                result = sb.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getParams(Map<String, String> paramValues) {
        String params = "";
        String beginLetter = "";
        Set<String> key = paramValues.keySet();
        try {
            for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
                String s = (String) it.next();
                if (params.equals("")) {
                    params += beginLetter + s + "="
                            + URLEncoder.encode(paramValues.get(s), "UTF-8");
                } else {
                    params += "&" + s + "="
                            + URLEncoder.encode(paramValues.get(s), "UTF-8");
                }
            }
        } catch (Exception e) {

        }


        return params;
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
                    if(openId!=null){
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
                    }else{
                        log.info("openId为 null ******************************************");
                        map.put("accessToken", access_token);
                        map.put("refreshToken", refreshToken);
                        map.put("openId", null);
                        map.put("id", null);
                        map.put("token", null);
                        map.put("mobile",null);
                    }
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
