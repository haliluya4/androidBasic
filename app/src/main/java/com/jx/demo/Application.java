package com.jx.demo;

import android.util.Log;

import com.jx.demo.network.ServiceManager;
import com.jx.demo.utils.DataCenter;
import com.jx.demo.utils.DebugMode;
import com.jx.demo.utils.Settings;
import com.jx.framework.log.XLogManager;
import com.jx.framework.log.XLogger;
import com.tencent.bugly.Bugly;

/**
 * 应用主类
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Settings.init(this);
        DataCenter.init(this);
        DebugMode.checkDebugMode(this);

        XLogManager.setLogger(new XLogger() {
            @Override
            public void d(String tag, String msg) {
                if (DebugMode.isDebugMode()) {
                    Log.d(tag, msg);
                }
            }

            @Override
            public void w(String tag, String msg, Throwable e) {
                Log.w(tag, msg, e);
            }

            @Override
            public void e(String tag, String msg, Throwable e) {
                Log.e(tag, msg, e);
            }
        });

        if (Settings.isDevDevice()) {
            Bugly.setIsDevelopmentDevice(getApplicationContext(), true);
        }
        // TODO 换成自己的bugly appId
        Bugly.init(getApplicationContext(), "xxx", DebugMode.isDebugMode());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ServiceManager.destroy();
        XLogManager.setLogger(null);
    }
}
