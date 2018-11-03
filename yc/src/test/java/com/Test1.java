package com;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class Test1 {
    public static void main(String[] args) {

        //TODO API: https://docs.open.alipay.com/194/105170/#s4

        //格式化当前时间
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strDate = sfDate.format(new Date());
        //得到17位时间如：20170411094039080
        System.out.println("时间17位：" + strDate);
        //为了防止高并发重复,再获取3个随机数
        String random = getRandom620(3);

        //最后得到20位订单编号。
        System.out.println("订单号20位：" + strDate + random);

        String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDBgQM858qcCHs3Ny5SuX+n+mnXJPQFDNGrLzxx4ZyD/tQVFSCYoczCCWnWBg6sQ3kXWShv807eVtc+WWkAa7Yz2Vk7khXRDbZJe+OYxyRLeg++TdxjQz/EV9jmqY7Nz2MPWMYTKG2wdx/wlTFZUDfNjMQQ24LoU8uj7vE0uy0XJUMLVulUgWbsqZWYwXYs4GMTc18aN9vnObKGe7VV1fQdkEAr7OLAige8f5uKnMBTbO0KgyaJkPVfx3FjtSE0pOjAHhlKScRmujl77W8pGPRHrbyTD84l08Z8BHkKX37QKAEiPegLTjsaJt88CqMitPAIWkg/H2i6YKukk/YRgayJAgMBAAECggEAZQUbUaKY7dinqu3N6zYYZPOgacdK9Y/5rerdHX7xHR/eTJtZBxYBNZOAg1FqO5iGESBksf1NwVmIyW4YGTYiSvM/WJAtf5QvBDH/YfUlB5pTucAgPFoRYkmSXOlMOjn4tzxbPAkxKp0mOY1J1BzC5TaF7V8L+oo0mALYPfUx1xCd29E9Bc8rhOwqVFH6NUwc8UWkYeixNCrMva/H+asufjTAu4+QWnr2yb61kIGTbjfSoRGNcimvYK6mF3HNRrtaOqDo9GMshJCnn3PGxtkckzyYLzQ84iAaDn46lMhWmK28B9qqvBwucgElL4O+ty+JcGGAKYdm/IDRAEeLTBFsAQKBgQDwP/idYzIcg944c0KH5wF1guT6CQrjj1BsEXPs5pp75a0q60NQtdcC+HiKNz/qTaMhVMzYgpOYGUkDumoc67hKP1a5rxrTFPrQOMcRwC3VUpQxSD+fGWkcsv7j8aAmZy2on+Dsy/anPgVehiOctGCpTV+RoMvJO16jgCzrm1+0QQKBgQDOMIVkmLPjUTo6OvfAUmx6Bv83O9jFTrzl9igE8M1rIPtZP+HLElnwiw+eHmjDaHxRRwhG91QHKoZWv+9xV9beTCPA1jAeOur4yY0+ukgwt8QV3cNExkFMs2VRbxn5z54sWgc+oJr5YOpGPSGBVBv5g3QaNyJAN42j/Zwu9o3GSQKBgQCTFAami4itDkmns2AihNhO0WBOdeS1pRHt5s1aVV5zxQq7QKXILlo/1k8tBl/Zl8fratexIFctASXC44d8xw3eXmJAcKNyX+CgQnIUOtuUQea5OOrCArNnv2jtsQmDGR1Jov722HLatcxhrqr0jzSx0M1WAONhATiRzbEyV79CQQKBgE9Do+QaN/1JOqMsDBYpGX6HthEbtqeTL1cQHC5aC/4E/ds/jaOteFnae7SDaC7GuhPcxLfTWMAwfthWjMv7wqROjOgS7wiQCch+YwgcQKhJ3N2zFOAZfvzncimuFkRMpjfSnDpdL3Zy5Vz/HyRCm0Z+XGG/lDEvqgnC8wmyyqnRAoGBAK1KzsJD1fhaveJqwj1BQzPHDlnfxoey1SL5hOcsf8xUETTLSO/sqRS/ScmOcSBOZih6VT//5Po6Hx0UsXqf7MuuriTK8eMMDKrkbSWQXm0VeLmRrjdH/x2Xzgq8aQ31IIH2SA3/WC2e+MyOHL1yfG7KdQfajigWuzFY2HtLmBoS";
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAso4n0QQyDCccBNwBfRtvaXEgBsZvJ2boPs02JR/XZgrD+ve6FeODWu+U1B84P+9XoNNEGHQ80ZH6eM/d6TfHNrNRqNQuOcu4YzYc807AW0Wr55U3qa4O1i/UlIfgshqKwE6jFSwH6wbmMr5haQ/kQpPFe+C0v94ZE7LAA3+hgsg+59jHqaprFhwnTmBcImtNSERgj2he8//j1Ytx53gWsOFfQiR1Rh9qUgHzAOl1TPU8M3yVWSgT2xmF8LUSH04sCnyjNVqaG51Kwplg1lL3xaEXXDXtv7L1yoQcY/Yb0sgI9XuD8EVzTrkAIX+6WolDZ+VEvPbBcT8RmVr6EcC4/QIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2018101361689138", APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类

        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + strDate + "\"," +
                "    \"total_amount\":\"0.1\"," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//设置业务参数
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.print(response.getBody());
    }

    /**
     * 获取6-10 的随机位数数字
     *
     * @param length 想要生成的长度
     * @return result
     */
    public static String getRandom620(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
            result += randInt;
        }
        return result;
    }


}
