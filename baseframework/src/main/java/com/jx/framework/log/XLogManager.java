package com.jx.framework.log;

/**
 * 框架所需的日志管理器，可以配置日志输出实现类
 */
public final class XLogManager {
    /**
     * 日志输出实现
     */
    private static XLogger sXLogger;

    /**
     * 设置公共日志输出实现
     *
     * @param logger XLogger实例
     */
    public static synchronized void setLogger(XLogger logger) {
        sXLogger = logger;
    }

    /**
     * 输出调试信息
     */
    public static synchronized void d(String tag, String msg) {
        if (sXLogger != null) {
            sXLogger.d(tag, msg);
        }
    }

    /**
     * 输出警告信息
     */
    public static synchronized void w(String tag, String msg, Throwable e) {
        if (sXLogger != null) {
            sXLogger.w(tag, msg, e);
        }
    }

    /**
     * 输出错误信息
     */
    public static synchronized void e(String tag, String msg) {
        if (sXLogger != null) {
            sXLogger.e(tag, msg, null);
        }
    }

    /**
     * 输出错误信息
     */
    public static synchronized void e(String tag, String msg, Throwable e) {
        if (sXLogger != null) {
            sXLogger.e(tag, msg, e);
        }
    }
}
