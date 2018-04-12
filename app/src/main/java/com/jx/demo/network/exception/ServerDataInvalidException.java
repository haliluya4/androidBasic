package com.jx.demo.network.exception;

/**
 * 自定义异常，表示服务器返回数据异常
 */
public final class ServerDataInvalidException extends RuntimeException {

    private final String resultClass;

    /**
     * @param resultClass 结果类
     * @param msg         错误信息，可以通过getMessage方法获取
     */
    public ServerDataInvalidException(String resultClass, String msg) {
        super(msg);
        this.resultClass = resultClass;
    }

    public String getResultClass() {
        return resultClass;
    }
}
