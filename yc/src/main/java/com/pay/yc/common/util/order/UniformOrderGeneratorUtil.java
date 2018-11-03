package com.pay.yc.common.util.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 自定义订单生成类(支付专用)
 * @author: dumx    
 * @date:   2017年11月14日 上午10:10:36   
 * @version V1.0 
 * 注意：禁止外泄以及用于其他的商业目的
 */
public class UniformOrderGeneratorUtil {

    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public static final String dtLong  = "yyMMddHHmmss";


    /** 完整时间 yyyy-MM-yc HH:mm:ss */
    public static final String simple  = "yyyy-MM-yc HH:mm:ss";

    /** 年月日(无下划线) yyyyMMdd */
    public static final String dtShort = "yyyyMMdd";

    public static String getOutTradeNo(){
        return UniformOrderGeneratorUtil.getOrderNum()+UniformOrderGeneratorUtil.getRandomNumStringByLength(5)+UniformOrderGeneratorUtil.getcurrTime();
    }
    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getOrderNum(){
        final Date date=new Date();
        final DateFormat df=new SimpleDateFormat(UniformOrderGeneratorUtil.dtLong);
        return df.format(date);
    }

    /**
     * 返回系统当前时间毫秒值
     * @return
     *
     */
    public static String getcurrTime(){
        final String currDate = String.valueOf(System.currentTimeMillis());
        return currDate;
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(final int length) {
        final String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        final Random random = new Random();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            final int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-yc HH:mm:ss
     * @return
     */
    public  static String getDateFormatter(){
        final Date date=new Date();
        final DateFormat df=new SimpleDateFormat(UniformOrderGeneratorUtil.simple);
        return df.format(date);
    }

    /**
     * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
     * @return
     */
    public static String getDate(){
        final Date date=new Date();
        final DateFormat df=new SimpleDateFormat(UniformOrderGeneratorUtil.dtShort);
        return df.format(date);
    }

    /**
     * 获取一定长度的随机数字
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomNumStringByLength(final int length) {
        final String base = "0123456789";
        final Random random = new Random();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            final int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-yc HH:mm:ss
     * @return
     */
    public  static String getDateFormatterByPara(final Date date){
        final DateFormat df=new SimpleDateFormat(UniformOrderGeneratorUtil.simple);
        return df.format(date);
    }

}
