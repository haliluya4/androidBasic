package com.jx.demo.network;

import com.jx.framework.log.XLogManager;
import com.jx.framework.network.XHeaderGenerator;
import com.jx.demo.utils.DataCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求头生成器，填充accessToken与platform
 */
final class HeaderProcessor implements XHeaderGenerator {
    @Override
    public Map<String, String> getHeader() {
        // TODO 换成自己平台的请求头，可自行扩展
        Map<String, String> headers = new HashMap<>();
        headers.put("platform", "android");
        String accessToken = DataCenter.getAccessToken();
        if (accessToken != null) {
            headers.put("accessToken", accessToken);
        }
        XLogManager.d("okhttp", "accessToken:" + accessToken);
        return headers;
    }

    private HeaderProcessor() {
    }

    private static HeaderProcessor sInstance;

    static HeaderProcessor getInstance() {
        if (sInstance == null) {
            synchronized (HeaderProcessor.class) {
                if (sInstance == null) {
                    sInstance = new HeaderProcessor();
                }
            }
        }
        return sInstance;
    }
}
