package com.jx.demo.network.exception;

/**
 * 自定义异常，表示token失效，需要重新登录
 */
public final class ReLoginException extends RuntimeException {
    /**
     * @param msg 服务端返回的错误信息，可以通过getMessage方法获取
     */
    public ReLoginException(String msg) {
        super(msg);
    }
}
