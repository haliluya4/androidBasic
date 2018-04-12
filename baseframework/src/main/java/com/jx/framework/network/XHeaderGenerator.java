package com.jx.framework.network;

import java.util.Map;

/**
 * 请求头生成器接口
 */
public interface XHeaderGenerator {
    /**
     * 返回请求头Map，如果为null或无内容，则表示不需要扩展
     * @return 请求头内容
     */
    Map<String, String> getHeader();
}
