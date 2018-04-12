package com.jx.demo.network;

/**
 * 结果校验接口，需要校验结果有效性的类，实现该接口即可
 */
public interface ResultValidator {
    /**
     * 结果是否有效
     *
     * @return 有效-返回null，异常-返回错误信息
     */
    String checkIsValid();
}
