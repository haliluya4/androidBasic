package com.jx.demo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 调试模式管理器
 */
public final class DebugMode {
    private static boolean sIsDebugMode = true;

    /**
     * 初始化是否是debug模式
     */
    public static void checkDebugMode(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        sIsDebugMode = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        if (!sIsDebugMode) {
            // 非调试版本才判断强制调试
            sIsDebugMode = Settings.isForceDebug();
        }
    }

    /**
     * 是否是调试模式，可以根据当前包及首选项变更
     *
     * @return true-是，false-否
     */
    public static boolean isDebugMode() {
        return sIsDebugMode;
    }
}
