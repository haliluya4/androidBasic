package com.jx.demo.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 请求参数辅助工具
 */
public final class RequestParamsHelper {
    /**
     * 获取新的请求参数，会自动填充requestId等公共参数。如果不需要，可以直接new一个HashMap
     *
     * @return 包含公共请求参数的Map
     */
    public static Map<String, Object> createNew() {
        Map<String, Object> newParams = new HashMap<>();
        // TODO 换成自己平台的公共请求参数
        newParams.put("requestId", UUID.randomUUID().toString());
        return newParams;
    }
}
