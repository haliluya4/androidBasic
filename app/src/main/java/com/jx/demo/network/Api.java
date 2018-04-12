package com.jx.demo.network;

import com.jx.demo.utils.Settings;

/**
 * 接口定义
 */
public final class Api {

    public static final Environment sEnvironment;    // 当前环境，用于支持调试页面展示

    public enum Environment {
        /**
         * 开发环境
         */
        DEV,
        /**
         * 测试环境
         */
        TEST,
        /**
         * 正式环境
         */
        PRODUCT
    }

    static String BASE_URL;

    static {
        // 根据调试选项更新环境
        sEnvironment = Settings.getCurrentEnv(Environment.PRODUCT);
        // TODO 切换为正式域名
        switch (sEnvironment) {
            case DEV:
                BASE_URL = "http://dev-api.xxx.com";
                break;
            case TEST:
                BASE_URL = "http://test-api.xxx.com";
                break;
            default:
                BASE_URL = "https://api.xxx.com";
        }
    }

    private static final String DOMAIN_AGENT = "/agent";

    public static final String LOGIN = DOMAIN_AGENT + "/user/login";

    public static final String GET_USER_INFO = DOMAIN_AGENT + "/user/userInfo";

}
