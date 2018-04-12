package com.jx.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * 用于存放可持久化的数据
 */
public final class DataCenter {

    private static final String PREFERENCE_NAME = "data-center";

    private final SharedPreferences preferences;

    private DataCenter(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // 单例
    private static DataCenter mInstance;

    public static void init(@NonNull Context context) {
        if (mInstance == null) {
            synchronized (DataCenter.class) {
                if (mInstance == null) {
                    mInstance = new DataCenter(context);
                }
            }
        }
    }

    private static void saveString(String key, String value) {
        SharedPreferences settings = mInstance.preferences;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(String key) {
        SharedPreferences settings = mInstance.preferences;
        try {
            return settings.getString(key, null);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveBoolean(String key, Boolean value) {
        SharedPreferences settings = mInstance.preferences;
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static Boolean getBoolean(String key) {
        SharedPreferences settings = mInstance.preferences;
        try {
            return settings.getBoolean(key, false);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    // token
    private static final String ACCESS_TOKEN = "accessToken";

    /**
     * 保存token
     */
    public static void setAccessToken(String accessToken) {
        synchronized (ACCESS_TOKEN) {
            saveString(ACCESS_TOKEN, accessToken);
        }
    }

    /**
     * 获取token，如果没有返回null
     */
    public static String getAccessToken() {
        synchronized (ACCESS_TOKEN) {
            return getString(ACCESS_TOKEN);
        }
    }

    // 登录账号
    private static final String ACCOUNT = "account";

    /**
     * 保存用户名
     */
    public static void setAccount(String account) {
        synchronized (ACCOUNT) {
            saveString(ACCOUNT, account);
        }
    }

    /**
     * 获取用户名，如果没有返回null
     */
    public static String getAccount() {
        synchronized (ACCOUNT) {
            return getString(ACCOUNT);
        }
    }

    private static final String NICK_NAME = "nickName";

    /**
     * 保存昵称
     */
    public static void setNickName(String nickName) {
        synchronized (NICK_NAME) {
            saveString(NICK_NAME, nickName);
        }
    }

    /**
     * 获取昵称，如果没有返回null
     */
    public static String getNickName() {
        synchronized (NICK_NAME) {
            return getString(NICK_NAME);
        }
    }

}
