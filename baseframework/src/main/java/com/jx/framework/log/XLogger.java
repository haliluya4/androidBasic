package com.jx.framework.log;

/**
 * 日志接口
 */
public interface XLogger {
    void d(String tag, String msg);

    void w(String tag, String msg, Throwable e);

    void e(String tag, String msg, Throwable e);
}
