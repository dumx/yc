package com.pay.yc.constants;

/**
 * 公共常量
 * @author dumx
 * 2017年9月9日 上午10:38:34
 */
public class Constants {
	/**
	 * 订单完成状态(未支付时为null)
	 */
	public static final String COMPLETED ="COMPLETED";  //已完成
	public static final String NOTCOMPLETED ="NOTCOMPLETED"; //未完成
	public static final String CANCEL ="CANCEL"; //取消订单
	public static final String FAILURE ="FAILURE"; //失败
	public static final String DELETE ="DELETE"; //客户删除
	public static final String APPLY_REFUNDS = "APPLY_REFUNDS"; //申请退款
	public static final String REFUNDS_FINISH = "REFUNDS_FINISH"; //完成退款

	public static final String STR_FORMAT = "00000000";//订单号生成规则固定用户量,千万级

	/**
	 * 用户角色
	 */
	public static final String ROLE_ADMIN = "ROLE_ADMIN";//超管
	public static final String ROLE_COMMONLY = "ROLE_COMMONLY"; //普通管理员/读写/审核
	public static final String ROLE_USER = "ROLE_USER"; //基础管理员,仅读取权限

	/**
	 * 用户类型(前台用户/管理端用户)
	 */
	public static final Integer WEBUSER = 0;//管理端用户
	public static final Integer MOBILEUSER = 1; //手机端用户



}
