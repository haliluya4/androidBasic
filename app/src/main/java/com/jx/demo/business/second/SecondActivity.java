package com.jx.demo.business.second;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.jx.demo.base.GoToLoginHelper;
import com.jx.demo.base.GoToLoginView;

import butterknife.ButterKnife;

/**
 * 用于演示TOKEN失效自动转登录的用法，只需要实现GoToLoginView，然后调用GoToLoginHelper的gotoLogin即可
 */
public class SecondActivity extends Activity implements GoToLoginView {
    private GoToLoginHelper mGoToLoginHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.second);
        ButterKnife.bind(this);

        mGoToLoginHelper = new GoToLoginHelper(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // return true;//返回真表示返回键被屏蔽掉
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {

    }

    @Override
    public void gotoLogin() {
        mGoToLoginHelper.gotoLogin();
    }
}
