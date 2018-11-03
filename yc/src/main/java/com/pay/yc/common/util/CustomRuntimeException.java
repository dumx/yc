package com.pay.yc.common.util;

/**
 * 自定义异常类
 * @author dumx
 * 2017年8月2日 下午4:21:11
 */
public class CustomRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6882178806561789418L;
    
    private String code;

    public CustomRuntimeException(final String message) {
        super(message);
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomRuntimeException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
