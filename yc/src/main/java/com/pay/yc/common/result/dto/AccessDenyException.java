/**
 * 异常处理
 */
package com.pay.yc.common.result.dto;

public class AccessDenyException extends RuntimeException {
	private static final long serialVersionUID = 6774083802665621769L;
	private final String code;

	public AccessDenyException(String code) {
		this.code = code;
	}

	public AccessDenyException(String code, String defaultMessage) {
		super(defaultMessage);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}