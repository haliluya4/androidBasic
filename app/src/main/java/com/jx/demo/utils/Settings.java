package com.jx.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.jx.demo.network.Api;

/**
 * 自定义设置管理
 */
public final class Settings {

    private static final String PREFERENCE_NAME = "settings";

    private final SharedPreferences preferences;

    private Settings(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // 单例
    private static Settings mInstance;

    public static void init(@NonNull Context context) {
        if (mInstance == null) {
            synchronized (Settings.class) {
                if (mInstance == null) {
                    mInstance = new Settings(context);
                }
            }
        }
    }

    // 是否强制调试模式
    private static final String IS_FORCE_DEBUG = "is_force_debug";

    /**
     * 设置强制调试模式，由于debugMode需要在重启后真正生效，故要求强制提交
     */
    @SuppressLint("ApplySharedPref")
    public static void setIsForceDebug(boolean isForceDebug) {
        SharedPreferences settings = mInstance.preferences;
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_FORCE_DEBUG, isForceDebug);
        editor.commit();
    }

    /**
     * 是否强制调试模式
     */
    public static boolean isForceDebug() {
        SharedPreferences settings = mInstance.preferences;
        return settings.getBoolean(IS_FORCE_DEBUG, false);
    }

    // 是否开发设备
    private static final String IS_DEV_DEVICE = "is_dev_device";

    /**
     * 设置是否开发设备，由于dev device需要在重启后真正生效，故要求强制提交
     */
    @SuppressLint("ApplySharedPref")
    public static void setIsDevDevice(boolean isDevDevice) {
        SharedPreferences settings = mInstance.preferences;
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_DEV_DEVICE, isDevDevice);
        editor.commit();
    }

    /**
     * 是否开发设备
     */
    public static boolean isDevDevice() {
        SharedPreferences settings = mInstance.preferences;
        return settings.getBoolean(IS_DEV_DEVICE, false);
    }

    // 当前环境
    private static final String CURRENT_ENV = "current_env";

    /**
     * 设置当前环境，由于环境信息需要在重启后真正生效，故要求强制提交
     */
    @SuppressLint("ApplySharedPref")
    public static void setCurrentEnv(Api.Environment environment) {
        SharedPreferences settings = mInstance.preferences;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CURRENT_ENV, environment.name());
        editor.commit();
    }

    /**
     * 获取当前环境
     */
    public static Api.Environment getCurrentEnv(Api.Environment def) {
        SharedPreferences settings = mInstance.preferences;
        String name = settings.getString(CURRENT_ENV, null);
        if (name != null) {
            for (Api.Environment environment : Api.Environment.values()) {
                if (environment.name().equals(name)) {
                    return environment;
                }
            }
        }
        // 如果都没匹配到或没修改过，则返回当前环境值
        return def;
    }

}
