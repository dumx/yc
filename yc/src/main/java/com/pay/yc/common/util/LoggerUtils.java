package com.pay.yc.common.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * @author dumx
 * 2017年8月2日 下午5:54:54
 */
public class LoggerUtils {

    private static Map<String, Logger> loggers = new HashMap<>();

//    private static Logger accessLogger = LoggerFactory.getLogger("access");

    public static void accessLog() {
    }

    public static void errorLog(final Class<?> clazz, final String message) {
        final Logger logger = LoggerUtils.logger(clazz);
        logger.error(message);
    }

    public static void errorLog(final Class<?> clazz, final Exception e, final String message) {
        final Logger logger = LoggerUtils.logger(clazz);
        if (message != null) {
            logger.error(message, e);
        } else {
            logger.error(e.getMessage(), e);
        }
    }

    public static void useIngLog(final Class<?> clazz, final String message) {
        final Logger logger = LoggerUtils.logger(clazz);
        if (!logger.isInfoEnabled()) {
            return;
        }
        logger.info(message);
    }

    private static Logger logger(final Class<?> clazz) {
        Logger logger = LoggerUtils.loggers.get(clazz.getName());
        if (logger != null) {
            return logger;
        }
        logger = LoggerFactory.getLogger(clazz);
        LoggerUtils.loggers.put(clazz.getName(), logger);
        return logger;
    }
}
