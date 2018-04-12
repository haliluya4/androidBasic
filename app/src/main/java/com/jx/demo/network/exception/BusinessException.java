package com.jx.demo.network.exception;

/**
 * 自定义异常，表示服务器返回业务错误码
 */
public final class BusinessException extends RuntimeException {
    private final int code;

    /**
     * @param code 业务错误码
     * @param msg  服务端返回的错误信息，可以通过getMessage方法获取
     */
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    /**
     * 获取错误码
     *
     * @return 业务错误码
     */
    public int getCode() {
        return code;
    }
}
