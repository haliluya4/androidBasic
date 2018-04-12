package com.jx.demo.base;

import android.app.Activity;
import android.content.Intent;

import com.jx.demo.business.login.LoginActivity;

/**
 * 登录页跳转辅助类
 */
public final class GoToLoginHelper {
    private final Activity mActivity;
    private boolean mIsLoginStarted;

    public GoToLoginHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     * 转到登录页面
     */
    public void gotoLogin() {
        if (!mIsLoginStarted) {
            mIsLoginStarted = true;
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
        }
    }
}
