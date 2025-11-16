package com.handler.excel2word.core.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public LogUtil() {
    }

    public static void debug(String msg) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(msg);
        }

    }

    public static void info(String msg) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(msg);
        }

    }

    public static void warn(String msg) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(msg);
        }

    }

    public static void error(String msg) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(msg);
        }

    }

    public static void error(String msg, Throwable e) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(msg, e);
        }

    }
}
